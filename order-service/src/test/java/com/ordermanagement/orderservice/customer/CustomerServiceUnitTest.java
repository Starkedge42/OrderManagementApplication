package com.ordermanagement.orderservice.customer;


import com.ordermanagement.orderservice.customer.entity.Customer;
import com.ordermanagement.orderservice.customer.repository.CustomerRepository;
import com.ordermanagement.orderservice.customer.service.CustomerServiceImpl;
import com.ordermanagement.orderservice.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceUnitTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerServiceImpl customerService;

    private Customer customer;

    @BeforeEach
    public void setup(){
        customer = Customer.builder()
                .id(1)
                .name("Customer_Name")
                .emailId("dummy@mal.com")
                .city("Delhi")
                .build();
    }



    @DisplayName("JUnit test for getAllCustomers method, should return list of customers")
    @Test
    void getAllCustomersShouldReturnCustomerList() {

        when(customerRepository.findAll()).thenReturn(List.of(customer));
        List<Customer> customers = this.customerService.getAllCustomers();

        assertEquals(1, customers.size());
        verify(this.customerRepository).findAll();
    }
    @DisplayName("JUnit test for getAllCustomers method, should return empty customer list")
    @Test
    void getAllCustomersShouldReturnEmptyCustomerList() {

        when(customerRepository.findAll()).thenReturn(Collections.emptyList());
        List<Customer> products = this.customerService.getAllCustomers();

        assertThat(products.size()).isEqualTo(0);
        verify(this.customerRepository).findAll();
    }

    @DisplayName("JUnit test for addCustomer method")
    @Test
    void addCustomerInInventoryShouldReturnSavedCustomer() {
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);
        Customer savedCustomer  = this.customerService.addCustomer(customer);
        assertThat(customer.getName()).isEqualTo(savedCustomer.getName());
        verify(this.customerRepository).save(customer);
    }

    @DisplayName("JUnit test for updateCustomer  - Positive Scenario")
    @Test
    void updateCustomerShouldReturnUpdatedCustomer() {

        when(customerRepository.findById(any(Integer.class))).thenReturn(Optional.ofNullable(customer));
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        customer.setName("UPDATED_CUSTOMER_NAME");
        customer.setEmailId("updatedmail@mail.com");
        customer.setCity("UPDATED_CITY");
        Customer updatedCustomer = this.customerService.updateCustomer(10, customer);

        assertThat(updatedCustomer.getName()).isEqualTo("UPDATED_CUSTOMER_NAME");
        assertThat(updatedCustomer.getEmailId()).isEqualTo("updatedmail@mail.com");
        assertThat(updatedCustomer.getCity()).isEqualTo("UPDATED_CITY");

        verify(this.customerRepository).findById(10);
        verify(this.customerRepository).save(customer);
    }

    @DisplayName("JUnit test for updateProductInventory method- Negative Scenario")
    @Test
    void updateCustomerShouldReturnCustomerNotFound() {

        when(customerRepository.findById(any(Integer.class))).thenThrow(ResourceNotFoundException.class);

        assertThrows(ResourceNotFoundException.class, () ->{
            customerService.updateCustomer(1, customer);
        });

        verify(this.customerRepository).findById(1);
        verify(this.customerRepository, never()).save(customer);
    }

    @DisplayName("JUnit test for removeCustomer method- positive")
    @Test
    void givenCustomerIdRemoveCustomerShouldDeleteCustomerAndReturnOk() {
        when(customerRepository.findById(any(Integer.class))).thenReturn(Optional.of(customer));
        ResponseEntity<?> response = this.customerService.removeCustomer(1);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(this.customerRepository).delete(customer);
    }

    @DisplayName("JUnit test for removeCustomer method - negative scenario")
    @Test
    void givenInvalidCustomerIdRemoveCustomerShouldReturnCustomerNotFound() {
        when(customerRepository.findById(any(Integer.class))).thenThrow(ResourceNotFoundException.class);

        assertThrows(ResourceNotFoundException.class, () ->{
            customerService.removeCustomer(1);
        });
        verify(this.customerRepository, never()).delete(customer);
    }
}
