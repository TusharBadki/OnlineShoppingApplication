package com.project.online.shopping.controller;

import com.project.online.shopping.dto.PackagingOptionRequestDto;
import com.project.online.shopping.dto.PackagingOptionResponseDto;
import com.project.online.shopping.service.PackagingOptionService;
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
@RequestMapping("/api/v1/packaging-option")
@RequiredArgsConstructor
public class PackagingController {

    private final PackagingOptionService packagingOptionService;

    @PostMapping
    public PackagingOptionResponseDto create(@RequestBody PackagingOptionRequestDto requestPackagingOption) {
        return packagingOptionService.create(requestPackagingOption);
    }

    @GetMapping("/{code}/{quantity}")
    public PackagingOptionResponseDto get(@PathVariable String code, @PathVariable int quantity) {
        return packagingOptionService.find(code, quantity);
    }

    @GetMapping
    public List<PackagingOptionResponseDto> getAllPackagingOptions() {
        return packagingOptionService.findAll();
    }

    @PutMapping("/{code}/{quantity}")
    public PackagingOptionResponseDto update(@PathVariable String code, @PathVariable int quantity, @RequestBody PackagingOptionRequestDto requestPackagingOption) {
        return packagingOptionService.update(code, quantity, requestPackagingOption);
    }

    @DeleteMapping("/{code}")
    public void delete(@PathVariable String code) {
        packagingOptionService.delete(code);
    }
}