package com.project.online.shopping.service;

import com.project.online.shopping.dto.PackagingOptionRequestDto;
import com.project.online.shopping.dto.PackagingOptionResponseDto;
import com.project.online.shopping.entity.PackagingOption;
import com.project.online.shopping.mapper.PackagingOptionMapper;
import com.project.online.shopping.repository.PackagingOptionRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class PackagingOptionService {

    private final PackagingOptionRepository repository;
    private final PackagingOptionMapper mapper;

    public PackagingOptionResponseDto create(PackagingOptionRequestDto dto) {
        PackagingOption packagingOption = mapper.toEntity(dto);
        return mapper.toResponse(repository.save(packagingOption));
    }

    public PackagingOptionResponseDto update(String code, int quantity, PackagingOptionRequestDto dto) {
        PackagingOption packagingOption = repository.findByCodeAndQuantity(code, quantity)
            .orElseThrow(() -> new RuntimeException("Product not found"));

        mapper.updateEntityFromDto(dto, packagingOption);
        return mapper.toResponse(repository.save(packagingOption));
    }

    public PackagingOptionResponseDto find(String code, int quantity) {
        PackagingOption packagingOption =  repository.findByCodeAndQuantity(code, quantity)
          .orElseThrow(() -> new RuntimeException("Product not found"));
      return mapper.toResponse(packagingOption);
    }

    public List<PackagingOptionResponseDto> findAll() {
        return repository.findAll()
            .stream()
            .map(mapper::toResponse)
            .collect(Collectors.toList());
    }

    public void delete(String code) {

        List<PackagingOption> packagingOptions = repository.findByCode(code);
        if (packagingOptions.isEmpty()) {
            throw new RuntimeException("No packaging options found for product code: " + code);
        }
        repository.deleteAll(packagingOptions);
    }

}