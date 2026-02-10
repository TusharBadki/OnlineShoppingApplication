package com.project.online.shopping.mapper;

import com.project.online.shopping.dto.PackagingOptionRequestDto;
import com.project.online.shopping.dto.PackagingOptionResponseDto;
import com.project.online.shopping.entity.PackagingOption;
import org.springframework.stereotype.Component;

@Component
public class PackagingOptionMapper {

        public PackagingOption toEntity(PackagingOptionRequestDto dto) {
        if (dto == null) return null;

        PackagingOption packagingOption = new PackagingOption();
        packagingOption.setCode(dto.getProductCode());
        packagingOption.setQuantity(dto.getProductQuantity());
        packagingOption.setPackagePrice(dto.getPackagePrice());
        return packagingOption;
    }

        public PackagingOptionResponseDto toResponse(PackagingOption packagingOption) {
        if (packagingOption == null) return null;

        PackagingOptionResponseDto dto = new PackagingOptionResponseDto();
        dto.setProductCode(packagingOption.getCode());
        dto.setProductQuantity(packagingOption.getQuantity());
        dto.setPackagePrice(packagingOption.getPackagePrice());
        return dto;
    }

        public void updateEntityFromDto(PackagingOptionRequestDto dto, PackagingOption packagingOption) {

            if (dto == null || packagingOption == null)
                return;

            if (dto.getProductQuantity() > 0) {
                packagingOption.setQuantity(dto.getProductQuantity());
            }

            if (dto.getPackagePrice() != null) {
                packagingOption.setPackagePrice(dto.getPackagePrice());
            }
        }
}
