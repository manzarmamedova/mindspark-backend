package com.mindspark.backend.service;

import com.mindspark.backend.dto.CardDto;
import com.mindspark.backend.entity.Card;
import com.mindspark.backend.entity.Category;
import com.mindspark.backend.repository.CardRepository;
import com.mindspark.backend.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CardService {

    private final CardRepository cardRepository;
    private final CategoryRepository categoryRepository;

    public CardService(CardRepository cardRepository,
                       CategoryRepository categoryRepository) {
        this.cardRepository = cardRepository;
        this.categoryRepository = categoryRepository;
    }


    // Get cards by category
    public List<CardDto> getCardsByCategory(Long categoryId) {

        List<Card> cards = cardRepository.findByCategoryId(categoryId);

        return cards.stream()
                .map(this::mapToDto)
                .toList();
    }


    // Get all cards
    public List<CardDto> getAllCards() {

        return cardRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .toList();
    }


    // Get card by id
    public CardDto getCardById(Long id) {

        Card card = cardRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Card not found with id: " + id));

        return mapToDto(card);
    }


    // Create card
    public CardDto createCard(CardDto dto) {

        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Category not found with id: " + dto.getCategoryId()
                        ));

        Card card = new Card();

        card.setTitle(dto.getTitle());
        card.setDescription(dto.getDescription());
        card.setFunFact(dto.getFunFact());
        card.setImageUrl(dto.getImageUrl());
        card.setSourceUrl(dto.getSourceUrl());
        card.setCategory(category);

        Card savedCard = cardRepository.save(card);

        return mapToDto(savedCard);
    }


    // Update card
    public CardDto updateCard(Long id, CardDto dto) {

        Card card = cardRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Card not found with id: " + id));


        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Category not found with id: " + dto.getCategoryId()
                        ));


        card.setTitle(dto.getTitle());
        card.setDescription(dto.getDescription());
        card.setFunFact(dto.getFunFact());
        card.setImageUrl(dto.getImageUrl());
        card.setSourceUrl(dto.getSourceUrl());
        card.setCategory(category);


        Card updatedCard = cardRepository.save(card);

        return mapToDto(updatedCard);
    }


    // Delete card
    public void deleteCard(Long id) {

        Card card = cardRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Card not found with id: " + id));

        cardRepository.delete(card);
    }



    // Entity -> DTO
    private CardDto mapToDto(Card card) {

        CardDto dto = new CardDto();

        dto.setId(card.getId());
        dto.setTitle(card.getTitle());
        dto.setDescription(card.getDescription());
        dto.setFunFact(card.getFunFact());
        dto.setImageUrl(card.getImageUrl());
        dto.setSourceUrl(card.getSourceUrl());
        dto.setCategoryId(card.getCategory().getId());

        return dto;
    }
}