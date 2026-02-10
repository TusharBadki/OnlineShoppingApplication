package com.project.online.shopping.service;

import com.project.online.shopping.dto.OrderItemResponse;
import com.project.online.shopping.dto.OrderRequest;
import com.project.online.shopping.dto.OrderResponse;
import com.project.online.shopping.entity.PackagingOption;
import com.project.online.shopping.entity.Product;
import com.project.online.shopping.repository.PackagingOptionRepository;
import com.project.online.shopping.repository.ProductRepository;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class OrderService {

    private final ProductRepository productRepository;
    private final PackagingOptionRepository packagingRepository;

    public OrderResponse process(OrderRequest request) {

        List<OrderItemResponse> oderItems = new ArrayList<>();
        BigDecimal completeOrderTotalCost = BigDecimal.ZERO;

        for (Map.Entry<String, Integer> entry : request.getItems().entrySet()) {
            String code = entry.getKey();
            int orderQuantity = entry.getValue();

            Product product =  productRepository.findByCode(code)
                .orElseThrow(() -> new RuntimeException("Product not found"));
            List<PackagingOption> options = packagingRepository.findByCode(code);

            BigDecimal itemTotalCost = calculate(product, orderQuantity, options);
            oderItems.add(new OrderItemResponse(code, orderQuantity, itemTotalCost));
            completeOrderTotalCost = completeOrderTotalCost.add(itemTotalCost);
        }

        return new OrderResponse(oderItems, completeOrderTotalCost);
    }


    public BigDecimal calculate(Product product, int quantity, List<PackagingOption> options) {

        options.sort(Comparator.comparingInt(PackagingOption::getQuantity).reversed());

        //----Actual logic----
        int quantityRemaining = quantity;
        BigDecimal totalCost = BigDecimal.ZERO; // Prices are in BigDecimal
        Map<Integer, Integer> packageBreakdownMap = new LinkedHashMap<>();
        for (PackagingOption option : options) {
            int count = quantityRemaining / option.getQuantity(); //12 / 5 => 2.4 || remaining : 2

            if (count > 0) {
               packageBreakdownMap.put(option.getQuantity(), count); // (5 che -> 2 set)
               totalCost = totalCost.add(option.getPackagePrice().multiply(BigDecimal.valueOf(count)));
               log.info(count + " packages of " + option.getQuantity() + " items ($" + option.getPackagePrice() + " each)");
               quantityRemaining = quantityRemaining - (option.getQuantity() * count);
            }
        }

        if (quantityRemaining > 0) {
            totalCost = totalCost.add(product.getUnitPrice().multiply(BigDecimal.valueOf(quantityRemaining)));
            packageBreakdownMap.put(1, quantityRemaining);
            log.info(quantityRemaining + " packages of 1 items ($" + product.getUnitPrice() + " each)");
        }

        log.info(quantity + product.getCode() +  " for $" + totalCost);
        //return new OrderResponse(totalCost, packageBreakdownMap);
        return totalCost;
    }
}