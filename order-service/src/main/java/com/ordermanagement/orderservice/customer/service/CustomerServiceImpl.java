package com.ordermanagement.orderservice.customer.service;

import com.ordermanagement.orderservice.customer.entity.Customer;
import com.ordermanagement.orderservice.customer.repository.CustomerRepository;
import com.ordermanagement.orderservice.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService{

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    @Override
    public Customer addCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    @Override
    public Customer updateCustomer(Integer id, Customer customerRequest) {
        return customerRepository.findById(id).map(customer ->{
            customer.setName(customerRequest.getName());
            customer.setCity(customerRequest.getCity());
            customer.setEmailId(customerRequest.getEmailId());
            return customerRepository.save(customer);
        }).orElseThrow(() -> new ResourceNotFoundException("CustomerId " + id + " not found"));
    }

    @Override
    public ResponseEntity<?> removeCustomer(Integer id) {
        return customerRepository.findById(id).map(customer -> {
            customerRepository.delete(customer);
            return ResponseEntity.ok().build();
        }).orElseThrow(() -> new ResourceNotFoundException("CustomerId " + id + " not found"));
    }
}
