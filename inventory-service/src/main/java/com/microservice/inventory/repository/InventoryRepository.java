package com.microservice.inventory.repository;

import com.microservice.inventory.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.Optional;

public interface InventoryRepository extends JpaRepository<Inventory,Long> {
    Optional<Inventory> findBySkuCode(String skuCode);

    Optional<Inventory> findInventoryByProductName(String prodName);

    Optional<Inventory> findInventoryByProductId(String productId);
}
