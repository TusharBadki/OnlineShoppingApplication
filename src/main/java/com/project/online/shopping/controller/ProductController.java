package com.project.online.shopping.controller;

import com.project.online.shopping.dto.ProductRequestDto;
import com.project.online.shopping.dto.ProductResponseDto;
import com.project.online.shopping.service.ProductService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ProductResponseDto create(@RequestBody ProductRequestDto requestProduct) {
        return productService.create(requestProduct);
    }

    @GetMapping("/{code}")
    public ProductResponseDto get(@PathVariable String code) {
        return productService.find(code);
    }

    @GetMapping
    public List<ProductResponseDto> getAllProducts() {
        return productService.findAll();
    }

    @PutMapping("/{code}")
    public ProductResponseDto update(@PathVariable String code, @RequestBody ProductRequestDto requestProduct) {
        return productService.update(code, requestProduct);
    }

    @DeleteMapping("/{code}")
    public void delete(@PathVariable String code) {
        productService.delete(code);
    }
}