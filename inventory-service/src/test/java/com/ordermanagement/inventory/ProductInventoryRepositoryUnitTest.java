package com.ordermanagement.inventory;

import com.ordermanagement.inventory.entity.ProductInventory;
import com.ordermanagement.inventory.repository.ProductInventoryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class ProductInventoryRepositoryUnitTest {

    @Autowired
    private ProductInventoryRepository inventoryRepository;


    @DisplayName("JUnit test for fetch all product method")
    @Test
    void findAllShouldReturnProductList() {

        List<ProductInventory> products = this.inventoryRepository.findAll();
        assertEquals(4, products.size());
    }

    @DisplayName("JUnit test for fetch by id method")
    @Test
    void findByProductIdShouldReturnProduct() {

        Optional<ProductInventory> product = this.inventoryRepository.findByProductId(2);
        assertTrue(product.isPresent());
    }

    @DisplayName("JUnit test for save product method")
    @Test
    void saveShouldInsertNewProduct() {

        ProductInventory newProduct = new ProductInventory();
        newProduct.setProductName("NEW_PRODUCT");
        newProduct.setProductId(6);
        newProduct.setNoOfUnits(10);

        ProductInventory persistedProduct = this.inventoryRepository.save(newProduct);

        assertNotNull(persistedProduct);
        assertEquals(5, persistedProduct.getId());
    }

    @DisplayName("JUnit test for update product method")
    @Test
    void saveShouldUpdateExistingProduct() {

        ProductInventory existingProduct = new ProductInventory();
        existingProduct.setId(2);
        existingProduct.setProductId(3);
        existingProduct.setProductName("EXISTING_PRODUCT");
        existingProduct.setNoOfUnits(100);

        ProductInventory updatedProduct = this.inventoryRepository.save(existingProduct);

        assertNotNull(updatedProduct);
        assertEquals("EXISTING_PRODUCT", updatedProduct.getProductName());
        assertEquals(3, updatedProduct.getProductId());
        assertEquals(100, updatedProduct.getNoOfUnits());
    }

    @DisplayName("JUnit test for deleteById method")
    @Test
    void deleteByIdShouldDeleteProduct() {

        this.inventoryRepository.deleteById(3);
        Optional<ProductInventory> product = this.inventoryRepository.findById(3);
        assertFalse(product.isPresent());
    }
}
