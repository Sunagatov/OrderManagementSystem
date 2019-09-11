package com.zufar.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemPagingAndSortingRepository extends PagingAndSortingRepository<Item, Long> {
}
