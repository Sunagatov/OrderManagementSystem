package com.zufar.repository;

import com.zufar.model.Status;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatusPagingAndSortingRepository extends PagingAndSortingRepository<Status, Long> {
}
