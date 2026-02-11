package com.project.online.shopping.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.online.shopping.dto.PackagingOptionRequestDto;
import com.project.online.shopping.dto.PackagingOptionResponseDto;
import com.project.online.shopping.service.PackagingOptionService;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(PackagingController.class)
class PackagingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PackagingOptionService packagingOptionService;

    @Autowired
    private ObjectMapper objectMapper;


    // CREATE Packaging Options Api
    @Test
    void shouldCreatePackagingOption() throws Exception {

        PackagingOptionRequestDto request =
            new PackagingOptionRequestDto("CE", 10, new BigDecimal("5.00"));

        PackagingOptionResponseDto response =
            new PackagingOptionResponseDto("CE", 10, new BigDecimal("5.00"));

        Mockito.when(packagingOptionService.create(any(PackagingOptionRequestDto.class)))
            .thenReturn(response);

        mockMvc.perform(post("/api/packaging-option")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.productCode").value("CE"))
            .andExpect(jsonPath("$.productQuantity").value(10))
            .andExpect(jsonPath("$.packagePrice").value(5.00));
    }

    // GET Packaging Options By Code and Quantity
    @Test
    void shouldReturnPackagingOptionByCodeAndQuantity() throws Exception {

        PackagingOptionResponseDto response =
            new PackagingOptionResponseDto("CE", 10, new BigDecimal("5.00"));

        Mockito.when(packagingOptionService.find("CE", 10))
            .thenReturn(response);

        mockMvc.perform(get("/api/packaging-option/CE/10"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.productCode").value("CE"))
            .andExpect(jsonPath("$.productQuantity").value(10));
    }

     // GET ALL Packaging Options Api
    @Test
    void shouldReturnAllPackagingOptions() throws Exception {

        PackagingOptionResponseDto option1 =
            new PackagingOptionResponseDto("CE", 10, new BigDecimal("5.00"));

        PackagingOptionResponseDto option2 =
            new PackagingOptionResponseDto("HM", 5, new BigDecimal("2.50"));

        Mockito.when(packagingOptionService.findAll())
            .thenReturn(List.of(option1, option2));

        mockMvc.perform(get("/api/packaging-option"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(2))
            .andExpect(jsonPath("$[0].productCode").value("CE"))
            .andExpect(jsonPath("$[1].productCode").value("HM"));
    }

    // UPDATE Packaging Options Api
    @Test
    void shouldUpdatePackagingOption() throws Exception {

        PackagingOptionRequestDto request =
            new PackagingOptionRequestDto("CE", 20, new BigDecimal("8.00"));

        PackagingOptionResponseDto response =
            new PackagingOptionResponseDto("CE", 20, new BigDecimal("8.00"));

        Mockito.when(packagingOptionService.update(eq("CE"), eq(10), any(PackagingOptionRequestDto.class)))
            .thenReturn(response);

        mockMvc.perform(put("/api/packaging-option/CE/10")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.productQuantity").value(20))
            .andExpect(jsonPath("$.packagePrice").value(8.00));
    }

    // DELETE Packaging Options Api
    @Test
    void shouldDeletePackagingOption() throws Exception {

        mockMvc.perform(delete("/api/packaging-option/CE"))
            .andExpect(status().isOk());

        Mockito.verify(packagingOptionService).delete("CE");
    }
}
