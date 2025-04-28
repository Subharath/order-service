package com.savoryswift.orderservice.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "carts")

public class Cart {
    @Id
    private String id;
    private String userId;

    private List<CartItem> items;
    private double subtotal;

    private double deliveryFee;
    private double total;
    //mod
    private boolean checkedOut = false;
    private String restaurantId;

    private String deliveryAddress;

    private Double totalAmount;


}
