package com.example.demo.service;

import com.example.demo.exceptions.ApiException;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.model.Category;
import com.example.demo.model.Product;
import com.example.demo.payload.ProductDTO;
import com.example.demo.payload.ProductResponse;
import com.example.demo.repositories.CategoryRepository;
import com.example.demo.repositories.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService implements IProductService {
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public ProductDTO addProduct(String categoryId, Product product) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category" , "categoryId" , categoryId));

        product.setCategory(category);
        double specialPrice = product.getPrice() - ((product.getPrice() * 0.01) * product.getPrice());
        product.setSpecialPrice(specialPrice);

        Product addedProduct = productRepository.save(product);
        return modelMapper.map(addedProduct , ProductDTO.class);
    }

    @Override
    public ProductResponse getAllProducts() {
        List<Product> products = productRepository.findAll();
        List<ProductDTO> productDTOs = products.stream().map(
                product -> modelMapper.map(product ,ProductDTO.class)).toList();

        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTOs);
        return productResponse;
    }


    //to test this
    @Override
    public ProductResponse searchByCategory(String categoryId) {
        Category category = categoryRepository.findById(categoryId)
                            .orElseThrow(() -> new ResourceNotFoundException("Category" , "categoryId" , categoryId));
        List<Product> products = productRepository.findByCategoryOrderByPriceAsc(category);
        ProductResponse productResponse = new ProductResponse();
        List<ProductDTO> productDTOS = products.stream().map(
                product -> modelMapper.map(product , ProductDTO.class))
                        .toList();
        productResponse.setContent(productDTOS);
        return productResponse;
    }

    //to test this
    @Override
    public ProductResponse searchProductByKeyword(String keyword) {
        List<Product> products = productRepository.findByProductNameLikeIgnoreCase('%'+ keyword + '%');
        List<ProductDTO> productDTOS = products.stream().map(
                product -> modelMapper.map(product , ProductDTO.class))
                .toList();

        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTOS);
        return productResponse;
    }

    @Override
    public ProductDTO updateProduct(Product product, Long productId) {
        Product productFromDB = productRepository.findById(productId)
                                .orElseThrow(() -> new ResourceNotFoundException("Product" , "productId" , productId) );

        productFromDB.setProductName(product.getProductName());
        productFromDB.setPrice(product.getPrice());
        productFromDB.setDescription(product.getDescription());
        productFromDB.setQuantity(product.getQuantity());
        productFromDB.setDiscount(product.getDiscount());
        productFromDB.setSpecialPrice(product.getSpecialPrice());

        Product updatedProduct = productRepository.save(productFromDB);
        ProductDTO productDTO = modelMapper.map(updatedProduct , ProductDTO.class);

        return productDTO;
    }

    //implement the delete

}
