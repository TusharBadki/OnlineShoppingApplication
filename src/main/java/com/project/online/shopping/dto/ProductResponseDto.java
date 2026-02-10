package com.project.online.shopping.dto;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductResponseDto {
    private String productCode;
    private String productName;
    private BigDecimal unitPrice;
}