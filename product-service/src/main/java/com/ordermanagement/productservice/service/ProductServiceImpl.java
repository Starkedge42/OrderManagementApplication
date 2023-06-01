package com.ordermanagement.productservice.service;

import com.ordermanagement.productservice.entity.Product;
import com.ordermanagement.productservice.exception.ResourceNotFoundException;
import com.ordermanagement.productservice.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product getProduct(Integer id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No product found with id : " + id));
    }

    @Override
    public Product addProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    public void removeProduct(Integer id) {
         productRepository.deleteById(id);

    }

    @Override
    public Product updateProduct(Product product) {
        return Optional.ofNullable(getProduct(product.getId())).map(product1 -> {
            product1.setPrice(product.getPrice());
            product1.setName(product.getName());
            return productRepository.save(product1);
        }).orElseThrow(()-> new ResourceNotFoundException("Product id " + product.getId() + " not found."));
    }
}
