package com.mindspark.backend.service;


import com.mindspark.backend.dto.CardDto;
import com.mindspark.backend.entity.Category;
import com.mindspark.backend.exception.CardNotFoundException;
import com.mindspark.backend.exception.UserNotFoundException;
import com.mindspark.backend.repository.CardRepository;
import com.mindspark.backend.repository.ReadHistoryRepository;
import com.mindspark.backend.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.mindspark.backend.entity.Card;
import com.mindspark.backend.entity.ReadHistory;
import com.mindspark.backend.entity.User;

import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReadHistoryServiceTest {

    @Mock
    private ReadHistoryRepository readHistoryRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CardRepository cardRepository;

    @InjectMocks
    private ReadHistoryService readHistoryService;


    @Test
    void markAsRead_shouldSaveHistory() {

        User user = new User();
        user.setId(1L);
        user.setEmail("test@gmail.com");

        Card card = new Card();
        card.setId(6L);

        when(userRepository.findByEmail("test@gmail.com"))
                .thenReturn(Optional.of(user));

        when(cardRepository.findById(6L))
                .thenReturn(Optional.of(card));

        readHistoryService.markAsRead("test@gmail.com", 6L);

        verify(readHistoryRepository)
                .save(any(ReadHistory.class));
    }


    @Test
    void markAsRead_shouldThrowExceptionWhenUserNotFound() {

        when(userRepository.findByEmail("test@gmail.com"))
                .thenReturn(Optional.empty());

        assertThrows(
                UserNotFoundException.class,
                () -> readHistoryService.markAsRead("test@gmail.com", 6L)
        );

        verify(cardRepository, never()).findById(6L);
        verify(readHistoryRepository, never()).save(any(ReadHistory.class));
    }


    @Test
    void markAsRead_shouldThrowExceptionWhenCardNotFound() {

        User user = new User();
        user.setId(1L);
        user.setEmail("test@gmail.com");

        when(userRepository.findByEmail("test@gmail.com"))
                .thenReturn(Optional.of(user));

        when(cardRepository.findById(6L))
                .thenReturn(Optional.empty());

        assertThrows(
                CardNotFoundException.class,
                () -> readHistoryService.markAsRead("test@gmail.com", 6L)
        );

        verify(readHistoryRepository, never())
                .save(any(ReadHistory.class));
    }

    @Test
    void getHistory_shouldReturnHistoryCards() {

        User user = new User();
        user.setId(1L);
        user.setEmail("test@gmail.com");

        Category category = new Category();
        category.setId(1L);

        Card card = new Card();
        card.setId(6L);
        card.setTitle("Test Card");
        card.setCategory(category);

        ReadHistory history = new ReadHistory();
        history.setUser(user);
        history.setCard(card);

        when(userRepository.findByEmail("test@gmail.com"))
                .thenReturn(Optional.of(user));

        when(readHistoryRepository.findByUserIdOrderByReadAtDesc(1L))
                .thenReturn(List.of(history));

        List<CardDto> result =
                readHistoryService.getHistory("test@gmail.com");

        assertEquals(1, result.size());
        assertEquals(6L, result.get(0).getId());
        assertEquals("Test Card", result.get(0).getTitle());
    }

}



