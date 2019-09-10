package com.zufar.repository;

import com.zufar.model.Order;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface OrderPagingAndSortingRepository extends PagingAndSortingRepository<Order, Long> {
}
