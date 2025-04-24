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
    @Setter
    private List<CartItem> items;
    private double subtotal;
    @Setter
    private double deliveryFee;
    private double total;
    //mod
    private boolean checkedOut = false;
    @Getter
    private String restaurantId;
    @Getter
    private String deliveryAddress;
    @Getter
    private Double totalAmount;


}
