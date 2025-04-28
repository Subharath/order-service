package com.savoryswift.orderservice.service;

import com.savoryswift.orderservice.config.RabbitMQConfig;
import com.savoryswift.orderservice.dto.OrderCheckoutRequestDTO;
import com.savoryswift.orderservice.dto.OrderDeliveryMessage;
import com.savoryswift.orderservice.dto.OrderRequestDTO;
import com.savoryswift.orderservice.entity.*;
import com.savoryswift.orderservice.mapper.OrderMapper;
import com.savoryswift.orderservice.repository.OrderRepository;
import com.savoryswift.orderservice.repository.CartRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, CartRepository cartRepository, RabbitTemplate rabbitTemplate) {
        this.orderRepository = orderRepository;
        this.cartRepository = cartRepository;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Autowired
    private OrderMapper orderMapper;

    @Override
    public Order placeOrder(OrderRequestDTO requestDTO) {
        Order order = orderMapper.toEntity(requestDTO);
        return orderRepository.save(order);
    }

    @Override
    public Order updateOrder(String orderId, Order updatedOrder) {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        if (optionalOrder.isEmpty()) {
            throw new RuntimeException("Order not found");
        }

        Order existingOrder = optionalOrder.get();

        if (!existingOrder.getStatus().equals(OrderStatus.CREATED)) {
            throw new RuntimeException("Cannot update order after it’s confirmed by restaurant");
        }

        updatedOrder.setId(existingOrder.getId());
        updatedOrder.setCreatedAt(existingOrder.getCreatedAt());
        updatedOrder.setUpdatedAt(LocalDateTime.now());
        return orderRepository.save(updatedOrder);
    }

    @Override
    public Order updateOrderStatus(String orderId, OrderStatus status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        order.setStatus(status);
        order.setUpdatedAt(LocalDateTime.now());
        Order savedOrder = orderRepository.save(order);

        if (status == OrderStatus.READY_FOR_PICKUP) {
            OrderDeliveryMessage message = new OrderDeliveryMessage(
                    order.getId(),
                    order.getUserId(),
                    order.getDeliveryAddress().getLatitude(),
                    order.getDeliveryAddress().getLongitude()
            );
            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.ORDER_DELIVERY_EXCHANGE,
                    RabbitMQConfig.ORDER_DELIVERY_ROUTING_KEY,
                    message
            );
            log.info("📦 Sent order delivery message to RabbitMQ for orderId: {}", orderId);
        }

        return savedOrder;
    }

    @Override
    public Order assignDeliveryPerson(String orderId, String deliveryPersonId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        order.setDeliveryPersonId(deliveryPersonId);
        order.setUpdatedAt(LocalDateTime.now());
        return orderRepository.save(order);
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public Order getOrderById(String orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }

    @Override
    public List<Order> getOrdersByUserId(String userId) {
        return orderRepository.findByUserId(userId);
    }

    @Override
    public List<Order> getOrdersByRestaurantId(String restaurantId) {
        return orderRepository.findByRestaurantId(restaurantId);
    }

    @Override
    public Order convertCartToOrder(OrderCheckoutRequestDTO requestDTO) {
        Cart cart = cartRepository.findByUserIdAndCheckedOutFalse(requestDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("Cart not found or already checked out"));

        if (cart.getItems() == null || cart.getItems().isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        List<OrderItem> orderItems = cart.getItems().stream()
                .map(cartItem -> new OrderItem(
                        cartItem.getMenuItemId(),
                        cartItem.getName(),
                        cartItem.getPrice(),
                        cartItem.getQuantity(),
                        cartItem.getSpecialInstructions()
                )).toList();

        Address address = Address.builder()
                .street(requestDTO.getStreet())
                .city(requestDTO.getCity())
                .postalCode(requestDTO.getPostalCode())
                .country(requestDTO.getCountry())
                .latitude(requestDTO.getLatitude())
                .longitude(requestDTO.getLongitude())
                .build();

        Order order = new Order();
        order.setUserId(requestDTO.getUserId());
        order.setRestaurantId(cart.getRestaurantId());
        order.setItems(orderItems);
        order.setDeliveryAddress(address);
        order.setCustomerEmail(requestDTO.getCustomerEmail());
        order.setCustomerPhoneNumber(requestDTO.getCustomerPhoneNumber());
        order.setDeliveryFee(cart.getDeliveryFee());
        order.setTotalAmount(cart.getTotal());
        order.setStatus(OrderStatus.CREATED);
        order.setPaymentStatus(PaymentStatus.PENDING);
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());

        Order savedOrder = orderRepository.save(order);

        cart.setCheckedOut(true);
        cartRepository.save(cart);

        log.info("🛒 Cart converted to order for userId: {}", requestDTO.getUserId());

        return savedOrder;
    }

    @Override
    public void markOrderAsPaid(String orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        order.setStatus(OrderStatus.PAID);
        order.setPaymentStatus(PaymentStatus.COMPLETED);
        order.setUpdatedAt(LocalDateTime.now());

        orderRepository.save(order);
        log.info("💰 Order marked as PAID: {}", orderId);
    }
}
