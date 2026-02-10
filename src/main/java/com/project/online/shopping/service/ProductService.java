package com.project.online.shopping.service;

import com.project.online.shopping.mapper.ProductMapper;
import com.project.online.shopping.dto.ProductRequestDto;
import com.project.online.shopping.dto.ProductResponseDto;
import com.project.online.shopping.entity.Product;
import com.project.online.shopping.repository.ProductRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {

    private final ProductRepository repository;
    private final ProductMapper mapper;

    public ProductResponseDto create(ProductRequestDto dto) {
        Product product = mapper.toEntity(dto);
        return mapper.toResponse(repository.save(product));
    }

    public ProductResponseDto update(String code, ProductRequestDto dto) {
        Product product = repository.findByCode(code)
            .orElseThrow(() -> new RuntimeException("Product not found"));

        mapper.updateEntityFromDto(dto, product);
        return mapper.toResponse(repository.save(product));
    }

    public ProductResponseDto find(String code) {
      Product product =  repository.findByCode(code)
          .orElseThrow(() -> new RuntimeException("Product not found"));
      return mapper.toResponse(product);
    }

    public List<ProductResponseDto> findAll() {
        return repository.findAll()
            .stream()
            .map(mapper::toResponse)
            .collect(Collectors.toList());
    }

    public void delete(String code) {
        Product product = repository.findByCode(code).orElseThrow();
        repository.delete(product);
    }
}