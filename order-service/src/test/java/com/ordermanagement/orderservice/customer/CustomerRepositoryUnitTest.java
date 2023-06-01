package com.ordermanagement.orderservice.customer;

import com.ordermanagement.orderservice.customer.entity.Customer;
import com.ordermanagement.orderservice.customer.repository.CustomerRepository;
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
public class CustomerRepositoryUnitTest {

    @Autowired
    private CustomerRepository customerRepository;

    @DisplayName("JUnit test for fetch all Customer method")
    @Test
    void findAllShouldReturnCustomerList() {

        List<Customer> customers = this.customerRepository.findAll();
        assertFalse(customers.isEmpty());
    }

    @DisplayName("JUnit test for fetch by id method")
    @Test
    void findByIdShouldReturnProduct() {

        Optional<Customer> customer = this.customerRepository.findById(1);
        assertTrue(customer.isPresent());
    }

    @DisplayName("JUnit test for findByCity method")
    @Test
    void findByCityShouldReturnListOfCustomerBelongsToCity() {

        List<Customer> customers = this.customerRepository.findByCity("Varanasi");
        assertFalse(customers.isEmpty());
    }

    @DisplayName("JUnit test for save customer method")
    @Test
    void saveShouldInsertNewProduct() {

        Customer newCustomer = new Customer();
        newCustomer.setName("NEW_PRODUCT");
        newCustomer.setCity("NEW CITY");
        newCustomer.setEmailId("newEmail@mail.com");

        Customer persistedProduct = this.customerRepository.save(newCustomer);

        assertNotNull(persistedProduct);
    }

    @DisplayName("JUnit test for update product method")
    @Test
    void saveShouldUpdateExistingProduct() {

        Customer existingCustomer = new Customer();
        existingCustomer.setId(3);
        existingCustomer.setName("EXISTING_PRODUCT");
        existingCustomer.setCity("DELHI");

        Customer updatedCustomer = this.customerRepository.save(existingCustomer);

        assertNotNull(updatedCustomer);
        assertEquals("EXISTING_PRODUCT", updatedCustomer.getName());
        assertEquals("DELHI", updatedCustomer.getCity());
    }

    @DisplayName("JUnit test for deleteById method")
    @Test
    void deleteByIdShouldDeleteProduct() {

        this.customerRepository.deleteById(2);
        Optional<Customer> product = this.customerRepository.findById(2);
        assertFalse(product.isPresent());
    }
}
