package org.example.repository;

import org.example.models.Category;

import java.util.List;

public interface CategoryRepository extends CrudRepository<Category> {
    List<Category> findAll();
}
