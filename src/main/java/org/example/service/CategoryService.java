package org.example.service;

import org.example.models.Category;

import java.util.List;

public interface CategoryService {
    List<Category> getAllCategories();
    void addCategory(String name);
    void deleteCategory(int id);
}