package com.savoryswift.orderservice.service;

import com.savoryswift.orderservice.dto.*;
import com.savoryswift.orderservice.entity.Cart;
import com.savoryswift.orderservice.mapper.CartMapper;
import com.savoryswift.orderservice.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartMapper cartMapper;

    private double calculateDeliveryFee(List<CartItemDTO> items) {
        double base = 150.0;
        return base + (items.size() * 20); // For example
    }

    @Override
    public CartResponseDTO addToCart(CartRequestDTO requestDTO) {
        Cart cart = cartMapper.toEntity(requestDTO);
        double subtotal = cart.getItems().stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();

        double deliveryFee = calculateDeliveryFee(requestDTO.getItems());
        cart.setSubtotal(subtotal);
        cart.setDeliveryFee(deliveryFee);
        cart.setTotal(subtotal + deliveryFee);

        return cartMapper.toDTO(cartRepository.save(cart));
    }

    @Override
    public CartResponseDTO getCartByUserId(String userId) {
        Cart cart = cartRepository.findByUserIdAndCheckedOutFalse(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));
        return cartMapper.toDTO(cart);
    }

    @Override
    public void clearCart(String userId) {
        cartRepository.findByUserIdAndCheckedOutFalse(userId)
                .ifPresent(cartRepository::delete);
    }
}
