package org.nahap.strore.application.repository;

import java.util.List;
import java.util.Optional;

public interface Repository<T> {
    T save(T entity);

    Optional<T> findById(int id);

    List<T> findAll();

    void deleteById(int id);
}