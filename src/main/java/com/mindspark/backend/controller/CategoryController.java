package com.mindspark.backend.controller;

import com.mindspark.backend.dto.CategoryDto;
import com.mindspark.backend.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public List<CategoryDto> getAllCategories() {
        return categoryService.getAllCategories();
    }

    @PostMapping
    public CategoryDto createCategory(@Valid @RequestBody CategoryDto dto) {
        return categoryService.createCategory(dto);
    }

    @PutMapping("/{id}")
    public CategoryDto updateCategory(@PathVariable Long id,@Valid @RequestBody CategoryDto dto) {
        return categoryService.updateCategory(id, dto);
    }

    @DeleteMapping("/{id}")
    public void deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
    }
}
