package com.savoryswift.orderservice.entity;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItem {
    private String menuItemId;
    private String name;

    private Integer quantity;
    private Double price;
    private String specialInstructions;



    public OrderItem(String menuItemId, String name, Double price, int quantity, String specialInstructions) {
        this.menuItemId = menuItemId;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.specialInstructions = specialInstructions;
    }
}
