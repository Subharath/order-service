package com.savoryswift.orderservice.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentRequestDTO {
    private String orderId;
    private double amount;
    private String paymentMethod; // "CARD", "MOBILE", "STRIPE", etc.
    private String paymentToken;// token/nonce from frontend/gateway

    private List<Item> items;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Item {
        private String name;
        private double price;
        private int quantity;
    }





}



