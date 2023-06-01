package com.ordermanagement.orderservice.order;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ordermanagement.orderservice.customer.entity.Customer;
import com.ordermanagement.orderservice.orders.controller.OrderController;
import com.ordermanagement.orderservice.orders.entity.Order;
import com.ordermanagement.orderservice.orders.service.OrderServiceImpl;
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
@ContextConfiguration(classes={OrderServiceImpl.class, OrderController.class})
public class OrderControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderServiceImpl orderService;

    @Autowired
    private ObjectMapper objectMapper;

    private Order order;

    @BeforeEach
    public void setup(){

        order = Order.builder()
                .id(1)
                .totalPrice(10000)
                .productId(4)
                .customer(buildTestCustomer())
                .build();
    }

    @DisplayName("JUnit test for getAllOrdersByCustomerId")
    @Test
    public void givenCustomerIdWhenGetAllOrdersByCustomerIdThenReturnOrderList() throws Exception{

        Integer userId = order.getCustomer().getId();
        List<Order> orders = new ArrayList<>();
        orders.add(order);
        orders.add(Order.builder().id(2).productId(5).totalPrice(5000).customer(buildTestCustomer()).build());

        given(this.orderService.getOrdersByCustomerId(any(Integer.class))).willReturn(orders);

        ResultActions response = mockMvc.perform(get("/api/v1/orderService/user/{userId}/orders", userId));

        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()", is(orders.size())));

    }

    @DisplayName("JUnit test for createOrder")
    @Test
    public void givenCustomerIdAndOrderWhenCreateOrderThenReturnCreatedOrder() throws Exception{

        given(orderService.placeOrder(any(Integer.class), any(Order.class))).willReturn(order);

        ResultActions response = mockMvc.perform(post("/api/v1/orderService/user/{userId}/orders", order.getCustomer().getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(order)));

        response.andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(order.getId()))
                .andExpect(jsonPath("$.productId").value(order.getProductId()))
                .andExpect(jsonPath("$.totalPrice").value(order.getTotalPrice()));

    }


    @DisplayName("JUnit test for updateOrder")
    @Test
    public void givenCustomerIdAndOrderWhenUpdateOrderThenUpdateOrderAndReturnUpdatedOrder() throws Exception {

        Order updatedOrder = Order.builder()
                .id(order.getId())
                .productId(19)
                .totalPrice(600)
                .customer(buildTestCustomer())
                .build();

        given(orderService.updateOrder(any(Integer.class), any(Integer.class), any(Order.class)))
                .willReturn(updatedOrder);

        ResultActions response = mockMvc.perform(put("/api/v1/orderService/user/{userId}/order/{orderId}",
                order.getCustomer().getId(), order.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedOrder)));

        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.id").value(updatedOrder.getId()))
                .andExpect(jsonPath("$.totalPrice").value(updatedOrder.getTotalPrice()))
                .andExpect(jsonPath("$.productId").value(updatedOrder.getProductId()));
    }

    @DisplayName("JUnit test for getOrderCountByCity")
    @Test
    public void givenCityWhenGetOrderCountByCityThenReturnOrderCount() throws Exception{

        String city = order.getCustomer().getCity();

        given(this.orderService.getOrderCountByCity(any(String.class))).willReturn(1);

        ResultActions response = mockMvc.perform(get("/api/v1/orderService/orders/{city}", city));

        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(result -> result.equals(1));

    }

    @DisplayName("Junit test for deleteCustomerById")
    @Test
    void givenCustomerIdAndOrderIdDeleteOrderAndReturnResponseEntity() throws Exception{

        given(orderService.removeOrder(any(Integer.class), any(Integer.class))).willReturn(ResponseEntity.ok().build());

        ResultActions response = mockMvc.perform(delete("/api/v1/orderService/user/{userId}/order/{orderId}",
                order.getCustomer().getId(), order.getId()));

        response.andExpect(status().isOk())
                .andDo(print());
    }
    Customer buildTestCustomer(){
        return Customer.builder()
                .id(1)
                .name("CUSTOMER_NAME")
                .emailId("customer@mail.com")
                .city("DELHI")
                .build();
    }
}
