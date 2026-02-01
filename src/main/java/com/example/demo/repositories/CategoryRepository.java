package com.example.demo.repositories;

import com.example.demo.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface CategoryRepository extends JpaRepository<Category, String> {
    Category findByCategoryName(String categoryName);
}
