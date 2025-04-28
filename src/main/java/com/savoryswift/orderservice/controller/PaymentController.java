package com.savoryswift.orderservice.controller;

import com.savoryswift.orderservice.dto.PaymentRequestDTO;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/payments")
@CrossOrigin(origins = "*") // for frontend access later
public class PaymentController {

    @Value("${stripe.public.key}")
    private String stripePublicKey;

    @PostMapping("/create-session")
    public ResponseEntity<Map<String, String>> createCheckoutSession(@RequestBody PaymentRequestDTO requestDTO) {
        try {
            // Build line items for each order item
            List<SessionCreateParams.LineItem> stripeLineItems = new ArrayList<>();

            requestDTO.getItems().forEach(item -> {
                stripeLineItems.add(
                        SessionCreateParams.LineItem.builder()
                                .setQuantity(Long.valueOf(item.getQuantity()))
                                .setPriceData(
                                        SessionCreateParams.LineItem.PriceData.builder()
                                                .setCurrency("lkr")
                                                .setUnitAmount((long) (item.getPrice() * 100)) // convert to cents
                                                .setProductData(
                                                        SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                .setName(item.getName())
                                                                .build()
                                                )
                                                .build()
                                )
                                .build()
                );
            });

            // Build the session
            SessionCreateParams params = SessionCreateParams.builder()
                    .setMode(SessionCreateParams.Mode.PAYMENT)
                    .setSuccessUrl("http://localhost:3000/payment-success?session_id={CHECKOUT_SESSION_ID}")
                    .setCancelUrl("http://localhost:3000/payment-cancel")
                    .addAllLineItem(stripeLineItems)
                    .build();

            Session session = Session.create(params);

            Map<String, String> response = new HashMap<>();
            response.put("checkoutUrl", session.getUrl());
            response.put("sessionId", session.getId());
            response.put("stripePublicKey", stripePublicKey);

            return ResponseEntity.ok(response);

        } catch (StripeException e) {
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }
}
