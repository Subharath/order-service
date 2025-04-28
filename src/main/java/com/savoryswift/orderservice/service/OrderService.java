package com.savoryswift.orderservice.service;

import com.savoryswift.orderservice.dto.OrderCheckoutRequestDTO;
import com.savoryswift.orderservice.dto.OrderRequestDTO;
import com.savoryswift.orderservice.entity.Order;
import com.savoryswift.orderservice.entity.OrderStatus;

import java.util.List;

public interface OrderService {

    Order placeOrder(OrderRequestDTO requestDTO);

    Order updateOrder(String orderId, Order updatedOrder);

    Order updateOrderStatus(String orderId, OrderStatus status);

    Order assignDeliveryPerson(String orderId, String deliveryPersonId);

    List<Order> getAllOrders();

    Order getOrderById(String orderId);

    List<Order> getOrdersByUserId(String userId);

    List<Order> getOrdersByRestaurantId(String restaurantId);

    //Order convertCartToOrder(String userId, String deliveryAddressId);

    Order convertCartToOrder(OrderCheckoutRequestDTO requestDTO);

    void markOrderAsPaid(String orderId);

}
