package com.project.online.shopping.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.online.shopping.dto.ProductRequestDto;
import com.project.online.shopping.dto.ProductResponseDto;
import com.project.online.shopping.service.ProductService;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    // POST Create product
    @Test
    void createProduct_shouldReturnCreatedProduct() throws Exception {
        ProductRequestDto request =
            new ProductRequestDto("CE", "Cheese", new BigDecimal("5.95"));

        ProductResponseDto response =
            new ProductResponseDto("CE", "Cheese", new BigDecimal("5.95"));

        Mockito.when(productService.create(Mockito.any()))
            .thenReturn(response);

        mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.productCode").value("CE"))
            .andExpect(jsonPath("$.productName").value("Cheese"))
            .andExpect(jsonPath("$.unitPrice").value(5.95));
    }

    // GET Products by code
    @Test
    void getProduct_shouldReturnProduct() throws Exception {
        ProductResponseDto response =
            new ProductResponseDto("CE", "Cheese", new BigDecimal("5.95"));

        Mockito.when(productService.find("CE")).thenReturn(response);

        mockMvc.perform(get("/api/products/CE"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.productCode").value("CE"))
            .andExpect(jsonPath("$.productName").value("Cheese"));
    }

    // GET all products
    @Test
    void getAllProducts_shouldReturnList() throws Exception {
        List<ProductResponseDto> products = List.of(
            new ProductResponseDto("CE", "Cheese", new BigDecimal("5.95")),
            new ProductResponseDto("HM", "Ham", new BigDecimal("7.95"))
        );

        Mockito.when(productService.findAll()).thenReturn(products);

        mockMvc.perform(get("/api/products"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(2))
            .andExpect(jsonPath("$[0].productCode").value("CE"))
            .andExpect(jsonPath("$[1].productCode").value("HM"));
    }

    //  PUT Update product Api
    @Test
    void updateProduct_shouldReturnUpdatedProduct() throws Exception {
        ProductRequestDto request =
            new ProductRequestDto("CE", "Feta Cheese", new BigDecimal("6.50"));

        ProductResponseDto response =
            new ProductResponseDto("CE", "Feta Cheese", new BigDecimal("6.50"));

        Mockito.when(productService.update(Mockito.eq("CE"), Mockito.any()))
            .thenReturn(response);

        mockMvc.perform(put("/api/products/CE")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.productName").value("Feta Cheese"))
            .andExpect(jsonPath("$.unitPrice").value(6.50));
    }

    // DELETE Product Api
    @Test
    void deleteProduct_shouldReturnOk() throws Exception {
        Mockito.doNothing().when(productService).delete("CE");

        mockMvc.perform(delete("/api/products/CE"))
            .andExpect(status().isOk());
    }
}