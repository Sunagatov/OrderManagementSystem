package com.zufar.domain.status;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatusRepository extends PagingAndSortingRepository<Status, Long> {
}
