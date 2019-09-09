package com.zufar.service;

import com.zufar.dto.ProductDTO;
import com.zufar.exception.ProductNotFoundException;
import com.zufar.model.Product;
import com.zufar.repository.ProductRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductService {

    private static final Logger LOGGER = LogManager.getLogger(ProductService.class);
    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Collection<ProductDTO> getAllProducts() {
        return  ((Collection<Product>) this.productRepository.findAll())
                .stream()
                .map(ProductService::convertToProductDTO)
                .collect(Collectors.toList());
    }

    public ProductDTO getProduct(Long id) {
        Product productEntity = this.productRepository.findById(id).orElseThrow(() -> {
            final String errorMessage = "The product with id = " + id + " not found.";
            ProductNotFoundException productNotFoundException = new ProductNotFoundException(errorMessage);
            LOGGER.error(errorMessage, productNotFoundException);
            return productNotFoundException;
        });
        return ProductService.convertToProductDTO(productEntity);
    }

    public ProductDTO saveProduct(ProductDTO product) {
        Product productEntity = ProductService.convertToProduct(product);
        productEntity = this.productRepository.save(productEntity);
        return ProductService.convertToProductDTO(productEntity);
    }

    public ProductDTO updateProduct(ProductDTO product) {
        this.isProductExists(product.getId());
        Product productEntity = ProductService.convertToProduct(product);
        productEntity = this.productRepository.save(productEntity);
        return ProductService.convertToProductDTO(productEntity);
    }

    public void deleteProduct(Long id) {
        this.isProductExists(id);
        this.productRepository.deleteById(id);
    }

    private void isProductExists(Long id) {
        if (!this.productRepository.existsById(id)) {
            final String errorMessage = "The product with id = " + id + " not found.";
            ProductNotFoundException productNotFoundException = new ProductNotFoundException(errorMessage);
            LOGGER.error(errorMessage, productNotFoundException);
            throw productNotFoundException;
        }
    }

    public static Product convertToProduct(ProductDTO product) {
        UtilService.isObjectNull(product, LOGGER, "There is no product to convert.");
        Product productEntity = new Product();
        productEntity.setId(product.getId());
        productEntity.setName(product.getName());
        return productEntity;
    }

    public static ProductDTO convertToProductDTO(Product product) {
        UtilService.isObjectNull(product, LOGGER, "There is no product to convert.");
        ProductDTO productEntity = new ProductDTO();
        productEntity.setId(product.getId());
        productEntity.setName(product.getName());
        return productEntity;
    }
}
