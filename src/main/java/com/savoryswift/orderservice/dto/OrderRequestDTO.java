package com.savoryswift.orderservice.dto;

import com.savoryswift.orderservice.entity.OrderStatus;
import com.savoryswift.orderservice.entity.PaymentStatus;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderRequestDTO {
    private String userId;
    private String restaurantId;
    private List<OrderItemDTO> items;
    private Double totalAmount;
    private AddressDTO deliveryAddress;
    private String customerEmail;
    private String customerPhoneNumber;

}
