package com.cafe.com.cafe.wrapper;

import lombok.Data;

@Data
public class ProductWrapper {
    private Integer id;
    private String name;
    private String description;
    private Integer price;
    private String status;
    private Integer categoryId;
    private String categoryName;

    // empty constructor
    public  ProductWrapper() {

    }

    // overloaded constructor
    public ProductWrapper(Integer id, String name, String description, Integer price, String status, Integer categoryId, String categoryName) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.status = status;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
    }

    // overloaded constructor
    public ProductWrapper(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    // overloaded constructor
    public ProductWrapper(Integer id, String name, String description, Integer price) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
    }
}
