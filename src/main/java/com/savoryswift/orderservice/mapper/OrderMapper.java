package com.savoryswift.orderservice.mapper;

import com.savoryswift.orderservice.dto.*;
import com.savoryswift.orderservice.entity.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderMapper {

    public Order toEntity(OrderRequestDTO dto) {
        return Order.builder()
                .userId(dto.getUserId())
                .restaurantId(dto.getRestaurantId())
                .items(toOrderItemList(dto.getItems()))
                .totalAmount(dto.getTotalAmount())
                .deliveryAddress(toAddress(dto.getDeliveryAddress()))
                .build();
    }

    public List<OrderItem> toOrderItemList(List<OrderItemDTO> dtos) {
        return dtos.stream().map(this::toOrderItem).collect(Collectors.toList());
    }

    public OrderItem toOrderItem(OrderItemDTO dto) {
        return OrderItem.builder()
                .menuItemId(dto.getMenuItemId())
                .name(dto.getName())
                .quantity(dto.getQuantity())
                .price(dto.getPrice())
                .specialInstructions(dto.getSpecialInstructions())
                .build();
    }

    public Address toAddress(AddressDTO dto) {
        return Address.builder()
                .street(dto.getStreet())
                .city(dto.getCity())
                //.state(dto.getState())
                .postalCode(dto.getPostalCode())
                .country(dto.getCountry())
                .latitude(dto.getLatitude())
                .longitude(dto.getLongitude())
                .build();
    }
}
