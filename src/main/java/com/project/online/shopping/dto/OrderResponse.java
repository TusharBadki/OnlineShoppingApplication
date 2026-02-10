package com.project.online.shopping.dto;

import java.math.BigDecimal;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderResponse {
    private List<OrderItemResponse> itemList;
    private BigDecimal totalOrderCost;
}