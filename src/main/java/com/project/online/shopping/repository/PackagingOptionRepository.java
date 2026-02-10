package com.project.online.shopping.repository;

import com.project.online.shopping.entity.PackagingOption;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PackagingOptionRepository extends JpaRepository<PackagingOption, Long> {
    Optional<PackagingOption> findByCodeAndQuantity(String productCode, int quantity);
    List<PackagingOption> findByCode(String productCode);
}