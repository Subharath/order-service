package com.savoryswift.orderservice.entity;

public enum OrderStatus {
    CREATED,
    RESTAURANT_CONFIRMED,
    PREPARING,
    READY_FOR_PICKUP,
    PICKED_UP,
    ON_THE_WAY,
    DELIVERED,
    CANCELLED
}
