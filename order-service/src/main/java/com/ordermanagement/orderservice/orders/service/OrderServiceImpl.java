package com.ordermanagement.orderservice.orders.service;

import com.ordermanagement.orderservice.customer.entity.Customer;
import com.ordermanagement.orderservice.customer.repository.CustomerRepository;
import com.ordermanagement.orderservice.customer.service.CustomerService;
import com.ordermanagement.orderservice.exception.ResourceNotFoundException;
import com.ordermanagement.orderservice.orders.entity.Order;
import com.ordermanagement.orderservice.orders.repository.OrderRepository;
import com.ordermanagement.orderservice.rest_consumer.InventoryRestConsumer;
import com.ordermanagement.orderservice.rest_consumer.ProductInventory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private InventoryRestConsumer inventoryRestConsumer;

    @Override
    public List<Order> getOrdersByCustomerId(Integer customerId) {
        return orderRepository.findByCustomerId(customerId);
    }

    @Override
    public Order placeOrder(Integer customerId, Order order) {

        Integer productQuantity = inventoryRestConsumer.getProductQuantityFromInventory(order.getProductId());

        ProductInventory productInventory = inventoryRestConsumer.getProductInventory(order.getProductId());

        if(productQuantity == null || productQuantity < 1 ) {
            throw new ResourceNotFoundException("Either product id " + order.getProductId() + " doesn't exist or 0 quantity available in inventory.");
        }

        return customerRepository.findById(customerId).map(customer -> {
            order.setCustomer(customer);
            inventoryRestConsumer.updateProductQuantity(productInventory, productQuantity <= 1 ? 0 : productQuantity-1);
            return orderRepository.save(order);
        }).orElseThrow(() -> new ResourceNotFoundException("Customer id " + customerId + "not found"));
    }

    @Override
    public Order updateOrder(Integer customerId, Integer orderId, Order order) {

        if(!customerRepository.existsById(customerId)){
            throw new ResourceNotFoundException("Customer id " + customerId + " not found");
        }
        return orderRepository.findById(orderId).map(order1 -> {
            order1.setTotalPrice(order.getTotalPrice());
            return orderRepository.save(order1);
        }).orElseThrow(()-> new ResourceNotFoundException("Order id " + orderId + " not found."));
    }

    @Override
    public Integer getOrderCountByCity(String city) {
        List<Customer> customers = customerRepository.findByCity(city);

        List<Order> orders = new ArrayList<>();
        for(Customer customer : customers){
            orders.addAll(orderRepository.findByCustomerId(customer.getId()));
        }
        return orders.size();
    }

    @Override
    public ResponseEntity<?> removeOrder(Integer customerId, Integer orderId) {
        return orderRepository.findByIdAndCustomerId(orderId, customerId).map(order -> {
            orderRepository.delete(order);
            return ResponseEntity.ok().build();
        }).orElseThrow(() -> new ResourceNotFoundException("Order not found with id " + orderId + " and " + "customer id " + customerId));
    }
}
