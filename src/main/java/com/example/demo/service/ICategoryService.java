package com.example.demo.service;

import com.example.demo.model.Category;
import com.example.demo.payload.CategoryDTO;
import com.example.demo.payload.CategoryResponse;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface ICategoryService {
    public CategoryResponse getAllCategories(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    public CategoryDTO createCategory(CategoryDTO categoryDTO);

    public CategoryDTO deleteCategory(String id);

    public CategoryDTO updateCategory(String id , CategoryDTO category);
}
