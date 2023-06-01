package com.ordermanagement.orderservice.orders.repository;

import com.ordermanagement.orderservice.orders.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    List<Order> findByCustomerId(Integer customerId);
    Optional<Order> findByIdAndCustomerId(Integer orderId, Integer customerId);

}
