package com.mindspark.backend.service;

import com.mindspark.backend.dto.CardDto;
import com.mindspark.backend.entity.Card;
import com.mindspark.backend.entity.Favorite;
import com.mindspark.backend.entity.User;
import com.mindspark.backend.exception.CardNotFoundException;
import com.mindspark.backend.exception.UserNotFoundException;
import com.mindspark.backend.repository.CardRepository;
import com.mindspark.backend.repository.FavoriteRepository;
import com.mindspark.backend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final UserRepository userRepository;
    private final CardRepository cardRepository;

    public FavoriteService(FavoriteRepository favoriteRepository,
                           UserRepository userRepository,
                           CardRepository cardRepository) {
        this.favoriteRepository = favoriteRepository;
        this.userRepository = userRepository;
        this.cardRepository = cardRepository;
    }

    public List<CardDto> getFavorites(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));

        return favoriteRepository.findByUserId(user.getId())
                .stream()
                .map(favorite -> mapToCardDto(favorite.getCard()))
                .toList();
    }

    public void addFavorite(String email, Long cardId) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));

        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new CardNotFoundException("Card not found with id: " + cardId));

        boolean alreadyExists = favoriteRepository.findByUserIdAndCardId(user.getId(), cardId).isPresent();
        if (alreadyExists) {
            return;
        }

        Favorite favorite = new Favorite();
        favorite.setUser(user);
        favorite.setCard(card);

        favoriteRepository.save(favorite);
    }

    public void removeFavorite(String email, Long cardId) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));

        favoriteRepository.deleteByUserIdAndCardId(user.getId(), cardId);
    }

    private CardDto mapToCardDto(Card card) {
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