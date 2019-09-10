package com.zufar.repository;

import com.zufar.model.Product;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ProductPagingAndSortingRepository extends PagingAndSortingRepository<Product, Long> {
}
