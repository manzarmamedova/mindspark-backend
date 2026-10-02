package com.mindspark.backend.service;


import com.mindspark.backend.entity.Category;
import com.mindspark.backend.repository.CardRepository;
import com.mindspark.backend.repository.FavoriteRepository;
import com.mindspark.backend.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import com.mindspark.backend.entity.Card;
import com.mindspark.backend.entity.Favorite;
import com.mindspark.backend.entity.User;
import com.mindspark.backend.dto.CardDto;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FavoriteServiceTest {

    @Mock
    private FavoriteRepository favoriteRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CardRepository cardRepository;

    @InjectMocks
    private FavoriteService favoriteService;

    @Test
    void addFavorite_shouldSaveFavorite() {

        User user = new User();
        user.setId(1L);
        user.setEmail("test@gmail.com");

        Card card = new Card();
        card.setId(6L);

        when(userRepository.findByEmail("test@gmail.com"))
                .thenReturn(Optional.of(user));

        when(cardRepository.findById(6L))
                .thenReturn(Optional.of(card));

        when(favoriteRepository.findByUserIdAndCardId(1L, 6L))
                .thenReturn(Optional.empty());

        favoriteService.addFavorite("test@gmail.com", 6L);

        verify(favoriteRepository).save(any(Favorite.class));
    }

    @Test
    void addFavorite_shouldNotSaveIfAlreadyExists() {

        User user = new User();
        user.setId(1L);
        user.setEmail("test@gmail.com");

        Card card = new Card();
        card.setId(6L);

        Favorite existingFavorite = new Favorite();
        existingFavorite.setUser(user);
        existingFavorite.setCard(card);

        when(userRepository.findByEmail("test@gmail.com"))
                .thenReturn(Optional.of(user));

        when(cardRepository.findById(6L))
                .thenReturn(Optional.of(card));

        when(favoriteRepository.findByUserIdAndCardId(1L, 6L))
                .thenReturn(Optional.of(existingFavorite));

        favoriteService.addFavorite("test@gmail.com", 6L);

        verify(favoriteRepository, never()).save(any(Favorite.class));
    }


    @Test
    void removeFavorite_shouldDeleteFavorite() {

        User user = new User();
        user.setId(1L);
        user.setEmail("test@gmail.com");

        when(userRepository.findByEmail("test@gmail.com"))
                .thenReturn(Optional.of(user));

        favoriteService.removeFavorite("test@gmail.com", 6L);

        verify(favoriteRepository)
                .deleteByUserIdAndCardId(1L, 6L);
    }


    @Test
    void getFavorites_shouldReturnFavoriteCards() {

        User user = new User();
        user.setId(1L);
        user.setEmail("test@gmail.com");

        Category category = new Category();
        category.setId(1L);

        Card card = new Card();
        card.setId(6L);
        card.setTitle("Test Card");
        card.setCategory(category);

        Favorite favorite = new Favorite();
        favorite.setUser(user);
        favorite.setCard(card);

        when(userRepository.findByEmail("test@gmail.com"))
                .thenReturn(Optional.of(user));

        when(favoriteRepository.findByUserId(1L))
                .thenReturn(List.of(favorite));

        List<CardDto> result =
                favoriteService.getFavorites("test@gmail.com");

        assertEquals(1, result.size());
        assertEquals(6L, result.get(0).getId());
        assertEquals("Test Card", result.get(0).getTitle());
    }
}
