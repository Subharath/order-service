package com.savoryswift.orderservice.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {

    @Id
    private String id;

    private String userId;
    private String restaurantId;
    @Setter
    @Getter
    private List<OrderItem> items;
    private Double totalAmount;

    private OrderStatus status;
    private PaymentStatus paymentStatus;

    private Address deliveryAddress;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private String deliveryPersonId;
    private String paymentId;
    private String trackingId;

    public void setDeliveryFee(double deliveryFee) {
    }

    public void setDeliveryAddress(String deliveryAddress) {
    }

}

