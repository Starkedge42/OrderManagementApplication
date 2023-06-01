package com.ordermanagement.productservice;

import com.ordermanagement.productservice.entity.Product;
import com.ordermanagement.productservice.exception.ResourceNotFoundException;
import com.ordermanagement.productservice.repository.ProductRepository;
import com.ordermanagement.productservice.service.ProductServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceUnitTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    private Product product;

    @BeforeEach
    public void setup(){
        product = this.buildTestingProduct();
    }

    @DisplayName("JUnit test for fetch all product method")
    @Test
    void getAllProductsShouldReturnProductList() {

        when(productRepository.findAll()).thenReturn(List.of(product));
        List<Product> products = this.productService.getAllProducts();

        assertEquals(1, products.size());
        verify(this.productRepository).findAll();
    }

    @DisplayName("JUnit test for fetch product by id method - positive scenario")
    @Test
    void getProductShouldReturnProduct() {

        when(productRepository.findById(any(Integer.class))).thenReturn(Optional.of(product));
        Product returnedProduct = this.productService.getProduct(product.getId());

        assertEquals(product.getName(), returnedProduct.getName());
        assertEquals(product.getId(), returnedProduct.getId());
        assertEquals(product.getPrice(), returnedProduct.getPrice());
        verify(this.productRepository).findById(product.getId());
    }

    @DisplayName("JUnit test for fetch product by id method - negative scenario")
    @Test
    void getProductShouldThrowException() {

        when(productRepository.findById(any(Integer.class))).thenThrow(ResourceNotFoundException.class);

        assertThrows(ResourceNotFoundException.class, () ->{
            productService.getProduct(product.getId());
        });

        verify(this.productRepository).findById(product.getId());
    }

    @DisplayName("JUnit test for save product method")
    @Test
    void addProductShouldInsertNewProduct() {
        this.productService.addProduct(product);
        verify(this.productRepository).save(product);
    }

    @DisplayName("JUnit test for updateProduct - Positive Scenario")
    @Test
    void updateProductShouldUpdateProductAndReturnProductObject() {

        when(productRepository.findById(any(Integer.class))).thenReturn(Optional.ofNullable(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        product.setPrice(890);
        product.setName("UPDATED_PRODUCT_NAME");
        Product updatedProduct = this.productService.updateProduct(product);

        assertThat(updatedProduct.getName()).isEqualTo("UPDATED_PRODUCT_NAME");
        assertThat(updatedProduct.getPrice()).isEqualTo(890);

        verify(this.productRepository).findById(product.getId());
        verify(this.productRepository).save(product);
    }

    @DisplayName("JUnit test for updateProduct - negative Scenario")
    @Test
    void givenProductUpdateProductShouldThrowException() {

        when(productRepository.findById(any(Integer.class))).thenThrow(ResourceNotFoundException.class);

        assertThrows(ResourceNotFoundException.class, () ->{
            productService.updateProduct(product);
        });

        verify(this.productRepository).findById(any(Integer.class));
        verify(this.productRepository, never()).save(product);
    }

    @DisplayName("JUnit test for removeProduct method")
    @Test
    void removeProductShouldDeleteProduct() {

        this.productService.removeProduct(1);
        verify(this.productRepository).deleteById(1);
    }

    private Product buildTestingProduct() {
        Product product = new Product();
        product.setId(1);
        product.setName("PRODUCT_NAME");
        product.setPrice(1000);
        return product;
    }
}
