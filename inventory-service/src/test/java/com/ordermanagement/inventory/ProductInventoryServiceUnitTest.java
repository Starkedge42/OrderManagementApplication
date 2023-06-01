package com.ordermanagement.inventory;

import com.ordermanagement.inventory.entity.ProductInventory;
import com.ordermanagement.inventory.exception.ResourceNotFoundException;
import com.ordermanagement.inventory.repository.ProductInventoryRepository;
import com.ordermanagement.inventory.service.ProductInventoryServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductInventoryServiceUnitTest {

    @Mock
    private ProductInventoryRepository inventoryRepository;

    @InjectMocks
    private ProductInventoryServiceImpl inventoryService;

    private ProductInventory productInventory;

    @BeforeEach
    public void setup(){
        productInventory = this.buildTestingProduct();
    }

    @DisplayName("JUnit test for getAvailableProductQuantity method")
    @Test
    void getAvailableProductQuantityShouldReturnProductQuantity() {

        when(inventoryRepository.findByProductId(any(Integer.class))).thenReturn(Optional.ofNullable(productInventory));
        Integer productQuantity = this.inventoryService.getAvailableProductQuantity(10);

        assertThat(productQuantity).isEqualTo(productInventory.getNoOfUnits());
        verify(this.inventoryRepository).findByProductId(10);
    }
    @DisplayName("JUnit test for getAvailableProductQuantity method - negative scenario")
    @Test
    void getAvailableProductQuantityShouldThrowException() {

        when(inventoryRepository.findByProductId(any(Integer.class))).thenThrow(ResourceNotFoundException.class);

        assertThrows(ResourceNotFoundException.class, () ->{
            inventoryService.getProductFromInventory(1);
        });
        verify(this.inventoryRepository).findByProductId(1);
    }

    @DisplayName("JUnit test for getAllProducts method, should return list of products in inventory")
    @Test
    void getAllProductsShouldReturnProductList() {

        when(inventoryRepository.findAll()).thenReturn(List.of(productInventory));
        List<ProductInventory> products = this.inventoryService.getAllProducts();

        assertEquals(1, products.size());
        verify(this.inventoryRepository).findAll();
    }
    @DisplayName("JUnit test for getAllProducts method, should return empty product list in inventory")
    @Test
    void getAllProductsShouldReturnEmptyProductList() {

        when(inventoryRepository.findAll()).thenReturn(Collections.emptyList());
        List<ProductInventory> products = this.inventoryService.getAllProducts();

        assertThat(products.size()).isEqualTo(0);
        verify(this.inventoryRepository).findAll();
    }

    @DisplayName("JUnit test for  getProductFromInventoryById method")
    @Test
    void getProductFromInventoryShouldReturnProduct() {

        when(inventoryRepository.findByProductId(any(Integer.class))).thenReturn(Optional.of(productInventory));
        ProductInventory returnedProduct = this.inventoryService.getProductFromInventory(10);

        assertEquals(productInventory.getId(), returnedProduct.getId());
        verify(this.inventoryRepository).findByProductId(10);
    }

    @DisplayName("JUnit test for getProductFromInventoryById method, should return ResourceNotFoundException")
    @Test
    void getProductFromInventoryShouldThrowException() {

        when(inventoryRepository.findByProductId(any(Integer.class))).thenThrow(ResourceNotFoundException.class);

        assertThrows(ResourceNotFoundException.class, () ->{
            inventoryService.getProductFromInventory(1);
        });
        verify(this.inventoryRepository).findByProductId(1);
    }

    @DisplayName("JUnit test for addProductsInInventory method")
    @Test
    void addProductsInInventoryShouldInsertNewProduct() {
        this.inventoryService.addProductsInInventory(productInventory);
        verify(this.inventoryRepository).save(productInventory);
    }

    @DisplayName("JUnit test for updateProductInventoryPartially method")
    @Test
    void updateProductQuantityOnlyShouldUpdateProductQuantity() {

        when(inventoryRepository.findByProductId(any(Integer.class))).thenReturn(Optional.ofNullable(productInventory));
        when(inventoryRepository.save(productInventory)).thenReturn(productInventory);

        productInventory.setNoOfUnits(89);
        ProductInventory updatedProduct = this.inventoryService.updateProductQuantityOnly(10, productInventory);

        assertThat(updatedProduct.getNoOfUnits()).isEqualTo(89);
        verify(this.inventoryRepository).findByProductId(10);
        verify(this.inventoryRepository).save(productInventory);
    }

    @DisplayName("JUnit test for updateProductQuantityOnly method- Negative Scenario")
    @Test
    void updateProductQuantityOnlyShouldThrowException() {

        when(inventoryRepository.findByProductId(any(Integer.class))).thenThrow(ResourceNotFoundException.class);

        assertThrows(ResourceNotFoundException.class, () ->{
            inventoryService.updateProductInventory(1, productInventory);
        });

        verify(this.inventoryRepository).findByProductId(any(Integer.class));
        verify(this.inventoryRepository, never()).save(productInventory);
    }

    @DisplayName("JUnit test for updateProductInventory  - Positive Scenario")
    @Test
    void updateProductInventoryShouldUpdateProductInventory() {

        when(inventoryRepository.findByProductId(10)).thenReturn(Optional.ofNullable(productInventory));
        when(inventoryRepository.save(productInventory)).thenReturn(productInventory);

        productInventory.setNoOfUnits(89);
        productInventory.setProductName("UPDATED_PRODUCT_NAME");
        productInventory.setProductId(11);
        ProductInventory updatedProduct = this.inventoryService.updateProductInventory(10, productInventory);

        assertThat(updatedProduct.getProductId()).isEqualTo(11);
        assertThat(updatedProduct.getProductName()).isEqualTo("UPDATED_PRODUCT_NAME");
        assertThat(updatedProduct.getNoOfUnits()).isEqualTo(89);

        verify(this.inventoryRepository).findByProductId(10);
        verify(this.inventoryRepository).save(productInventory);
    }

    @DisplayName("JUnit test for updateProductInventory method- Negative Scenario")
    @Test
    void updateProductInventoryShouldThrowException() {

        when(inventoryRepository.findByProductId(any(Integer.class))).thenThrow(ResourceNotFoundException.class);

        assertThrows(ResourceNotFoundException.class, () ->{
            inventoryService.updateProductInventory(1, productInventory);
        });

        verify(this.inventoryRepository).findByProductId(any(Integer.class));
        verify(this.inventoryRepository, never()).save(productInventory);
    }

    private ProductInventory buildTestingProduct() {
        ProductInventory product = new ProductInventory();
        product.setId(1);
        product.setProductId(10);
        product.setProductName("PRODUCT_NAME");
        product.setNoOfUnits(20);
        return product;
    }
}
