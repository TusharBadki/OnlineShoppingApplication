package com.project.online.shopping.mapper;

import com.project.online.shopping.dto.ProductRequestDto;
import com.project.online.shopping.dto.ProductResponseDto;
import com.project.online.shopping.entity.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public Product toEntity(ProductRequestDto dto) {
        if (dto == null) return null;

        Product product = new Product();
        product.setCode(dto.getProductCode());
        product.setName(dto.getProductName());
        product.setUnitPrice(dto.getUnitPrice());
        return product;
    }

    public ProductResponseDto toResponse(Product product) {
        if (product == null) return null;

        ProductResponseDto dto = new ProductResponseDto();
        dto.setProductCode(product.getCode());
        dto.setProductName(product.getName());
        dto.setUnitPrice(product.getUnitPrice());
        return dto;
    }

    public void updateEntityFromDto(ProductRequestDto dto, Product product) {
        if (dto == null || product == null) return;

        if (dto.getProductName() != null) {
            product.setName(dto.getProductName());
        }
        if (dto.getUnitPrice() != null) {
            product.setUnitPrice(dto.getUnitPrice());
        }
    }
}
