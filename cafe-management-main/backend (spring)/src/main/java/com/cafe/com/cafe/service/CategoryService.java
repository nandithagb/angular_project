package com.cafe.com.cafe.service;

import com.cafe.com.cafe.modal.Category;
import org.springframework.http.ResponseEntity;
import java.util.List;
import java.util.Map;

public interface CategoryService {
    // adds new category to the category api
    ResponseEntity<String> addNewCategory(Map<String, String> requestMap);

    // retrieves all categories api
    ResponseEntity<List<Category>> getAllCategory(String filterValue);

    // updates category api
    ResponseEntity<String> updateCategory(Map<String, String> requestMap);
}
