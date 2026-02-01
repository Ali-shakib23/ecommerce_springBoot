package com.example.demo.controller;

import com.example.demo.model.Product;
import com.example.demo.payload.ProductDTO;
import com.example.demo.payload.ProductResponse;
import com.example.demo.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/api")
public class ProductController {

    @Autowired
    IProductService productService;

    public ProductController(IProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/admin/categories/{categoryId}/product")
    public ResponseEntity<ProductDTO> addProduct(@PathVariable String categoryId,  @RequestBody Product product){
        ProductDTO addedProduct = productService.addProduct(categoryId, product);
        return new ResponseEntity<>(addedProduct , HttpStatus.CREATED);
    }

    @GetMapping("/public/products")
    public ResponseEntity<ProductResponse> getAllProducts(){
        ProductResponse productResponse = productService.getAllProducts();
        return new ResponseEntity<>(productResponse , HttpStatus.OK);
    }

    @GetMapping("/public/categories/{categoryId}/products")
    public ResponseEntity<ProductResponse> getProductsByCategory(@PathVariable String categoryId){
        ProductResponse productResponse = productService.searchByCategory(categoryId);
        return new ResponseEntity<>(productResponse , HttpStatus.OK);
    }

    @GetMapping("/public/products/keyword/{keyword}")
    public ResponseEntity<ProductResponse> getProductsByKeyword(@PathVariable String keyword){
        ProductResponse productResponse =  productService.searchProductByKeyword(keyword);
        return new ResponseEntity<>(productResponse , HttpStatus.OK);
    }

    //to test this
    @PutMapping("admin/products/productId")
    public ResponseEntity<ProductDTO> updateProduct(Product product , @PathVariable Long productId){
        ProductDTO productDTO = productService.updateProduct(product , productId);
        return new ResponseEntity<>(productDTO , HttpStatus.OK);
    }
}
