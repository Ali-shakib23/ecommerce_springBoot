package com.example.demo.controller;

import com.example.demo.config.AppConstants;
import com.example.demo.model.Category;
import com.example.demo.payload.CategoryDTO;
import com.example.demo.payload.CategoryResponse;
import com.example.demo.service.ICategoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class CategoryController {
    ICategoryService categoryService;

    public CategoryController(ICategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/categories")
    public ResponseEntity<CategoryResponse> getCategories(
           @RequestParam(name = "pageNumber" , defaultValue = AppConstants.PAGE_NUMBER) Integer pageNumber,
           @RequestParam(name = "pageSize" , defaultValue = AppConstants.PAGE_SIZE) Integer pageSize,
           @RequestParam(name = "sortBy" , defaultValue = AppConstants.SORT_BY) String sortBy,
           @RequestParam(name = "sortOrder" , defaultValue = AppConstants.SORT_DIR) String sortOrder
    ) {
        CategoryResponse categoryResponse = categoryService.getAllCategories(pageNumber , pageSize, sortBy, sortOrder);
        return new ResponseEntity<>(categoryResponse , HttpStatus.OK);
    }

    @PostMapping("/category/post")
    public ResponseEntity<CategoryDTO> createCategory(@Valid @RequestBody CategoryDTO categoryDTO) {
        CategoryDTO savedCategoryDTO = categoryService.createCategory(categoryDTO);
        return new ResponseEntity<>( savedCategoryDTO, HttpStatus.CREATED);
    }

    @DeleteMapping("category/delete/{id}")
    public ResponseEntity<CategoryDTO> deleteCategory(@PathVariable String id){
        CategoryDTO deletedCategory = categoryService.deleteCategory(id);
        return new ResponseEntity<>( deletedCategory, HttpStatus.OK);
    }

    @PutMapping("category/update/{id}")
    public ResponseEntity<?> updateCategory(@Valid @PathVariable String id, @RequestBody CategoryDTO categoryDTO) {
        CategoryDTO updatedCategory = categoryService.updateCategory(id, categoryDTO);
        return new ResponseEntity(updatedCategory, HttpStatus.OK);
    }
}
