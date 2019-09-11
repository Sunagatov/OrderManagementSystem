package com.zufar.repository;

import com.zufar.domain.customer.Customer;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerPagingAndSortingRepository extends PagingAndSortingRepository<Customer, Long> {
}
