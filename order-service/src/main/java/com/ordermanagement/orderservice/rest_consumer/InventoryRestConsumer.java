package com.ordermanagement.orderservice.rest_consumer;

import com.ordermanagement.orderservice.config.MyConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class InventoryRestConsumer {

    @Autowired
    private MyConfig myConfig;
    private final String INVENTORY_SERVICE_BASE_URL = "http://inventory-service/api/v1/productInventory/";

    public Integer getProductQuantityFromInventory(Integer id) {

        String resourceUrl = INVENTORY_SERVICE_BASE_URL + "available-quantity/" + id;
        ResponseEntity<String> availableQuantity
                = myConfig.restTemplate().getForEntity(resourceUrl, String.class);
        return availableQuantity.getBody() == null ? 0 : Integer.parseInt(availableQuantity.getBody());
    }

    public  ResponseEntity<ProductInventory> updateProductQuantity(ProductInventory productInventory, Integer availableQuantity) {

        String resourceUrl =  INVENTORY_SERVICE_BASE_URL+ "update-inventory/" + productInventory.getProductId();
        productInventory.setNoOfUnits(availableQuantity);
        HttpEntity<ProductInventory> request = new HttpEntity<ProductInventory>(productInventory);

        return myConfig.restTemplate().exchange(resourceUrl, HttpMethod.PUT,
                request,ProductInventory.class, Integer.toString(productInventory.getProductId()));
    }

    public ProductInventory getProductInventory(Integer productId) {

        String resourceUrl = INVENTORY_SERVICE_BASE_URL + "product/" + productId;
        ResponseEntity<ProductInventory> productInventoryResponseEntity
                = myConfig.restTemplate().getForEntity(resourceUrl, ProductInventory.class);

        return productInventoryResponseEntity.getBody();
    }
}
