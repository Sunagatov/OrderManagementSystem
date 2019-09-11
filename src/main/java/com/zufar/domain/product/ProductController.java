package com.zufar.domain.product;

import com.zufar.dto.ProductDTO;
import com.zufar.service.DaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping(value = "products", produces = {MediaType.APPLICATION_JSON_VALUE})
public class ProductController {

    private final DaoService<ProductDTO> productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping(value = "/all")
    public @ResponseBody Collection<ProductDTO> getProducts() {
        return this.productService.getAll();
    }

    @GetMapping(value = "/{id}")
    public @ResponseBody ProductDTO getProduct(@PathVariable Long id) {
        return this.productService.getById(id);
    }

    @DeleteMapping(value = "/{id}")
    public void deleteProduct(@PathVariable Long id) {
        this.productService.deleteById(id);
    }

    @PostMapping
    public @ResponseBody ProductDTO saveProduct(@RequestBody ProductDTO product) {
        return this.productService.save(product);
    }

    @PutMapping
    public @ResponseBody ProductDTO updateProduct(@RequestBody ProductDTO product) {
        return  this.productService.update(product);
    }
}
