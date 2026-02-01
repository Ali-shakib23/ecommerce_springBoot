package com.example.demo.service;

import com.example.demo.exceptions.ApiException;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.model.Cart;
import com.example.demo.model.CartItem;
import com.example.demo.model.Product;
import com.example.demo.payload.CartDTO;
import com.example.demo.payload.ProductDTO;
import com.example.demo.repositories.CartItemRepository;
import com.example.demo.repositories.CartRepository;
import com.example.demo.repositories.ProductRepository;
import com.example.demo.util.AuthUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class CartServiceImpl implements ICartService{

    @Autowired
    CartRepository cartRepository;

    @Autowired
    AuthUtil authUtil;

    @Autowired
    CartItemRepository cartItemRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    ProductRepository productRepository;
    @Override
    public CartDTO addProductToCart(Long productId, Integer quantity) {
        //find existg cart or create One
        Cart cart = createCart();

        //retreieve prouct details
        Product product = productRepository.findById(productId)
                .orElseThrow( () -> new ResourceNotFoundException("Product" , "productId" , productId));
        //perform validation
        CartItem cartItem = cartItemRepository.findCartItemByProductIdAndCartId(
                cart.getCartId(),
                productId
        );

        if (cartItem != null){
            throw new ApiException("product already exist");
        }

        if (product.getQuantity() == 0){
            throw new ApiException( product.getProductName() + "is not available");
        }

        if (product.getQuantity() < quantity){
            throw new ApiException(" please, make an order of the " + product.getProductName() +
                    "less than or equal to the quantity" + product.getQuantity() + ".");
        }

        CartItem newCartItem = new CartItem();
        newCartItem.setProduct(product);
        newCartItem.setCart(cart);
        newCartItem.setDiscount(product.getDiscount());
        newCartItem.setProductPrice(product.getSpecialPrice());

        cartItemRepository.save(newCartItem);
        product.setQuantity(product.getQuantity());

        cart.setTotalPrice(cart.getTotalPrice() + (product.getSpecialPrice() * quantity));
        cartRepository.save(cart);

        CartDTO cartDTO = modelMapper.map(cart , CartDTO.class);

        List<CartItem> cartItems = cart.getCartItems();
        Stream<ProductDTO> productDTOStream = cartItems.stream().map(item -> {
            ProductDTO map = modelMapper.map(item.getProduct(), ProductDTO.class);
            map.setQuantity(item.getQuantity());
            return map;
        });
        //ceate cart item

        cartDTO.setProducts(productDTOStream.toList());
        // save cart ietem
        // return updated cart
        return cartDTO;
    }

    @Override
    public List<CartDTO> getAllCarts() {
        List<Cart> carts = cartRepository.findAll();

        if(carts.size() == 0){
            throw new ApiException("no cart exist");
        }

        List<CartDTO> cartDTOS = carts.stream().map(cart -> {
            CartDTO cartDTO = modelMapper.map(cart , CartDTO.class);
            List<ProductDTO> products = cart.getCartItems()
                    .stream()
                    .map(p -> modelMapper.map(p.getProduct() , ProductDTO.class))
                    .collect(Collectors.toList());
            cartDTO.setProducts(products);
            return cartDTO;
        }).collect(Collectors.toList());

        return cartDTOS;
    }

    Cart createCart(){
        Cart userCart = cartRepository.findCartByEmail(authUtil.loggedInEmail());

        if (userCart != null){
            return userCart;
        }
        Cart cart = new Cart();
        cart.setTotalPrice(0.00);
        cart.setUser(authUtil.loggedInUser());
        Cart newCart = cartRepository.save(cart);
        return newCart;
    }
}
