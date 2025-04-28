package com.savoryswift.orderservice.dto;

import lombok.Data;

@Data
public class CartToOrderRequestDTO {
    private String userId;
    private String deliveryAddressId; // If you're selecting from saved addresses
}
