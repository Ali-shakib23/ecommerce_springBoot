package com.example.demo.service;

import com.example.demo.payload.CartDTO;

import java.util.List;

public interface ICartService {
    CartDTO addProductToCart(Long productId , Integer quantity);

    List<CartDTO> getAllCarts();
}
