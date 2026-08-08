package com.mindspark.backend.controller;

import com.mindspark.backend.dto.CategoryDto;
import com.mindspark.backend.exception.CategoryNotFoundException;
import com.mindspark.backend.service.CategoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.json.JsonMapper;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CategoryController.class)
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JsonMapper jsonMapper;

    @MockitoBean
    private CategoryService categoryService;

    @Test
    void getAllCategories_shouldReturnCategoryList() throws Exception {
        CategoryDto category1 = new CategoryDto(1L, "Bilim", "🔬");
        CategoryDto category2 = new CategoryDto(2L, "Tarih", "🏛️");

        when(categoryService.getAllCategories()).thenReturn(List.of(category1, category2));

        mockMvc.perform(get("/api/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].categoryName").value("Bilim"))
                .andExpect(jsonPath("$[1].categoryName").value("Tarih"));
    }

    @Test
    void createCategory_shouldReturnCreatedCategory_whenValidInput() throws Exception {
        CategoryDto request = new CategoryDto(null, "Bilim", "🔬");
        CategoryDto response = new CategoryDto(1L, "Bilim", "🔬");

        when(categoryService.createCategory(any(CategoryDto.class))).thenReturn(response);

        mockMvc.perform(post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.categoryName").value("Bilim"));
    }

    @Test
    void createCategory_shouldReturnBadRequest_whenNameIsBlank() throws Exception {
        CategoryDto invalidRequest = new CategoryDto(null, "", "🔬");

        mockMvc.perform(post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateCategory_shouldReturnUpdatedCategory() throws Exception {
        CategoryDto request = new CategoryDto(null, "Bilim ve Teknoloji", "🔬");
        CategoryDto response = new CategoryDto(1L, "Bilim ve Teknoloji", "🔬");

        when(categoryService.updateCategory(eq(1L), any(CategoryDto.class))).thenReturn(response);

        mockMvc.perform(put("/api/categories/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.categoryName").value("Bilim ve Teknoloji"));
    }

    @Test
    void updateCategory_shouldReturnNotFound_whenCategoryDoesNotExist() throws Exception {
        CategoryDto request = new CategoryDto(null, "Bilim", "🔬");

        when(categoryService.updateCategory(eq(99L), any(CategoryDto.class)))
                .thenThrow(new CategoryNotFoundException("Category not found with id: 99"));

        mockMvc.perform(put("/api/categories/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteCategory_shouldReturnOk() throws Exception {
        doNothing().when(categoryService).deleteCategory(1L);

        mockMvc.perform(delete("/api/categories/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Category deleted successfully"));
    }

    @Test
    void deleteCategory_shouldReturnNotFound_whenCategoryDoesNotExist() throws Exception {
        doThrow(new CategoryNotFoundException("Category not found with id: 99"))
                .when(categoryService).deleteCategory(99L);

        mockMvc.perform(delete("/api/categories/99"))
                .andExpect(status().isNotFound());
    }
}