package com.savoryswift.orderservice.repository;

import com.savoryswift.orderservice.entity.Order;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends MongoRepository<Order, String> {

    // Find all orders by a specific user
    List<Order> findByUserId(String userId);

    // Find all orders by restaurant
    List<Order> findByRestaurantId(String restaurantId);

    // Find all orders assigned to a specific delivery person
    List<Order> findByDeliveryPersonId(String deliveryPersonId);

    // Optional: Get orders by status
    List<Order> findByStatus(String status);
}
