package com.mindspark.backend.service;
import com.mindspark.backend.dto.CardDto;
import com.mindspark.backend.entity.Card;
import com.mindspark.backend.entity.Category;
import com.mindspark.backend.exception.CardNotFoundException;
import com.mindspark.backend.exception.CategoryNotFoundException;
import com.mindspark.backend.repository.CardRepository;
import com.mindspark.backend.repository.CategoryRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class CardServiceTest {
    @Mock
    private CardRepository cardRepository;

    @Mock
    private CategoryRepository categoryRepository;


    @InjectMocks
    private CardService cardService;



    @Test
    void createCard_shouldCreateCard() {

        Category category = new Category();
        category.setId(1L);
        category.setCategoryName("Bilim");
        category.setIcon("🔬");


        CardDto dto = new CardDto(
                null,
                "Kara Delikler",
                "Uzaydaki güçlü çekim alanları",
                "İlginç bilgi",
                "image.jpg",
                "source.com",
                1L
        );


        Card savedCard = new Card();
        savedCard.setId(1L);
        savedCard.setTitle("Kara Delikler");
        savedCard.setDescription("Uzaydaki güçlü çekim alanları");
        savedCard.setFunFact("İlginç bilgi");
        savedCard.setImageUrl("image.jpg");
        savedCard.setSourceUrl("source.com");
        savedCard.setCategory(category);


        when(categoryRepository.findById(1L))
                .thenReturn(Optional.of(category));


        when(cardRepository.save(any(Card.class)))
                .thenReturn(savedCard);



        CardDto result = cardService.createCard(dto);


        assertEquals("Kara Delikler", result.getTitle());
        assertEquals(1L, result.getCategoryId());
    }




    @Test
    void getAllCards_shouldReturnCardList() {

        Card card = new Card();
        card.setId(1L);
        card.setTitle("Mars");

        Category category = new Category();
        category.setId(1L);

        card.setCategory(category);


        when(cardRepository.findAll())
                .thenReturn(List.of(card));


        List<CardDto> result = cardService.getAllCards();


        assertEquals(1, result.size());
        assertEquals("Mars", result.get(0).getTitle());
    }




    @Test
    void getCardById_shouldReturnCard() {

        Card card = new Card();
        card.setId(1L);
        card.setTitle("Mars");


        Category category = new Category();
        category.setId(1L);

        card.setCategory(category);



        when(cardRepository.findById(1L))
                .thenReturn(Optional.of(card));



        CardDto result = cardService.getCardById(1L);



        assertEquals("Mars", result.getTitle());
    }




    @Test
    void getCardById_shouldThrowException_whenCardNotFound() {


        when(cardRepository.findById(99L))
                .thenReturn(Optional.empty());


        assertThrows(
                CardNotFoundException.class,
                () -> cardService.getCardById(99L)
        );
    }





    @Test
    void updateCard_shouldUpdateCard() {


        Category category = new Category();
        category.setId(1L);


        Card card = new Card();
        card.setId(1L);
        card.setTitle("Eski");
        card.setCategory(category);



        CardDto dto = new CardDto(
                null,
                "Yeni",
                "Description",
                "Fact",
                "image",
                "source",
                1L
        );



        when(cardRepository.findById(1L))
                .thenReturn(Optional.of(card));


        when(categoryRepository.findById(1L))
                .thenReturn(Optional.of(category));


        when(cardRepository.save(any(Card.class)))
                .thenReturn(card);



        CardDto result = cardService.updateCard(1L, dto);



        assertEquals("Yeni", result.getTitle());

    }





    @Test
    void deleteCard_shouldDeleteCard() {


        Card card = new Card();

        card.setId(1L);


        when(cardRepository.findById(1L))
                .thenReturn(Optional.of(card));



        cardService.deleteCard(1L);



        verify(cardRepository)
                .delete(card);
    }





    @Test
    void createCard_shouldThrowException_whenCategoryNotFound() {


        CardDto dto = new CardDto(
                null,
                "Test",
                "Desc",
                "Fact",
                "image",
                "source",
                99L
        );


        when(categoryRepository.findById(99L))
                .thenReturn(Optional.empty());



        assertThrows(
                CategoryNotFoundException.class,
                () -> cardService.createCard(dto)
        );
    }
}

