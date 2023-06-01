package com.ordermanagement.productservice.service;

import com.ordermanagement.productservice.entity.Product;

import java.util.List;

public interface ProductService {

    List<Product> getAllProducts();

    Product getProduct(Integer id);

    Product addProduct(Product product);

    void removeProduct(Integer id);

    Product updateProduct(Product product);
}
