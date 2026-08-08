package com.mindspark.backend.controller;

import com.mindspark.backend.dto.CardDto;
import com.mindspark.backend.exception.CardNotFoundException;
import com.mindspark.backend.exception.CategoryNotFoundException;
import com.mindspark.backend.service.CardService;
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

@WebMvcTest(CardController.class)
class CardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JsonMapper jsonMapper;

    @MockitoBean
    private CardService cardService;

    @Test
    void getAllCards_shouldReturnCardList() throws Exception {
        CardDto card1 = new CardDto(1L, "Test Title One", "Test description one", "Test fun fact one", "http://example.com/image1.jpg", "http://example.com/source1", 1L);
        CardDto card2 = new CardDto(2L, "Test Title Two", "Test description two", "Test fun fact two", "http://example.com/image2.jpg", "http://example.com/source2", 1L);

        when(cardService.getAllCards()).thenReturn(List.of(card1, card2));

        mockMvc.perform(get("/api/cards"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].title").value("Test Title One"))
                .andExpect(jsonPath("$[1].title").value("Test Title Two"));
    }

    @Test
    void getCardsByCategory_shouldReturnCardList() throws Exception {
        CardDto card = new CardDto(1L, "Test Title", "Test description", "Test fun fact", "http://example.com/image.jpg", "http://example.com/source", 1L);

        when(cardService.getCardsByCategory(1L)).thenReturn(List.of(card));

        mockMvc.perform(get("/api/cards/category/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].categoryId").value(1));
    }

    @Test
    void getCardsByCategory_shouldReturnNotFound_whenCategoryDoesNotExist() throws Exception {
        when(cardService.getCardsByCategory(99L))
                .thenThrow(new CategoryNotFoundException("Category not found with id: 99"));

        mockMvc.perform(get("/api/cards/category/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getCardById_shouldReturnCard_whenCardExists() throws Exception {
        CardDto card = new CardDto(1L, "Test Title", "Test description", "Test fun fact", "http://example.com/image.jpg", "http://example.com/source", 1L);

        when(cardService.getCardById(1L)).thenReturn(card);

        mockMvc.perform(get("/api/cards/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Test Title"));
    }

    @Test
    void getCardById_shouldReturnNotFound_whenCardDoesNotExist() throws Exception {
        when(cardService.getCardById(99L))
                .thenThrow(new CardNotFoundException("Card not found with id: 99"));

        mockMvc.perform(get("/api/cards/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createCard_shouldReturnCreatedCard_whenValidInput() throws Exception {
        CardDto request = new CardDto(null, "Test Title", "Test description", "Test fun fact", "http://example.com/image.jpg", "http://example.com/source", 1L);
        CardDto response = new CardDto(1L, "Test Title", "Test description", "Test fun fact", "http://example.com/image.jpg", "http://example.com/source", 1L);

        when(cardService.createCard(any(CardDto.class))).thenReturn(response);

        mockMvc.perform(post("/api/cards")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Test Title"));
    }

    @Test
    void createCard_shouldReturnBadRequest_whenTitleIsBlank() throws Exception {
        CardDto invalidRequest = new CardDto(null, "", "Test description", "Test fun fact", "http://example.com/image.jpg", "http://example.com/source", 1L);

        mockMvc.perform(post("/api/cards")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createCard_shouldReturnBadRequest_whenCategoryIdIsNull() throws Exception {
        CardDto invalidRequest = new CardDto(null, "Test Title", "Test description", "Test fun fact", "http://example.com/image.jpg", "http://example.com/source", null);

        mockMvc.perform(post("/api/cards")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createCard_shouldReturnNotFound_whenCategoryDoesNotExist() throws Exception {
        CardDto request = new CardDto(null, "Test Title", "Test description", "Test fun fact", "http://example.com/image.jpg", "http://example.com/source", 99L);

        when(cardService.createCard(any(CardDto.class)))
                .thenThrow(new CategoryNotFoundException("Category not found with id: 99"));

        mockMvc.perform(post("/api/cards")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateCard_shouldReturnUpdatedCard() throws Exception {
        CardDto request = new CardDto(null, "Updated Title", "Updated description", "Updated fun fact", "http://example.com/image.jpg", "http://example.com/source", 1L);
        CardDto response = new CardDto(1L, "Updated Title", "Updated description", "Updated fun fact", "http://example.com/image.jpg", "http://example.com/source", 1L);

        when(cardService.updateCard(eq(1L), any(CardDto.class))).thenReturn(response);

        mockMvc.perform(put("/api/cards/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Title"));
    }

    @Test
    void updateCard_shouldReturnNotFound_whenCardDoesNotExist() throws Exception {
        CardDto request = new CardDto(null, "Test Title", "Test description", "Test fun fact", "http://example.com/image.jpg", "http://example.com/source", 1L);

        when(cardService.updateCard(eq(99L), any(CardDto.class)))
                .thenThrow(new CardNotFoundException("Card not found with id: 99"));

        mockMvc.perform(put("/api/cards/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteCard_shouldReturnOk() throws Exception {
        doNothing().when(cardService).deleteCard(1L);

        mockMvc.perform(delete("/api/cards/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Card deleted successfully"));
    }

    @Test
    void deleteCard_shouldReturnNotFound_whenCardDoesNotExist() throws Exception {
        doThrow(new CardNotFoundException("Card not found with id: 99"))
                .when(cardService).deleteCard(99L);

        mockMvc.perform(delete("/api/cards/99"))
                .andExpect(status().isNotFound());
    }
}