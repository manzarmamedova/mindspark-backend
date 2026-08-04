package com.mindspark.backend.dto;


import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CardDto {
    private Long id;
    private String title;
    private String description;
    private String funFact;
    private String imageUrl;
    private String sourceUrl;
    private Long categoryId;
}
