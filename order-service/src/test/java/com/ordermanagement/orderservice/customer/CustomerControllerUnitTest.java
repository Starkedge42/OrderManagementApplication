package com.ordermanagement.orderservice.customer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ordermanagement.orderservice.customer.controller.CustomerController;
import com.ordermanagement.orderservice.customer.entity.Customer;
import com.ordermanagement.orderservice.customer.service.CustomerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@ContextConfiguration(classes={CustomerServiceImpl.class, CustomerController.class})
public class CustomerControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerServiceImpl customerService;

    @Autowired
    private ObjectMapper objectMapper;

    private Customer customer;

    @BeforeEach
    public void setup(){

        customer = Customer.builder()
                .id(1)
                .name("CUSTOMER_NAME")
                .emailId("customer@dummy.com")
                .city("Delhi")
                .build();
    }

    @DisplayName("JUnit test for Get All Customers")
    @Test
    public void givenListOfCustomerWhenGetAllCustomersThenReturnCustomerList() throws Exception{

        List<Customer> customers = new ArrayList<>();
        customers.add(Customer.builder().id(11).name("PRODUCT_11").emailId("customer1@mail.com").city("Delhi").build());
        customers.add(Customer.builder().id(12).name("PRODUCT_12").emailId("customer1@mail.com").city("Mumbai").build());

        given(this.customerService.getAllCustomers()).willReturn(customers);

        ResultActions response = mockMvc.perform(get("/api/v1/orderService/customers"));

        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()", is(customers.size())));

    }

    @DisplayName("JUnit test for addCustomer")
    @Test
    public void addGivenCustomerInInventoryThenReturnSavedCustomer() throws Exception{

        given(customerService.addCustomer(any(Customer.class)))
                .willAnswer((invocation)-> invocation.getArgument(0));

        ResultActions response = mockMvc.perform(post("/api/v1/orderService/customer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customer)));

        response.andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(customer.getId()))
                .andExpect(jsonPath("$.name").value(customer.getName()))
                .andExpect(jsonPath("$.emailId").value(customer.getEmailId()))
                .andExpect(jsonPath("$.city").value(customer.getCity()));

    }


    @DisplayName("JUnit test for update product quantity only")
    @Test
    public void givenCustomerWhenUpdateCustomerThenUpdateCustomerAndReturnUpdatedCustomer() throws Exception {

        Customer updatedCustomer = Customer.builder()
                .id(customer.getId())
                .name("UPDATED_CUSTOMER_NAME")
                .emailId("updatedemail@mail.com")
                .city("UPDATED_CITY")
                .build();

        given(customerService.updateCustomer(any(Integer.class), any(Customer.class)))
                .willAnswer((invocation) -> invocation.getArgument(1));

        ResultActions response = mockMvc.perform(put("/api/v1/orderService/customer/{id}", customer.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedCustomer)));

        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.name").value(updatedCustomer.getName()))
                .andExpect(jsonPath("$.emailId").value(updatedCustomer.getEmailId()))
                .andExpect(jsonPath("$.city").value(updatedCustomer.getCity()));
    }

    @DisplayName("Junit test for deleteCustomerById")
    @Test
    void givenCustomerIdDeleteCustomerAndReturnResponseEntity() throws Exception{

        given(customerService.removeCustomer(any(Integer.class))).willReturn(ResponseEntity.ok().build());

        ResultActions response = mockMvc.perform(delete("/api/v1/orderService/customer/{id}", customer.getId()));

        response.andExpect(status().isOk())
                .andDo(print());
    }
}
