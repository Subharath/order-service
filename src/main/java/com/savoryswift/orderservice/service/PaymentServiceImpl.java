package com.savoryswift.orderservice.service;

import com.savoryswift.orderservice.dto.PaymentRequestDTO;
import com.savoryswift.orderservice.dto.PaymentResponseDTO;
import com.savoryswift.orderservice.enums.PaymentStatus;
import com.savoryswift.orderservice.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
public class PaymentServiceImpl implements PaymentService {

    @Override
    public PaymentResponseDTO processPayment(PaymentRequestDTO request) {
        log.info("Processing payment for order: {}", request.getOrderId());

        // Simulated payment gateway logic
        if ("VALID".equalsIgnoreCase(request.getPaymentToken())) {
            String paymentId = UUID.randomUUID().toString();
            log.info("Payment successful. Payment ID: {}", paymentId);
            return new PaymentResponseDTO("SUCCESS", paymentId);
        }

        log.warn("Payment failed.");
        return new PaymentResponseDTO("FAILED", null);
    }
}
