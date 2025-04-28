package com.savoryswift.orderservice.service;

import com.savoryswift.orderservice.dto.OrderCheckoutRequestDTO;
import com.savoryswift.orderservice.dto.OrderRequestDTO;
import com.savoryswift.orderservice.entity.*;
import com.savoryswift.orderservice.mapper.OrderMapper;
import com.savoryswift.orderservice.repository.OrderRepository;
import com.savoryswift.orderservice.repository.CartRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, CartRepository cartRepository) {
        this.orderRepository = orderRepository;
        this.cartRepository = cartRepository;
    }

    @Autowired
    private OrderMapper orderMapper;

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
        return orderRepository.save(order);
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
        // 1. Fetch Cart
        Cart cart = cartRepository.findByUserIdAndCheckedOutFalse(requestDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("Cart not found or already checked out"));

        if (cart.getItems() == null || cart.getItems().isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        // 2. Map Cart Items to Order Items
        List<OrderItem> orderItems = cart.getItems().stream()
                .map(cartItem -> new OrderItem(
                        cartItem.getMenuItemId(),
                        cartItem.getName(),
                        cartItem.getPrice(),
                        cartItem.getQuantity(),
                        cartItem.getSpecialInstructions()
                )).toList();

        // 3. Build Address
        Address address = Address.builder()
                .street(requestDTO.getStreet())
                .city(requestDTO.getCity())
                .postalCode(requestDTO.getPostalCode())
                .country(requestDTO.getCountry())
                .latitude(requestDTO.getLatitude())
                .longitude(requestDTO.getLongitude())
                .build();

        // 4. Create Order
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

        // 5. Save Order
        Order savedOrder = orderRepository.save(order);

        // 6. Mark cart as checked out
        cart.setCheckedOut(true);
        cartRepository.save(cart);

        return savedOrder;
    }

    @Override
    public void markOrderAsPaid(String orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        order.setStatus(OrderStatus.PAID); // or your equivalent
        order.setPaymentStatus(PaymentStatus.COMPLETED);

        orderRepository.save(order);
        log.info("💰 Order marked as PAID: {}", orderId);
    }





}
