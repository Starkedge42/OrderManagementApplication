package com.ordermanagement.orderservice;

import com.ordermanagement.orderservice.config.MyConfig;
import com.ordermanagement.orderservice.rest_consumer.InventoryRestConsumer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.client.RestTemplate;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = {InventoryRestConsumer.class, MyConfig.class})
public class InventoryRestConsumerUnitTest {

    @InjectMocks
    private InventoryRestConsumer inventoryRestConsumer;

    @Mock
    MyConfig myConfig;

    @DisplayName("Junit test for getProductQuantityFromInventory")
    @Test
    void givenProductIdWhenGetProductQuantityFromInventoryReturnAvailableProductQuantity(){

        final String INVENTORY_SERVICE_BASE_URL = "http://localhost:8083/api/v1/productInventory/";
        String quantity = "5";
        String resourceUrl = INVENTORY_SERVICE_BASE_URL + "available-quantity/" + 1;
        when(myConfig.restTemplate()).thenReturn(new RestTemplate());
        when(myConfig.restTemplate().getForEntity(resourceUrl, String.class))
          .thenReturn(new ResponseEntity(quantity, HttpStatus.OK));

        Integer availableQuantity = inventoryRestConsumer.getProductQuantityFromInventory(1);
        assertThat(availableQuantity).isEqualTo(quantity);
    }
}
