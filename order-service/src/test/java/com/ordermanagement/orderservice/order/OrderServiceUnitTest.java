package com.ordermanagement.orderservice.order;

import com.ordermanagement.orderservice.customer.entity.Customer;
import com.ordermanagement.orderservice.customer.repository.CustomerRepository;
import com.ordermanagement.orderservice.exception.ResourceNotFoundException;
import com.ordermanagement.orderservice.orders.entity.Order;
import com.ordermanagement.orderservice.orders.repository.OrderRepository;
import com.ordermanagement.orderservice.orders.service.OrderServiceImpl;
import com.ordermanagement.orderservice.rest_consumer.InventoryRestConsumer;
import com.ordermanagement.orderservice.rest_consumer.ProductInventory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceUnitTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private InventoryRestConsumer inventoryRestConsumer;

    @InjectMocks
    private OrderServiceImpl orderService;

    private Order order;
    private Customer customer;

    @BeforeEach
    public void setup(){
        customer = Customer.builder()
                .id(2)
                .name("Customer_Name")
                .emailId("dummy@mal.com")
                .city("Delhi")
                .build();
        order = Order.builder()
                .id(1)
                .productId(4)
                .totalPrice(50000)
                .customer(customer)
                .build();
    }

    @DisplayName("JUnit test for getOrdersByCustomerId method")
    @Test
    void givenCustomerIdWhenGetOrdersByCustomerIdReturnCustomerList(){

        Integer customerId = customer.getId();
        when(orderRepository.findByCustomerId(any(Integer.class))).thenReturn(List.of(order));
        List<Order> orders = orderService.getOrdersByCustomerId(customerId);

        assertThat(orders.size()).isEqualTo(1);
        verify(this.orderRepository).findByCustomerId(customerId);
    }

    @DisplayName("Junit test for placeOrder - positive scenario")
    @Test
    void givenCustomerIdAndOrderWhenPlaceOrderReturnPlacedOrder(){

        Integer customerId = customer.getId();
        ProductInventory productInventory = new ProductInventory();
        productInventory.setProductId(4);
        productInventory.setProductName("PRODUCT_NAME");
        productInventory.setNoOfUnits(5);

        ResponseEntity<ProductInventory> inventoryResponseEntity =
                new ResponseEntity<>(productInventory, HttpStatus.OK);
        given(inventoryRestConsumer.getProductQuantityFromInventory(any(Integer.class)))
                .willReturn(5);
        given(inventoryRestConsumer.getProductInventory(any(Integer.class)))
                .willReturn(productInventory);
        given(customerRepository.findById(any(Integer.class)))
                .willReturn(Optional.ofNullable(customer));
        given(inventoryRestConsumer.updateProductQuantity(any(ProductInventory.class), any(Integer.class)))
                .willReturn(inventoryResponseEntity);
        given(orderRepository.save(any(Order.class))).willReturn(order);

        Order placedOrder = orderService.placeOrder(customerId, order);

        assertThat(placedOrder.getProductId()).isEqualTo(order.getProductId());
        assertThat(placedOrder.getTotalPrice()).isEqualTo(order.getTotalPrice());
        verify(this.inventoryRestConsumer).getProductInventory(order.getProductId());
        verify(this.inventoryRestConsumer).getProductQuantityFromInventory(order.getProductId());
        verify(this.customerRepository).findById(customerId);
        verify(this.inventoryRestConsumer).updateProductQuantity(productInventory, 4);
        verify(this.orderRepository).save(order);

    }
    @DisplayName("Junit test for placeOrder - negative scenario(product doesn't exist in inventory or quantity = 0")
    @Test
    void givenCustomerIdAndOrderWhenPlaceOrderReturnProductNotFound(){

        Integer customerId = customer.getId();
        given(inventoryRestConsumer.getProductQuantityFromInventory(any(Integer.class)))
                .willReturn(0);
        given(inventoryRestConsumer.getProductInventory(any(Integer.class)))
                .willReturn(null);

        assertThrows(ResourceNotFoundException.class, () ->{
            orderService.placeOrder(customerId, order);
        });

        verify(this.inventoryRestConsumer).getProductInventory(order.getProductId());
        verify(this.inventoryRestConsumer).getProductQuantityFromInventory(order.getProductId());
        verify(this.customerRepository, never()).findById(customerId);
        verify(this.inventoryRestConsumer, never()).updateProductQuantity(any(ProductInventory.class), any(Integer.class));
        verify(this.orderRepository, never()).save(order);

    }
    @DisplayName("Junit test for placeOrder - negative scenario(customer not present)")
    @Test
    void givenInvalidCustomerIdAndOrderWhenPlaceOrderReturnCustomerNotFound(){

        Integer customerId = customer.getId();
        ProductInventory productInventory = new ProductInventory();
        productInventory.setProductId(4);
        productInventory.setProductName("PRODUCT_NAME");
        productInventory.setNoOfUnits(5);

        given(inventoryRestConsumer.getProductQuantityFromInventory(any(Integer.class)))
                .willReturn(5);
        given(inventoryRestConsumer.getProductInventory(any(Integer.class)))
                .willReturn(productInventory);
        given(customerRepository.findById(any(Integer.class)))
                .willThrow(ResourceNotFoundException.class);

        assertThrows(ResourceNotFoundException.class, () ->{
            orderService.placeOrder(customerId, order);
        });

        verify(this.inventoryRestConsumer).getProductInventory(order.getProductId());
        verify(this.inventoryRestConsumer).getProductQuantityFromInventory(order.getProductId());
        verify(this.customerRepository).findById(customerId);
        verify(this.inventoryRestConsumer, never()).updateProductQuantity(any(ProductInventory.class), any(Integer.class));
        verify(this.orderRepository, never()).save(order);

    }

    @DisplayName("Junit test for updateOrder with valid Customer id - positive scenario")
    @Test
    void givenCustomerIdAndOrderWhenUpdateOrderReturnUpdatedOrder(){

        Integer customerId = customer.getId();
        given(customerRepository.existsById(any(Integer.class))).willReturn(true);
        order.setTotalPrice(7000);
        given(orderRepository.findById(any(Integer.class))).willReturn(Optional.ofNullable(order));
        given(orderRepository.save(any(Order.class))).willReturn(order);

        Order updatedOrder = this.orderService.updateOrder(customerId, order.getId(), order);

        assertThat(updatedOrder.getTotalPrice()).isEqualTo(7000);
        verify(this.orderRepository).findById(order.getId());
        verify(this.orderRepository).save(order);
    }

    @DisplayName("Junit test for updateOrder with invalid Customer id - negative scenario")
    @Test
    void givenCustomerIdAndInvalidOrderWhenUpdateOrderThrowException(){

        Integer customerId = customer.getId();
        given(customerRepository.existsById(any(Integer.class))).willReturn(true);
        order.setTotalPrice(7000);
        given(orderRepository.findById(any(Integer.class)))
                .willThrow(ResourceNotFoundException.class);

        assertThrows(ResourceNotFoundException.class, () ->{
            orderService.updateOrder(customerId, order.getId(), order);
        });
        verify(this.orderRepository).findById(order.getId());
        verify(this.orderRepository, never()).save(order);
    }

    @DisplayName("Junit test for updateOrder with invalid Customer id - negative scenario")
    @Test
    void givenInvalidCustomerIdAndOrderWhenUpdateOrderThrowException(){

        Integer customerId = customer.getId();
        given(customerRepository.existsById(any(Integer.class))).willReturn(false);

        assertThrows(ResourceNotFoundException.class, () ->{
            orderService.updateOrder(customerId, order.getId(), order);
        });
        verify(this.customerRepository).existsById(customerId);
        verify(this.orderRepository, never()).findById(order.getId());
        verify(this.orderRepository, never()).save(order);
    }

    @DisplayName("Junit test for remove order - positive scenario")
    @Test
    void givenCustomerIdAndOrderIdWhenRemoveOrderDeleteAndReturnOK(){

        Integer customerId = customer.getId();
        given(orderRepository.findByIdAndCustomerId(any(Integer.class), any(Integer.class)))
                .willReturn(Optional.of(order));
        ResponseEntity<?> response = this.orderService.removeOrder(customerId, order.getId());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(this.orderRepository).delete(order);
    }

    @DisplayName("Junit test for remove order - positive scenario")
    @Test
    void givenCustomerIdAndOrderIdWhenRemoveOrderThrowException(){

        Integer customerId = customer.getId();
        given(orderRepository.findByIdAndCustomerId(any(Integer.class), any(Integer.class)))
                .willThrow(ResourceNotFoundException.class);
        assertThrows(ResourceNotFoundException.class, () ->{
            orderService.removeOrder(customerId, order.getId());
        });

        verify(this.orderRepository, never()).delete(order);
    }
}
