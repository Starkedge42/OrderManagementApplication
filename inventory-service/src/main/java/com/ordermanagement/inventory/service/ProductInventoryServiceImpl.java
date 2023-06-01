package com.ordermanagement.inventory.service;

import com.ordermanagement.inventory.entity.ProductInventory;
import com.ordermanagement.inventory.exception.ResourceNotFoundException;
import com.ordermanagement.inventory.repository.ProductInventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductInventoryServiceImpl implements ProductInventoryService{

    @Autowired
    private ProductInventoryRepository productInventoryRepository;

    @Override
    public Integer getAvailableProductQuantity(Integer productId) {
        return productInventoryRepository.findByProductId(productId)
                .map(productInventory -> productInventory.getNoOfUnits())
                .orElseThrow(() -> new ResourceNotFoundException("No product found with product id " + productId));
    }

    @Override
    public List<ProductInventory> getAllProducts() {
        return productInventoryRepository.findAll();
    }

    @Override
    public ProductInventory getProductFromInventory(Integer productId) {
        return productInventoryRepository.findByProductId(productId).orElseThrow(() -> new ResourceNotFoundException("No product found with product id " + productId));
    }

    @Override
    public ProductInventory addProductsInInventory(ProductInventory productInventory){
        productInventoryRepository.save(productInventory);
        return productInventory;
    }

    @Override
    public ProductInventory updateProductQuantityOnly(Integer productId, ProductInventory productInventory) {
        return productInventoryRepository.findByProductId(productId)
                .map(productInventory1 -> {
                    productInventory1.setNoOfUnits(productInventory.getNoOfUnits());
                    return productInventoryRepository.save(productInventory1);
                }).orElseThrow(() -> new ResourceNotFoundException("Product id " + productId + " not found"));
    }

    @Override
    public ProductInventory updateProductInventory(Integer productId, ProductInventory productInventory) {
        return productInventoryRepository.findByProductId(productId)
                .map(productInventory1 -> {
                    productInventory1.setNoOfUnits(productInventory.getNoOfUnits());
                    productInventory1.setProductName(productInventory.getProductName());
                    return productInventoryRepository.save(productInventory1);
                }).orElseThrow(() -> new ResourceNotFoundException("Product id " + productId + " not found"));
    }



}
