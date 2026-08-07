package com.mindspark.backend.service;

import com.mindspark.backend.dto.CategoryDto;
import com.mindspark.backend.entity.Category;
import com.mindspark.backend.exception.CategoryNotFoundException;
import com.mindspark.backend.repository.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;


import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {


    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    @Test
    void createCategory_shouldCreateCategory() {

        CategoryDto dto = new CategoryDto(
                null,
                "Bilim",
                "🔬"
        );


        Category savedCategory = new Category();
        savedCategory.setId(1L);
        savedCategory.setCategoryName("Bilim");
        savedCategory.setIcon("🔬");


        when(categoryRepository.save(any(Category.class)))
                .thenReturn(savedCategory);


        CategoryDto result = categoryService.createCategory(dto);


        assertEquals("Bilim", result.getCategoryName());
        assertEquals("🔬", result.getIcon());
    }

    @Test
    void getAllCategories_shouldReturnCategoryList() {

        Category category1 = new Category();
        category1.setId(1L);
        category1.setCategoryName("Bilim");
        category1.setIcon("🔬");


        Category category2 = new Category();
        category2.setId(2L);
        category2.setCategoryName("Tarih");
        category2.setIcon("🏛️");


        when(categoryRepository.findAll())
                .thenReturn(List.of(category1, category2));


        List<CategoryDto> result = categoryService.getAllCategories();


        assertEquals(2, result.size());
        assertEquals("Bilim", result.get(0).getCategoryName());
        assertEquals("Tarih", result.get(1).getCategoryName());
    }

    @Test
    void updateCategory_shouldUpdateCategory() {

        Category existingCategory = new Category();
        existingCategory.setId(1L);
        existingCategory.setCategoryName("Bilim");
        existingCategory.setIcon("🔬");


        CategoryDto dto = new CategoryDto(
                null,
                "Yeni Bilim",
                "🧪"
        );


        when(categoryRepository.findById(1L))
                .thenReturn(Optional.of(existingCategory));


        when(categoryRepository.save(any(Category.class)))
                .thenReturn(existingCategory);


        CategoryDto result = categoryService.updateCategory(1L, dto);


        assertEquals("Yeni Bilim", result.getCategoryName());
        assertEquals("🧪", result.getIcon());
    }

    @Test
    void deleteCategory_shouldDeleteCategory() {

        Category category = new Category();
        category.setId(1L);
        category.setCategoryName("Bilim");
        category.setIcon("🔬");


        when(categoryRepository.findById(1L))
                .thenReturn(Optional.of(category));


        categoryService.deleteCategory(1L);


        verify(categoryRepository).delete(category);
    }

    @Test
    void updateCategory_shouldThrowException_whenCategoryNotFound() {

        when(categoryRepository.findById(99L))
                .thenReturn(Optional.empty());


        assertThrows(
                CategoryNotFoundException.class,
                () -> categoryService.updateCategory(
                        99L,
                        new CategoryDto(null, "Test", "🧪")
                )
        );
    }
}
