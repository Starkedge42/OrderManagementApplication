package com.ordermanagement.orderservice.orders.controller;

import com.ordermanagement.orderservice.exception.ServiceUnavailableException;
import com.ordermanagement.orderservice.orders.entity.Order;
import com.ordermanagement.orderservice.orders.service.OrderService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/orderService")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/user/{userId}/orders")
    public List<Order> getAllOrdersByCustomerId( @PathVariable Integer userId){
        return orderService.getOrdersByCustomerId(userId);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/user/{userId}/orders")
    @CircuitBreaker(name = "inventoryServiceBreaker", fallbackMethod = "inventoryServiceFallback")
    public Order createOrder(@PathVariable Integer userId, @RequestBody Order order) {
        return orderService.placeOrder(userId, order);
    }

    public Order inventoryServiceFallback(Integer userId, Order order, Exception ex){
        LOGGER.info("Fallback method is executed because Inventory service is down - " + ex.getMessage());
        throw new ServiceUnavailableException("Fallback method is executed because Inventory service is down");
    }

    @PutMapping("/user/{userId}/order/{orderId}")
    public Order updateOrder(@PathVariable Integer userId, @PathVariable Integer orderId, @RequestBody Order order) {
        return orderService.updateOrder(userId, orderId, order);
    }

    @GetMapping("/orders/{city}")
    public Integer getOrdersByCity(@PathVariable String city){
        return orderService.getOrderCountByCity(city);
    }

    @DeleteMapping("/user/{userId}/order/{orderId}")
    public ResponseEntity<?> deleteOrder(@PathVariable Integer userId, @PathVariable Integer orderId) {
        return orderService.removeOrder(userId, orderId);
    }
}
