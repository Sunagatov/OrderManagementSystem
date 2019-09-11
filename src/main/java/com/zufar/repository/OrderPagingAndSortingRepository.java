package com.zufar.repository;

import com.zufar.domain.order.Order;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderPagingAndSortingRepository extends PagingAndSortingRepository<Order, Long> {
}
