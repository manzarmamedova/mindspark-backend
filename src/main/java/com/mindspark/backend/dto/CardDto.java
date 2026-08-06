package com.mindspark.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CardDto {
    private Long id;

    @NotBlank(message = "Title cannot be empty")

    private String title;

    @NotBlank(message = "Description cannot be empty")
    @Size(max = 500, message = "Description must be at most 500 characters")
    private String description;
    @Size(max = 500, message = "Fun fact must be at most 500 characters")
    private String funFact;
    private String imageUrl;
    private String sourceUrl;

    @NotNull(message = "Category id is required")
    private Long categoryId;
}
