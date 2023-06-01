package com.ordermanagement.orderservice.customer.controller;

import com.ordermanagement.orderservice.customer.entity.Customer;
import com.ordermanagement.orderservice.customer.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orderService")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @GetMapping("/customers")
    public List<Customer> getAllCustomers(){
        return customerService.getAllCustomers();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/customer")
    public Customer addCustomer(@RequestBody Customer customer){
        return customerService.addCustomer(customer);
    }

    @PutMapping("/customer/{id}")
    public Customer updateCustomer(@PathVariable Integer id, @RequestBody Customer customerRequest) {
         return customerService.updateCustomer(id, customerRequest);
    }

    @DeleteMapping("/customer/{id}")
    public ResponseEntity<?> deleteCustomer(@PathVariable Integer id){
        return customerService.removeCustomer(id);
    }
}
