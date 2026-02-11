package com.project.online.shopping.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.project.online.shopping.dto.ProductRequestDto;
import com.project.online.shopping.dto.ProductResponseDto;
import com.project.online.shopping.entity.Product;
import com.project.online.shopping.mapper.ProductMapper;
import com.project.online.shopping.repository.ProductRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository repository;

    @Mock
    private ProductMapper mapper;

    @InjectMocks
    private ProductService productService;

    // Create Product
    @Test
    void shouldCreateProduct() {
        ProductRequestDto requestDto = new ProductRequestDto("CE", "Cheese", BigDecimal.valueOf(30.00));
        Product product = new Product(10L, "CE", "Cheese", BigDecimal.valueOf(30.00));
        Product savedProduct = new Product(10L, "CE", "Cheese", BigDecimal.valueOf(30.00));
        ProductResponseDto responseDto = new ProductResponseDto("CE", "Cheese", BigDecimal.valueOf(30.00));

        when(mapper.toEntity(requestDto)).thenReturn(product);
        when(repository.save(product)).thenReturn(savedProduct);
        when(mapper.toResponse(savedProduct)).thenReturn(responseDto);

        ProductResponseDto result = productService.create(requestDto);

        assertNotNull(result);
        assertEquals("CE", result.getProductCode());

        verify(repository).save(product);
        verify(mapper).toEntity(requestDto);
        verify(mapper).toResponse(savedProduct);
    }

   // Update Product
    @Test
    void shouldUpdateProduct() {
        String code = "CE";
        ProductRequestDto requestDto = new ProductRequestDto("CE", "Feta Cheese", BigDecimal.valueOf(30.00));
        Product existingProduct = new Product(10L, "CE", "Mozzarella Cheese", BigDecimal.valueOf(30.00));
        ProductResponseDto responseDto = new ProductResponseDto("CE", "Feta Cheese", BigDecimal.valueOf(30.00));

        when(repository.findByCode(code)).thenReturn(Optional.of(existingProduct));
        when(repository.save(existingProduct)).thenReturn(existingProduct);
        when(mapper.toResponse(existingProduct)).thenReturn(responseDto);

        ProductResponseDto result = productService.update(code, requestDto);

        assertEquals("Feta Cheese", result.getProductName());

        verify(mapper).updateEntityFromDto(requestDto, existingProduct);
        verify(repository).save(existingProduct);
    }

    // Update Api :  product Not found
    @Test
    void shouldThrowExceptionWhenUpdatingNonExistingProduct() {
        when(repository.findByCode("CE")).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
            () -> productService.update("CE", new ProductRequestDto()));

        assertEquals("Product not found", exception.getMessage());
    }

    // Find / Get Product
    @Test
    void shouldFindProductByCode() {
        String code = "CE";
        Product product = new Product(10L, "CE", "Cheese", BigDecimal.valueOf(30.00));
        ProductResponseDto responseDto = new ProductResponseDto("CE", "Cheese", BigDecimal.valueOf(30.00));

        when(repository.findByCode(code)).thenReturn(Optional.of(product));
        when(mapper.toResponse(product)).thenReturn(responseDto);

        ProductResponseDto result = productService.find(code);

        assertEquals("CE", result.getProductCode());
    }

    // Get product : Not Found
    @Test
    void shouldThrowExceptionWhenProductNotFound() {
        when(repository.findByCode("CE")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
            () -> productService.find("CE"));
    }

    // Get all products
    @Test
    void shouldReturnAllProducts() {
        Product product1 = new Product(10L, "CE", "Cheese", BigDecimal.valueOf(30.00));
        Product product2 = new Product(11L, "HM", "Ham", BigDecimal.valueOf(40.00));

        ProductResponseDto dto1 = new ProductResponseDto("CE", "Cheese", BigDecimal.valueOf(30.00));
        ProductResponseDto dto2 = new ProductResponseDto("HM", "Ham", BigDecimal.valueOf(40.00));

        when(repository.findAll()).thenReturn(List.of(product1, product2));
        when(mapper.toResponse(product1)).thenReturn(dto1);
        when(mapper.toResponse(product2)).thenReturn(dto2);

        List<ProductResponseDto> result = productService.findAll();

        assertEquals(2, result.size());
        verify(repository).findAll();
    }

    // Delete product
    @Test
    void shouldDeleteProduct() {
        String code = "CE";
        Product product = new Product(12L, "CE", "Cheese", BigDecimal.valueOf(10.00));

        when(repository.findByCode(code)).thenReturn(Optional.of(product));

        productService.delete(code);

        verify(repository).delete(product);
    }

   // Delete Api : product not found
    @Test
    void shouldThrowExceptionWhenDeletingNonExistingProduct() {
        when(repository.findByCode("LM")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
            () -> productService.delete("LM"));
    }
}
