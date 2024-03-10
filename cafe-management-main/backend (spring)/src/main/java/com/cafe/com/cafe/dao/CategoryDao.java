package com.cafe.com.cafe.dao;

import com.cafe.com.cafe.modal.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CategoryDao extends JpaRepository<Category, Integer> {
    List<Category> getAllCategory();
}
