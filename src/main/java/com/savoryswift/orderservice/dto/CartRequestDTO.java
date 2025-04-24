package com.savoryswift.orderservice.dto;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartRequestDTO {
    private String userId;
    private List<CartItemDTO> items;
}
