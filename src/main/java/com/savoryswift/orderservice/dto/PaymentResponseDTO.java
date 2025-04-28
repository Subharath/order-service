package com.savoryswift.orderservice.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentResponseDTO {
    private String status;      // "SUCCESS", "FAILED"
    private String paymentId;   // Generated reference ID
}
