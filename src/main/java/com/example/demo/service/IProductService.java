package com.example.demo.service;

import com.example.demo.model.Product;
import com.example.demo.payload.ProductDTO;
import com.example.demo.payload.ProductResponse;

public interface IProductService {
    public ProductDTO addProduct(String categoryId, Product product);

    public ProductResponse getAllProducts();

    public ProductResponse searchByCategory(String categoryId);

    public ProductResponse  searchProductByKeyword(String keyword);

    ProductDTO updateProduct(Product product, Long productId);
}
