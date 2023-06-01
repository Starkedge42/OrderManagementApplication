package com.ordermanagement.orderservice.customer.service;

import com.ordermanagement.orderservice.customer.entity.Customer;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CustomerService {
    List<Customer> getAllCustomers();
    Customer addCustomer(Customer customer);
    Customer updateCustomer(Integer id, Customer customer);
    ResponseEntity<?> removeCustomer(Integer id);
}
