package org.example.service;

import org.example.models.Category;
import org.example.repository.CategoryRepository;

import java.sql.SQLException;
import java.util.List;

public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public void addCategory(String name) {
        String clean = name == null ? "" : name.trim();
        if (clean.length() < 2 || clean.length() > 100) {
            throw new IllegalArgumentException("Название категории должно быть от 2 до 100 символов");
        }
        if (categoryRepository.existsByName(clean)) {
            throw new IllegalArgumentException("Такая категория уже есть");
        }
        try {
            categoryRepository.save(Category.builder().name(clean).build());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteCategory(int id) {
        if (categoryRepository.isUsedInOffers(id)) {
            throw new IllegalArgumentException("Нельзя удалить категорию, пока в ней есть объявления");
        }
        categoryRepository.deleteById(id);
    }
}