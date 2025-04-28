package com.savoryswift.orderservice.mapper;

import com.savoryswift.orderservice.dto.*;
import com.savoryswift.orderservice.entity.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CartMapper {

    public Cart toEntity(CartRequestDTO dto) {
        return Cart.builder()
                .userId(dto.getUserId())
                .restaurantId(dto.getRestaurantId())
                .items(dto.getItems().stream()
                        .map(this::toEntity)
                        .collect(Collectors.toList()))
                .build();
    }

    public CartItem toEntity(CartItemDTO dto) {
        return CartItem.builder()
                .menuItemId(dto.getMenuItemId())
                .name(dto.getName())
                .price(dto.getPrice())
                .quantity(dto.getQuantity())
                .specialInstructions(dto.getSpecialInstructions())
                .build();
    }

    public CartResponseDTO toDTO(Cart cart) {
        return CartResponseDTO.builder()
                .id(cart.getId())
                .userId(cart.getUserId())
                .restaurantId(cart.getRestaurantId())
                .items(cart.getItems().stream()
                        .map(this::toDTO)
                        .collect(Collectors.toList()))
                .subtotal(cart.getSubtotal())
                .deliveryFee(cart.getDeliveryFee())
                .total(cart.getTotal())
                .checkedOut(cart.isCheckedOut())
                .build();
    }

    public CartItemDTO toDTO(CartItem item) {
        return CartItemDTO.builder()
                .menuItemId(item.getMenuItemId())
                .name(item.getName())
                .price(item.getPrice())
                .quantity(item.getQuantity())
                .specialInstructions(item.getSpecialInstructions())
                .build();
    }
}
