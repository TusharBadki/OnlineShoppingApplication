package com.project.online.shopping.dto;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderItemResponse {
    private String productCode;
    private int requestedQuantity;
    private BigDecimal totalPrice;
}
