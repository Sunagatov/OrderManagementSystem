package com.zufar.repository;

import com.zufar.model.Customer;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface CustomerPagingAndSortingRepository extends PagingAndSortingRepository<Customer, Long> {
}
