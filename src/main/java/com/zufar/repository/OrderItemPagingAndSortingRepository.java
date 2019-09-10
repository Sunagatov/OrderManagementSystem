package com.zufar.repository;

import com.zufar.model.OrderItem;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemPagingAndSortingRepository extends PagingAndSortingRepository<OrderItem, Long> {
}
