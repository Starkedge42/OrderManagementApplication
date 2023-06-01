package com.ordermanagement.inventory.repository;

import com.ordermanagement.inventory.entity.ProductInventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductInventoryRepository extends JpaRepository<ProductInventory, Integer> {
    Optional<ProductInventory> findByProductId(Integer productId);
}