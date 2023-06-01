package com.ordermanagement.productservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ordermanagement.productservice.entity.Product;
import com.ordermanagement.productservice.exception.ResourceNotFoundException;
import com.ordermanagement.productservice.service.ProductService;
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
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup(){
    }

    @DisplayName("JUnit test for Get All Products")
    @Test
    public void givenListOfProductsWhenGetProductsThenReturnProductList() throws Exception{

        List<Product> products = new ArrayList<>();
        products.add(Product.builder().name("Chair").price(500).build());
        products.add(Product.builder().name("Table").price(1500).build());
        given(productService.getAllProducts()).willReturn(products);

        ResultActions response = mockMvc.perform(get("/api/v1/product/products"));

        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()",
                        is(products.size())));

    }

    @DisplayName("JUnit test for getListOfProducts, positive scenario - valid product id")
    @Test
    public void givenProductIdWhenGetProductThenReturnProductObject() throws Exception{
        Integer productId = 1;
        Product product = Product.builder()
                .id(productId)
                .name("PRODUCT_NAME")
                .price(1000).build();
        given(productService.getProduct(any(Integer.class))).willReturn(product);

        ResultActions response = mockMvc.perform(get("/api/v1/product/{id}", productId));

        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.name").value(product.getName()))
                .andExpect(jsonPath("$.price").value(product.getPrice()));

    }
    @DisplayName("JUnit test for getProductById, negative scenario - invalid product id")
    @Test
    public void givenInvalidProductIdWhenGetProductThenReturnProductNotFound() throws Exception{
        Integer productId = 1;
        when(productService.getProduct(any(Integer.class))).thenThrow(ResourceNotFoundException.class);

        ResultActions response = mockMvc.perform(get("/api/v1/product/{id}", productId));

        response.andExpect(status().isNotFound())
                .andDo(print());

    }

    @DisplayName("JUnit test for save Product endpoint")
    @Test
    public void givenProductObjectWhenCreateProductThenReturnSavedProduct() throws Exception{

        Product product = Product.builder().id(1).name("PRODUCT_NAME").price(1000).build();

        given(productService.addProduct(any(Product.class)))
                .willAnswer((invocation)-> invocation.getArgument(0));

        ResultActions response = mockMvc.perform(post("/api/v1/product/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(product)));

        response.andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(product.getId()))
                .andExpect(jsonPath("$.name").value(product.getName()))
                .andExpect(jsonPath("$.price").value(product.getPrice()));

    }

    @DisplayName("JUnit test for update product endpoint")
    @Test
    public void givenUpdatedProductWhenUpdateProductThenReturnUpdateProductObject() throws Exception{

        Integer productId = 1;
        Product savedProduct = Product.builder()
                .name("Chair")
                .price(1000)
                .build();

        Product updatedProduct = Product.builder()
                .name("Table")
                .price(1500)
                .build();

        given(productService.getProduct(any(Integer.class))).willReturn(savedProduct);
        given(productService.updateProduct(any(Product.class)))
                .willAnswer((invocation)-> invocation.getArgument(0));

        ResultActions response = mockMvc.perform(put("/api/v1/product/update/{id}", productId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedProduct)));


        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.name").value(updatedProduct.getName()))
                .andExpect(jsonPath("$.price").value(updatedProduct.getPrice()));
    }

    @DisplayName("JUnit test for delete product endpoint")
    @Test
    public void givenProductIdWhenRemoveProductThenReturn200() throws Exception{
        Integer productId = 1;
        willDoNothing().given(productService).removeProduct(productId);

        ResultActions response = mockMvc.perform(delete("/api/v1/product/{id}", productId));

        response.andExpect(status().isOk())
                .andDo(print());
    }
}

