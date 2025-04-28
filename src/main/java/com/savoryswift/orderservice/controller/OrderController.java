package com.savoryswift.orderservice.controller;

import com.savoryswift.orderservice.dto.CartToOrderRequestDTO;
import com.savoryswift.orderservice.dto.OrderCheckoutRequestDTO;
import com.savoryswift.orderservice.dto.OrderRequestDTO;
import com.savoryswift.orderservice.entity.Order;
import com.savoryswift.orderservice.entity.OrderStatus;
import com.savoryswift.orderservice.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "*")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    /* ✅ Place a new order
    @PostMapping
    public ResponseEntity<Order> placeOrder(@RequestBody Order order) {
        return ResponseEntity.ok(orderService.placeOrder(order));
    }*/

    @PostMapping
    public ResponseEntity<Order> placeOrder(@RequestBody OrderRequestDTO orderRequestDTO) {
        return ResponseEntity.ok(orderService.placeOrder(orderRequestDTO));
    }


    // ✅ Update an order (only if still in CREATED state)
    @PutMapping("/{orderId}")
    public ResponseEntity<Order> updateOrder(@PathVariable String orderId, @RequestBody Order order) {
        return ResponseEntity.ok(orderService.updateOrder(orderId, order));
    }

    // ✅ Change the status of an order
    @PutMapping("/{orderId}/status")
    public ResponseEntity<Order> updateOrderStatus(@PathVariable String orderId, @RequestParam OrderStatus status) {
        return ResponseEntity.ok(orderService.updateOrderStatus(orderId, status));
    }

    // ✅ Assign delivery personnel
    @PutMapping("/{orderId}/assign-delivery")
    public ResponseEntity<Order> assignDelivery(@PathVariable String orderId, @RequestParam String deliveryPersonId) {
        return ResponseEntity.ok(orderService.assignDeliveryPerson(orderId, deliveryPersonId));
    }

    // ✅ Get all orders
    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    // ✅ Get order by ID
    @GetMapping("/{orderId}")
    public ResponseEntity<Order> getOrderById(@PathVariable String orderId) {
        return ResponseEntity.ok(orderService.getOrderById(orderId));
    }

    // ✅ Get orders by user
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Order>> getOrdersByUser(@PathVariable String userId) {
        return ResponseEntity.ok(orderService.getOrdersByUserId(userId));
    }

    // ✅ Get orders by restaurant
    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<List<Order>> getOrdersByRestaurant(@PathVariable String restaurantId) {
        return ResponseEntity.ok(orderService.getOrdersByRestaurantId(restaurantId));
    }

    /*@PostMapping("/cart/checkout")
    public ResponseEntity<Order> checkoutCart(@RequestBody CartToOrderRequestDTO dto) {
        Order order = orderService.convertCartToOrder(dto.getUserId(), dto.getDeliveryAddressId());
        return ResponseEntity.ok(order);
    }*/
    //modded
    @PostMapping("/cart/checkout")
    public ResponseEntity<Order> checkoutCart(@RequestBody OrderCheckoutRequestDTO requestDTO) {
        Order order = orderService.convertCartToOrder(requestDTO);
        return ResponseEntity.ok(order);
    }

}
