package com.zufar.product;

import com.zufar.service.DaoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.Collection;

@RestController
@RequestMapping(value = "products", produces = {MediaType.APPLICATION_JSON_VALUE})
public class ProductController {

    private final DaoService<ProductDTO> productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public @ResponseBody
    Collection<ProductDTO> getProducts() {
        return this.productService.getAll();
    }

    @GetMapping(value = "/{id}")
    public @ResponseBody
    ProductDTO getProduct(@PathVariable Long id) {
        return this.productService.getById(id);
    }

    @DeleteMapping(value = "/{id}")
    public void deleteProduct(@PathVariable Long id) {
        this.productService.deleteById(id);
    }

    @PostMapping
    public @ResponseBody
    ProductDTO saveProduct(@RequestBody ProductDTO product) {
        return this.productService.save(product);
    }

    @PutMapping
    public @ResponseBody
    ProductDTO updateProduct(@RequestBody ProductDTO product) {
        return this.productService.update(product);
    }
}
