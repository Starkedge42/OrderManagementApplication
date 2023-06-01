package com.ordermanagement.inventory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ordermanagement.inventory.entity.ProductInventory;
import com.ordermanagement.inventory.exception.BadJsonRequestException;
import com.ordermanagement.inventory.exception.ResourceNotFoundException;
import com.ordermanagement.inventory.service.ProductInventoryServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
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
public class ProductInventoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductInventoryServiceImpl inventoryService;

    @Autowired
    private ObjectMapper objectMapper;

    private ProductInventory product;

    @BeforeEach
    public void setup(){

        product = ProductInventory.builder()
                .id(1)
                .productName("PRODUCT_NAME")
                .productId(10)
                .noOfUnits(50)
                .build();
    }

    @DisplayName("JUnit test for getAvailableQuantityOfProduct endpoint - positive scenario")
    @Test
    public void givenProductIdWhenAvailableThenReturnAvailableQuantityOfProduct() throws Exception{

        given(inventoryService.getAvailableProductQuantity(any(Integer.class))).willReturn(product.getNoOfUnits());

        ResultActions response = mockMvc.perform(get("/api/v1/productInventory/available-quantity/{productId}"
                , product.getProductId()));

        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(result -> result.equals(product.getNoOfUnits()));

    }

    @DisplayName("JUnit test for getAvailableQuantityOfProduct endpoint - negative Scenario")
    @Test
    public void givenProductIdWhenNotAvailableThenReturnProductNotFound() throws Exception{

        given(inventoryService.getAvailableProductQuantity(any(Integer.class))).willThrow(ResourceNotFoundException.class);

        ResultActions response = mockMvc.perform(get("/api/v1/productInventory/available-quantity/{productId}",
                product.getProductId()));

        response.andExpect(status().isNotFound())
                .andDo(print());

    }

    @DisplayName("JUnit test for getProductById - positive scenario")
    @Test
    public void givenProductIdWhenAvailableThenReturnProduct() throws Exception{

        given(inventoryService.getProductFromInventory(any(Integer.class))).willReturn(product);

        ResultActions response = mockMvc.perform(get("/api/v1/productInventory/product/{productId}"
                , product.getProductId()));

        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.productName", is("PRODUCT_NAME")))
                .andExpect(jsonPath("$.productId", is(10)))
                .andExpect(jsonPath("$.noOfUnits",
                        is(50)));

    }
    @DisplayName("JUnit test for getProductByID - negative scenario")
    @Test
    public void givenProductIdWhenUnavailableThenReturnProductNotFound() throws Exception{

        given(inventoryService.getProductFromInventory(any(Integer.class))).willThrow(ResourceNotFoundException.class);

        ResultActions response = mockMvc.perform(get("/api/v1/productInventory/product/{productId}"
                , product.getProductId()));

        response.andExpect(status().isNotFound())
                .andDo(print());

    }

    @DisplayName("JUnit test for Get All Products")
    @Test
    public void givenListOfProductWhenGetAllProductsThenReturnProductList() throws Exception{

        List<ProductInventory> products = new ArrayList<>();
        products.add(ProductInventory.builder().productId(11).productName("PRODUCT_11").noOfUnits(100).build());
        products.add(ProductInventory.builder().productId(12).productName("PRODUCT_12").noOfUnits(100).build());

        given(inventoryService.getAllProducts()).willReturn(products);

        ResultActions response = mockMvc.perform(get("/api/v1/productInventory/products"));

        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()", is(products.size())));

    }

    @DisplayName("JUnit test for addProductInInventory - positive scenario")
    @Test
    public void addGivenProductInInventoryThenReturnSavedProduct() throws Exception{

        given(inventoryService.addProductsInInventory(any(ProductInventory.class)))
                .willAnswer((invocation)-> invocation.getArgument(0));

        ResultActions response = mockMvc.perform(post("/api/v1/productInventory/add-product")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(product)));

        response.andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(product.getId()))
                .andExpect(jsonPath("$.productId").value(product.getProductId()))
                .andExpect(jsonPath("$.productName").value(product.getProductName()))
                .andExpect(jsonPath("$.noOfUnits").value(product.getNoOfUnits()));

    }

    @DisplayName("JUnit test for addProductInInventory - negative scenario")
    @Test
    public void addGivenProductInInventoryThenThrowException() throws Exception{

        ProductInventory badProduct = ProductInventory.builder()
                .productName("NEW_PRODUCT_NAME")
                .noOfUnits(100)
                .build();
        given(inventoryService.addProductsInInventory(any(ProductInventory.class)))
                .willThrow(BadJsonRequestException.class);

        ResultActions response = mockMvc.perform(post("/api/v1/productInventory/add-product")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(badProduct)));

        response.andExpect(status().isBadRequest())
                .andDo(print());

    }

    @DisplayName("JUnit test for update product quantity only")
    @Test
    public void givenProductWhenUpdateProductPartiallyThenUpdateProductQuantityOnlyAndReturnProduct() throws Exception {

        ProductInventory updatedProduct = ProductInventory.builder()
                .id(product.getId())
                .productId(product.getProductId())
                .productName(product.getProductName())
                .noOfUnits(27)
                .build();

        given(inventoryService.updateProductQuantityOnly(any(Integer.class), any(ProductInventory.class)))
                .willAnswer((invocation) -> invocation.getArgument(1));

        ResultActions response = mockMvc.perform(patch("/api/v1/productInventory/update-inventory/{productId}", product.getProductId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedProduct)));

        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.productId").value(product.getProductId()))
                .andExpect(jsonPath("$.productName").value(product.getProductName()))
                .andExpect(jsonPath("$.noOfUnits").value(updatedProduct.getNoOfUnits()));
    }

    @DisplayName("JUnit test for update product inventory")
    @Test
    public void givenProductWhenUpdateProductThenReturnUpdatedProductObject() throws Exception {

        ProductInventory updatedProduct = ProductInventory.builder()
                .productId(18)
                .productName("UPDATED_PRODUCT_NAME")
                .noOfUnits(27)
                .build();

        given(inventoryService.updateProductInventory(any(Integer.class), any(ProductInventory.class)))
                .willAnswer((invocation) -> invocation.getArgument(1));

        ResultActions response = mockMvc.perform(put("/api/v1/productInventory/update-inventory/{productId}", product.getProductId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedProduct)));

        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.productId").value(updatedProduct.getProductId()))
                .andExpect(jsonPath("$.productName").value(updatedProduct.getProductName()))
                .andExpect(jsonPath("$.noOfUnits").value(updatedProduct.getNoOfUnits()));
    }

}

