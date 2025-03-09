package com.devsuperior.dscatalog.tests;

import com.devsuperior.dscatalog.DTO.ProductDTO;
import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.entities.Product;

import java.time.Instant;

public class Factory {

    public static Product createProduct(){

        Product product = new Product(1L, "Phone", "Good Phone", 800.0, "https://igm.com/img.png");
        product.getCategories().add(new Category(2L, "Eletronics"));
        return product;
    }

    public static ProductDTO createProductDTO(){
        return new ProductDTO(createProduct());
    }
}
