package com.savoryswift.orderservice.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemDTO {
    private String menuItemId;
    private String name;
    private int quantity;
    private double price;
    private String specialInstructions;
}
