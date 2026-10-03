package com.mindspark.backend.service;

import com.mindspark.backend.dto.CardDto;
import com.mindspark.backend.entity.Card;
import com.mindspark.backend.entity.ReadHistory;
import com.mindspark.backend.entity.User;
import com.mindspark.backend.exception.CardNotFoundException;
import com.mindspark.backend.exception.UserNotFoundException;
import com.mindspark.backend.repository.CardRepository;
import com.mindspark.backend.repository.ReadHistoryRepository;
import com.mindspark.backend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class ReadHistoryService {

    private final ReadHistoryRepository readHistoryRepository;
    private final UserRepository userRepository;
    private final CardRepository cardRepository;

    public ReadHistoryService(ReadHistoryRepository readHistoryRepository,
                              UserRepository userRepository,
                              CardRepository cardRepository) {
        this.readHistoryRepository = readHistoryRepository;
        this.userRepository = userRepository;
        this.cardRepository = cardRepository;
    }

    public List<CardDto> getHistory(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));

        return readHistoryRepository.findByUserIdOrderByReadAtDesc(user.getId())
                .stream()
                .map(readHistory -> mapToCardDto(readHistory.getCard()))
                .toList();
    }

    public void markAsRead(String email, Long cardId) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(
                        "User not found with email: " + email
                ));

        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new CardNotFoundException(
                        "Card not found with id: " + cardId
                ));

        Optional<ReadHistory> existingHistory =
                readHistoryRepository.findByUserIdAndCardId(
                        user.getId(),
                        cardId
                );

        if (existingHistory.isPresent()) {

            ReadHistory history = existingHistory.get();
            history.setReadAt(Instant.now());

            readHistoryRepository.save(history);

        } else {

            ReadHistory readHistory = new ReadHistory();
            readHistory.setUser(user);
            readHistory.setCard(card);

            readHistoryRepository.save(readHistory);
        }
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