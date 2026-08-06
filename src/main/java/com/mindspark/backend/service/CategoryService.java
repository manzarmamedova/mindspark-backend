package com.mindspark.backend.service;


import com.mindspark.backend.dto.CategoryDto;
import com.mindspark.backend.entity.Category;
import com.mindspark.backend.exception.CategoryNotFoundException;
import com.mindspark.backend.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryService {


    private CategoryRepository categoryRepository;
    public CategoryService(CategoryRepository categoryRepository){
        this.categoryRepository = categoryRepository;
    }


    public List<CategoryDto> getAllCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(category -> new CategoryDto(
                        category.getId(),
                        category.getCategoryName(),
                        category.getIcon()
                ))
                .toList();

    }

    public CategoryDto createCategory(CategoryDto dto) {
        Category category = new Category();
        category.setCategoryName(dto.getCategoryName());
        category.setIcon(dto.getIcon());

        Category saved = categoryRepository.save(category);

        CategoryDto result = new CategoryDto();
        result.setId(saved.getId());
        result.setCategoryName(saved.getCategoryName());
        result.setIcon(saved.getIcon());

        return result;
    }

    public CategoryDto updateCategory(long id, CategoryDto dto) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found with id: " + id));


        category.setCategoryName(dto.getCategoryName());
        category.setIcon(dto.getIcon());
        Category updated = categoryRepository.save(category);

        CategoryDto result = new CategoryDto();
        result.setId(updated.getId());
        result.setCategoryName(updated.getCategoryName());
        result.setIcon(updated.getIcon());
        return result;

    }

    public void deleteCategory(long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() ->
                        new CategoryNotFoundException("Category not found with id: " + id));

        categoryRepository.delete(category);
    }
}
