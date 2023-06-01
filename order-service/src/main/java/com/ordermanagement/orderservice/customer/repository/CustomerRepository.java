package com.ordermanagement.orderservice.customer.repository;

import com.ordermanagement.orderservice.customer.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    List<Customer> findByCity(String  city);
}
