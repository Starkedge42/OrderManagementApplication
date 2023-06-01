package com.ordermanagement.inventory.service;

import com.ordermanagement.inventory.entity.ProductInventory;

import java.util.List;

public interface ProductInventoryService {

    Integer getAvailableProductQuantity(Integer productId);
    ProductInventory addProductsInInventory(ProductInventory productInventory);
    ProductInventory updateProductQuantityOnly(Integer productId, ProductInventory productInventory);
    ProductInventory updateProductInventory(Integer productId, ProductInventory productInventory);
    List<ProductInventory> getAllProducts();
    ProductInventory getProductFromInventory(Integer productId);
}
