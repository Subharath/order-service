package com.savoryswift.orderservice.dto;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartResponseDTO {
    private String id;
    private String userId;
    private List<CartItemDTO> items;
    private double subtotal;
    private double deliveryFee;
    private double total;
    private boolean checkedOut;
}
