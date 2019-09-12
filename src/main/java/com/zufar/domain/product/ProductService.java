package com.zufar.domain.product;

import com.zufar.dto.ProductDTO;
import com.zufar.exception.ProductNotFoundException;
import com.zufar.service.DaoService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductService implements DaoService<ProductDTO> {

    private static final Logger LOGGER = LogManager.getLogger(ProductService.class);
    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Collection<ProductDTO> getAll() {
        return  ((Collection<Product>) this.productRepository.findAll())
                .stream()
                .map(ProductService::convertToProductDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<ProductDTO> getAll(String sortBy) {
        return  ((Collection<Product>) this.productRepository.findAll(Sort.by(sortBy)))
                .stream()
                .map(ProductService::convertToProductDTO)
                .collect(Collectors.toList());        
    }

    @Override
    public ProductDTO getById(Long id) {
        Product productEntity = this.productRepository.findById(id).orElseThrow(() -> {
            final String errorMessage = "The product with id = " + id + " not found.";
            ProductNotFoundException productNotFoundException = new ProductNotFoundException(errorMessage);
            LOGGER.error(errorMessage, productNotFoundException);
            return productNotFoundException;
        });
        return ProductService.convertToProductDTO(productEntity);
    }

    @Override
    public ProductDTO save(ProductDTO product) {
        Product productEntity = ProductService.convertToProduct(product);
        productEntity = this.productRepository.save(productEntity);
        return ProductService.convertToProductDTO(productEntity);
    }

    @Override
    public ProductDTO update(ProductDTO product) {
        this.isExists(product.getId());
        Product productEntity = ProductService.convertToProduct(product);
        productEntity = this.productRepository.save(productEntity);
        return ProductService.convertToProductDTO(productEntity);
    }

    @Override
    public void deleteById(Long id) {
        this.isExists(id);
        this.productRepository.deleteById(id);
    }

    @Override
    public Boolean isExists(Long id) {
        if (!this.productRepository.existsById(id)) {
            final String errorMessage = "The product with id = " + id + " not found.";
            ProductNotFoundException productNotFoundException = new ProductNotFoundException(errorMessage);
            LOGGER.error(errorMessage, productNotFoundException);
            throw productNotFoundException;
        }
        return true;
    }

    public static Product convertToProduct(ProductDTO product) {
        Objects.requireNonNull(product, "There is no product to convert.");
        return new Product(
                product.getId(),
                product.getName());
    }

    public static ProductDTO convertToProductDTO(Product product) {
        Objects.requireNonNull(product, "There is no product to convert.");
        return new ProductDTO(
                product.getId(),
                product.getName());
    }
}
