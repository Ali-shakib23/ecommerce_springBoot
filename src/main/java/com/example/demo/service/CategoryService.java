package com.example.demo.service;

import com.example.demo.exceptions.ApiException;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.model.Category;
import com.example.demo.payload.CategoryDTO;
import com.example.demo.payload.CategoryResponse;
import com.example.demo.repositories.CategoryRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
public class CategoryService implements ICategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    public CategoryService() {

    }

    @Override
    public CategoryResponse getAllCategories(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndOrder = sortOrder
                .equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber , pageSize, sortByAndOrder);
        Page<Category> categoryPage = categoryRepository.findAll(pageDetails);

        List<Category> categories = categoryPage.getContent();
        if (categories.isEmpty()) {
            throw new ApiException("No categories are found");
        }
        List<CategoryDTO> categoryDTOs = categories.stream()
                .map(category ->modelMapper.map(category , CategoryDTO.class))
                .toList();
        //DO THE MAPPING HERE USING THE MAPPER AND THE STREAM AND TURN IT TO LIST
        // INITILAIZE THE CATEGORYREPSONSE AND SET THE CONTENT TO IT
        CategoryResponse categoryResponse = new CategoryResponse();

        categoryResponse.setContent(categoryDTOs);
        categoryResponse.setPageNumber(categoryPage.getNumber());
        categoryResponse.setPageSize(categoryPage.getSize());
        categoryResponse.setTotalElements(categoryPage.getTotalElements());
        categoryResponse.setTotalPages(categoryPage.getTotalPages());
        categoryResponse.setLastPage(categoryPage.isLast());

        // TEN RETURN THE Categor repsonse
        return categoryResponse;
    }

    @Override
    public CategoryDTO createCategory(CategoryDTO categoryDTO){
        categoryDTO.setCategoryId(UUID.randomUUID().toString());
        categoryDTO.setTimStamp(Instant.now().toString());
        Category category = modelMapper.map(categoryDTO, Category.class);
        Category categoryFromDB = categoryRepository.findByCategoryName(category.getCategoryName());
        if (categoryFromDB != null){
            throw new ApiException("Category with  " + category.getCategoryName() + "   name already exists " );
        }
        Category savedCategory = categoryRepository.save(category);
        CategoryDTO savedCategoryDTO = modelMapper.map(savedCategory , CategoryDTO.class);
        return savedCategoryDTO;
    }

    @Override
    public CategoryDTO deleteCategory(String id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow( () -> new ResourceNotFoundException("category" , "categoryId", id));
        categoryRepository.delete(category);
        return modelMapper.map(category, CategoryDTO.class);
    }

    @Override
    public CategoryDTO updateCategory(String id , CategoryDTO categoryDTO){
//        Category savedCategory = categoryRepository.findById(id)
//                .orElseThrow( () -> new ResourceNotFoundException("category" , "categoryId", id));
//        savedCategory = modelMapper.map(categoryDTO, Category.class);
//        savedCategory.setTimStamp(Instant.now().toString());
//
//        Category newCategory = categoryRepository.save( savedCategory );
//        CategoryDTO updatedCategoryDTO = modelMapper.map(newCategory, CategoryDTO.class);
//        return updatedCategoryDTO;

        Category savedCategory = categoryRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("category", "categoryId", id)
                );
        // Map fields from DTO into the existing entity
        Category category = modelMapper.map(categoryDTO, Category.class);
        category.setCategoryId(id);
        category.setTimStamp(Instant.now().toString());
        savedCategory = categoryRepository.save(category);
        return modelMapper.map( savedCategory, CategoryDTO.class);
    }

}
