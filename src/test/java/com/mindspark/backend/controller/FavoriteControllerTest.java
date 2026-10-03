package com.mindspark.backend.controller;

import com.mindspark.backend.config.CustomUserDetailsService;
import com.mindspark.backend.dto.CardDto;
import com.mindspark.backend.service.FavoriteService;
import com.mindspark.backend.service.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FavoriteController.class)
class FavoriteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private FavoriteService favoriteService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private CustomUserDetailsService customUserDetailsService;


    @Test
    void getFavorites_shouldReturnFavorites() throws Exception {

        CardDto card = new CardDto(
                6L,
                "Test Card",
                "Test description",
                "Test fun fact",
                "http://example.com/image.jpg",
                "http://example.com/source",
                1L
        );

        when(favoriteService.getFavorites("test@gmail.com"))
                .thenReturn(List.of(card));

        mockMvc.perform(
                        get("/api/favorites")
                                .with(user("test@gmail.com"))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(6))
                .andExpect(jsonPath("$[0].title").value("Test Card"));

        verify(favoriteService)
                .getFavorites("test@gmail.com");
    }


    @Test
    void addFavorite_shouldReturnOk() throws Exception {

        mockMvc.perform(
                        post("/api/favorites/6")
                                .with(user("test@gmail.com"))
                                .with(csrf())
                )
                .andExpect(status().isOk());

        verify(favoriteService)
                .addFavorite("test@gmail.com", 6L);
    }


    @Test
    void removeFavorite_shouldReturnOk() throws Exception {

        mockMvc.perform(
                        delete("/api/favorites/6")
                                .with(user("test@gmail.com"))
                                .with(csrf())
                )
                .andExpect(status().isOk());

        verify(favoriteService)
                .removeFavorite("test@gmail.com", 6L);
    }
}