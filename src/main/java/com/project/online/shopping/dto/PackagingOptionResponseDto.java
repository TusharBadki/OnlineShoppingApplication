package com.project.online.shopping.dto;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PackagingOptionResponseDto {

    private String productCode;
    private int productQuantity;
    private BigDecimal packagePrice;
}