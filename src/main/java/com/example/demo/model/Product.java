package com.example.demo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Generated;
import lombok.NoArgsConstructor;
import org.springframework.boot.autoconfigure.jackson.JacksonProperties;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity(name = "products")
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    //add other properties
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long productId;

    @NotBlank
    @Size(min = 3, message = "Product name must contain atleast 3 characters")
    public String productName;

    @NotBlank
    @Size(min = 3, message = "Product name must contain atleast 3 characters")
    private String description;
    private Integer quantity;
    private double price;
    private double discount;
    private double specialPrice;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "seller_id")
    private User user ;

    @OneToMany(mappedBy = "product" , cascade = {CascadeType.PERSIST , CascadeType.MERGE} , fetch = FetchType.EAGER )
    private List<CartItem> products = new ArrayList<>();

}
