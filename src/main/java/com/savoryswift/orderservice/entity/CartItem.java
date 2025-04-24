package com.savoryswift.orderservice.entity;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItem {

    private String menuItemId;

    private String name;

    private Integer quantity;

    private Double price;
    private String specialInstructions;


}
