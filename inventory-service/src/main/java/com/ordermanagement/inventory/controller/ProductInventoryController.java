package com.ordermanagement.inventory.controller;

import com.ordermanagement.inventory.entity.ProductInventory;
import com.ordermanagement.inventory.service.ProductInventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/productInventory")
public class ProductInventoryController {
    @Autowired
    private ProductInventoryService productInventoryService;

    @GetMapping("/available-quantity/{productId}")
    public Integer getProductQuantityInInventory(@PathVariable Integer productId){
        return productInventoryService.getAvailableProductQuantity(productId);
    }

    @GetMapping("/product/{productId}")
    public ProductInventory getProduct(@PathVariable Integer productId){
        return productInventoryService.getProductFromInventory(productId);
    }

    @GetMapping("/products")
    public List<ProductInventory> getAllProducts(){
        return productInventoryService.getAllProducts();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/add-product")
    public ProductInventory addProducts(@RequestBody ProductInventory productInventory){
        return productInventoryService.addProductsInInventory(productInventory);
    }

    @PatchMapping("/update-inventory/{productId}")
    public ProductInventory updateProductInventoryPartially(@PathVariable Integer productId, @RequestBody ProductInventory productInventory){
        return productInventoryService.updateProductQuantityOnly(productId, productInventory);
    }
    @PutMapping("/update-inventory/{productId}")
    public ProductInventory updateProductInventory(@PathVariable Integer productId, @RequestBody ProductInventory productInventory){
        return productInventoryService.updateProductInventory(productId, productInventory);
    }
}
