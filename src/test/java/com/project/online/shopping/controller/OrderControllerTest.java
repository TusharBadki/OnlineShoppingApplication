package com.project.online.shopping.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.online.shopping.dto.OrderItemResponse;
import com.project.online.shopping.dto.OrderRequest;
import com.project.online.shopping.dto.OrderResponse;
import com.project.online.shopping.service.OrderService;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private OrderService orderService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldPlaceOrderSuccessfully() throws Exception {

            OrderRequest request = new OrderRequest(Map.of("CE", 3, "HM", 5));

            OrderItemResponse item1 =
                new OrderItemResponse("CE", 3, new BigDecimal("3.00"));

            OrderItemResponse item2 =
                new OrderItemResponse("HM", 5, new BigDecimal("10.00"));

            OrderResponse response = new OrderResponse(
                List.of(item1, item2),
                new BigDecimal("13.00")
            );

            Mockito.when(orderService.process(any(OrderRequest.class)))
                .thenReturn(response);

            mockMvc.perform(post("/api/grocery/orders")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.itemList.length()").value(2))
                .andExpect(jsonPath("$.itemList[0].productCode").value("CE"))
                .andExpect(jsonPath("$.itemList[0].requestedQuantity").value(3))
                .andExpect(jsonPath("$.itemList[1].productCode").value("HM"))
                .andExpect(jsonPath("$.itemList[1].requestedQuantity").value(5))
                .andExpect(jsonPath("$.totalOrderCost").value(13.00));
        }
}

