package com.zufar.repository;

import com.zufar.model.OrderItem;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface OrderItemPagingAndSortingRepository extends PagingAndSortingRepository<OrderItem, Long> {
}
