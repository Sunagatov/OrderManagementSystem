package com.zufar.service;

import java.util.Collection;

public interface DaoService<T> {
    
    Collection<T> getAll();

    Collection<T> getAll(String sortBy);

    T getById(Long id);

    T save(T entity);

    T update(T entity);

    void deleteById(Long id) ;

    Boolean isExists(Long id) ;
}
