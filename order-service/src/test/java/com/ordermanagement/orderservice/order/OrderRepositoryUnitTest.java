package com.ordermanagement.orderservice.order;

import com.ordermanagement.orderservice.customer.entity.Customer;
import com.ordermanagement.orderservice.orders.entity.Order;
import com.ordermanagement.orderservice.orders.repository.OrderRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class OrderRepositoryUnitTest {

    @Autowired
    private OrderRepository orderRepository;


    @DisplayName("JUnit test for findByCustomerId method")
    @Test
    void findByCustomerIdShouldReturnOrderList() {

        List<Order> orders = this.orderRepository.findByCustomerId(1);
        assertNotNull(orders);
    }

    @DisplayName("JUnit test for findByOrderIdAndCustomerId method")
    @Test
    void findByOrderIdAndCustomerIdShouldReturnOrder() {

        Optional<Order> order = this.orderRepository.findById(1);
        assertTrue(order.isPresent());
    }

    @DisplayName("JUnit test for findById method")
    @Test
    void findByIdShouldReturnOrder() {

        Optional<Order> order = this.orderRepository.findByIdAndCustomerId(1,1);
        assertTrue(order.isPresent());
    }

    @DisplayName("JUnit test for findByCity method")
    @Test
    void ExistByIdShouldReturnTrue() {

        Boolean exists = this.orderRepository.existsById(1);
        assertTrue(exists);
    }

    @DisplayName("JUnit test for create order method")
    @Test
    void saveShouldPlaceNewOrder() {

        Order newOrder = new Order();
        newOrder.setProductId(33);
        newOrder.setTotalPrice(11111);
        newOrder.setCustomer(buildTestCustomer());

        Order persistedOrder = this.orderRepository.save(newOrder);
        assertNotNull(persistedOrder);

    }

    @DisplayName("JUnit test for update order method")
    @Test
    void saveShouldUpdateExistingProduct() {

        Order existingOrder = new Order();
        existingOrder.setId(3);
        existingOrder.setProductId(4);
        existingOrder.setTotalPrice(1000);
        existingOrder.setCustomer(buildTestCustomer());

        Order updatedOrder = this.orderRepository.save(existingOrder);

        assertNotNull(updatedOrder);
        assertEquals(existingOrder.getProductId(), updatedOrder.getProductId());
        assertEquals(existingOrder.getTotalPrice(), updatedOrder.getTotalPrice());
    }

    @DisplayName("JUnit test for deleteById method")
    @Test
    void deleteShouldDeleteProduct() {

        Order order = buildTestOrder();
        this.orderRepository.delete(order);
        Optional<Order> fetchedOrder = orderRepository.findById(order.getId());
        assertFalse(fetchedOrder.isPresent());
    }

    Order buildTestOrder(){
        Customer customer = buildTestCustomer();
        Order newOrder = new Order();
        newOrder.setCustomer(customer);
        newOrder.setId(1);
        newOrder.setTotalPrice(10000);
        newOrder.setProductId(33);
        return newOrder;
    }
    Customer buildTestCustomer(){
        Customer customer = Customer.builder()
                .id(1)
                .name("CUSTOMER_NAME")
                .city("CUSTOMER_CITY")
                .emailId("customer@mail.com")
                .build();
        return customer;
    }

}
