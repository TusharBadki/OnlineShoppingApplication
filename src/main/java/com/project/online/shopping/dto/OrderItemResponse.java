package com.project.online.shopping.dto;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemResponse {
    private String productCode;
    private int requestedQuantity;
    private BigDecimal totalPrice;
}
