package com.ordermanagement.orderservice.orders.service;

import com.ordermanagement.orderservice.orders.entity.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface OrderService {
    Order placeOrder(Integer customerId, Order order);
    Order updateOrder(Integer customerId, Integer orderId, Order order);
    List<Order> getOrdersByCustomerId(Integer customerId);
    Integer getOrderCountByCity(String city);
    ResponseEntity<?> removeOrder(Integer customerId, Integer orderId);
}
