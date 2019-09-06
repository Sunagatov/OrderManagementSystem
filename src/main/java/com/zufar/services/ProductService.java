package com.zufar.services;

import com.zufar.models.Product;
import com.zufar.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProductService {

    private ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
    
    public Product saveProduct(String name) {
        Product product = new Product();
        product.setName(name);
        return this.productRepository.save(product);
    }
}
