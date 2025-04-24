package com.savoryswift.orderservice.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemDTO {
    private String menuItemId;
    private String name;
    private Integer quantity;
    private Double price;
    private String specialInstructions;
}
