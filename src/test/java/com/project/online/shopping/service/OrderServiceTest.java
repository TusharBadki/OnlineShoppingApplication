package com.project.online.shopping.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.project.online.shopping.dto.OrderItemResponse;
import com.project.online.shopping.dto.OrderRequest;
import com.project.online.shopping.dto.OrderResponse;
import com.project.online.shopping.entity.PackagingOption;
import com.project.online.shopping.entity.Product;
import com.project.online.shopping.repository.PackagingOptionRepository;
import com.project.online.shopping.repository.ProductRepository;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private PackagingOptionRepository packagingRepository;

    @InjectMocks
    private OrderService orderService;


    @Test
    void process_shouldCalculateOrderTotalSuccessfully() {

        // Mock Order
        OrderRequest request = new OrderRequest();
        request.setItems(Map.of("CE", 10));

        Product product = new Product();
        product.setCode("CE");
        product.setUnitPrice(BigDecimal.valueOf(5.95));

        PackagingOption option = new PackagingOption();
        option.setQuantity(3);
        option.setPackagePrice(BigDecimal.valueOf(14.95));

        when(productRepository.findByCode("CE"))
            .thenReturn(Optional.of(product));
        when(packagingRepository.findByCode("CE"))
            .thenReturn(new ArrayList<>(List.of(option)));

        OrderResponse response = orderService.process(request);

        assertThat(response).isNotNull();
        assertThat(response.getItemList()).hasSize(1);

        OrderItemResponse item = response.getItemList().get(0);
        assertThat(item.getProductCode()).isEqualTo("CE");
        assertThat(item.getRequestedQuantity()).isEqualTo(10);
        assertThat(item.getTotalPrice()).isNotNull();

        assertThat(response.getTotalOrderCost())
            .isEqualByComparingTo(item.getTotalPrice());

        verify(productRepository).findByCode("CE");
        verify(packagingRepository).findByCode("CE");
    }

    @Test
    void process_shouldThrowException_whenProductNotFound() {

        String productCode = "PP";
        // Mock Order
        OrderRequest request = new OrderRequest();
        request.setItems(Map.of(productCode, 5));

        when(productRepository.findByCode(productCode))
            .thenReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.process(request))
            .isInstanceOf(RuntimeException.class)
            .hasMessage(productCode +" Product not found");
    }


    @Test
    void calculate_shouldUsePackagingOptionsCorrectly() {

        // Mock Product
        Product product = new Product();
        product.setCode("HM");
        product.setUnitPrice(BigDecimal.valueOf(7.95));

        PackagingOption option1 = new PackagingOption();
        option1.setQuantity(5);
        option1.setPackagePrice(BigDecimal.valueOf(29.95));

        PackagingOption option2 = new PackagingOption();
        option2.setQuantity(2);
        option2.setPackagePrice(BigDecimal.valueOf(13.95));

        List<PackagingOption> options = new ArrayList<>();
        options.add(option1);
        options.add(option2);

        BigDecimal total = orderService.calculate(product, 10, options);

        assertThat(total).isEqualByComparingTo(BigDecimal.valueOf(59.90));
    }

    @Test
    void calculate_shouldFallbackToUnitPriceForRemainingQuantity() {

        // Mock Product
        Product product = new Product();
        product.setCode("SS");
        product.setUnitPrice(BigDecimal.valueOf(11.95));

        PackagingOption option = new PackagingOption();
        option.setQuantity(3);
        option.setPackagePrice(BigDecimal.valueOf(30.00));

        List<PackagingOption> options = new ArrayList<>(List.of(option));

        BigDecimal total = orderService.calculate(product, 5, options);

        BigDecimal expected =
            BigDecimal.valueOf(30.00)
                .add(BigDecimal.valueOf(11.95 * 2));

        assertThat(total).isEqualByComparingTo(expected);
    }
}
