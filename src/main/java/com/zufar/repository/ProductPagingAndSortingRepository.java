package com.zufar.repository;

import com.zufar.model.Product;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductPagingAndSortingRepository extends PagingAndSortingRepository<Product, Long> {
}
