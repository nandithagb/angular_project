package com.cafe.com.cafe.service;

import com.cafe.com.cafe.wrapper.ProductWrapper;
import org.springframework.http.ResponseEntity;
import java.util.List;
import java.util.Map;

public interface ProductService {

    // adds new product to a category api
    ResponseEntity<String> addNewProduct(Map<String, String> requestMap);

    // retrieves all products api
    ResponseEntity<List<ProductWrapper>> getAllProduct(String filterValue);

    // updates a product api
    ResponseEntity<String> updateProduct(Map<String, String> requestMap);

    // deletes a product api
    ResponseEntity<String> deleteProduct(Integer id);

    // updates a product's status api
    ResponseEntity<String> updateStatus(Map<String, String> requestMap);

    // retrieves products from a specified category api
    ResponseEntity<List<ProductWrapper>> getByCategory(Integer id);

    // retrieves products by id api
    ResponseEntity<ProductWrapper> getById(Integer id);

}
