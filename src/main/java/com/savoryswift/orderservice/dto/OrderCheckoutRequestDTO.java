package com.savoryswift.orderservice.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderCheckoutRequestDTO {
    private String userId;
    private String customerEmail;
    private String customerPhoneNumber;
    private String street;
    private String city;
    private String postalCode;
    private String country;
    private Double latitude;
    private Double longitude;
}
