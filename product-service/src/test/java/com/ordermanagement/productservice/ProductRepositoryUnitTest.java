package com.ordermanagement.productservice;

import com.ordermanagement.productservice.entity.Product;
import com.ordermanagement.productservice.repository.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class ProductRepositoryUnitTest {

    @Autowired
    private ProductRepository productRepository;


    @DisplayName("JUnit test for fetch all product method")
    @Test
    void findAllShouldReturnProductList() {

        List<Product> products = this.productRepository.findAll();
        assertEquals(4, products.size());
    }

    @DisplayName("JUnit test for fetch by id method")
    @Test
    void findByIdShouldReturnProduct() {

        Optional<Product> product = this.productRepository.findById(2);
        assertTrue(product.isPresent());
    }

    @DisplayName("JUnit test for save product method")
    @Test
    void saveShouldInsertNewProduct() {

        Product newProduct = new Product();
        newProduct.setName("NEW_PRODUCT");
        newProduct.setPrice(200);

        Product persistedProduct = this.productRepository.save(newProduct);

        assertNotNull(persistedProduct);
        assertEquals(5, persistedProduct.getId());
    }

    @DisplayName("JUnit test for update product method")
    @Test
    void saveShouldUpdateExistingProduct() {

        Product existingProduct = new Product();
        existingProduct.setId(3);
        existingProduct.setName("EXISTING_PRODUCT");
        existingProduct.setPrice(1000);

        Product updatedProduct = this.productRepository.save(existingProduct);

        assertNotNull(updatedProduct);
        assertEquals("EXISTING_PRODUCT", updatedProduct.getName());
        assertEquals(1000, updatedProduct.getPrice());
    }

    @DisplayName("JUnit test for deleteById method")
    @Test
    void deleteByIdShouldDeleteProduct() {

        this.productRepository.deleteById(2);
        Optional<Product> product = this.productRepository.findById(2);
        assertFalse(product.isPresent());
    }
}
