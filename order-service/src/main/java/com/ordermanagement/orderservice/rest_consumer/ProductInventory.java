package com.ordermanagement.orderservice.rest_consumer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

public class ProductInventory {

    private int productId;

    private String productName;

    private int noOfUnits;

}
