package com.zufar.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductPagingAndSortingRepository extends PagingAndSortingRepository<Product, Long> {
}
