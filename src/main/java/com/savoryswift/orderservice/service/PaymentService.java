package com.savoryswift.orderservice.service;

import com.savoryswift.orderservice.dto.PaymentRequestDTO;
import com.savoryswift.orderservice.dto.PaymentResponseDTO;

public interface PaymentService {
    PaymentResponseDTO processPayment(PaymentRequestDTO request);
}
