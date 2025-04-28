package com.savoryswift.orderservice.service;

import com.savoryswift.orderservice.dto.*;

public interface CartService {
    CartResponseDTO addToCart(CartRequestDTO requestDTO);
    CartResponseDTO getCartByUserId(String userId);
    void clearCart(String userId);
}
