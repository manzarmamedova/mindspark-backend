package com.mindspark.backend.service;


import com.mindspark.backend.dto.CategoryDto;
import com.mindspark.backend.entity.Category;
import com.mindspark.backend.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;
    public List<CategoryDto>getAllCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(category->new CategoryDto(
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

}
