package com.mindspark.backend.controller;

import com.mindspark.backend.dto.CardDto;
import com.mindspark.backend.service.CardService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cards")
@CrossOrigin(origins = "*")
public class CardController {

    private final CardService cardService;


    public CardController(CardService cardService) {
        this.cardService = cardService;
    }


    // Get all cards
    @GetMapping
    public ResponseEntity<List<CardDto>> getAllCards() {

        return ResponseEntity.ok(
                cardService.getAllCards()
        );
    }


    // Get cards by category
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<CardDto>> getCardsByCategory(
            @PathVariable Long categoryId) {

        return ResponseEntity.ok(
                cardService.getCardsByCategory(categoryId)
        );
    }


    // Get card by id
    @GetMapping("/{id}")
    public ResponseEntity<CardDto> getCardById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                cardService.getCardById(id)
        );
    }


    // Create card
    @PostMapping
    public ResponseEntity<CardDto> createCard(
            @RequestBody CardDto dto) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(cardService.createCard(dto));
    }


    // Update card
    @PutMapping("/{id}")
    public ResponseEntity<CardDto> updateCard(
            @PathVariable Long id,
            @RequestBody CardDto dto) {

        return ResponseEntity.ok(
                cardService.updateCard(id, dto)
        );
    }


    // Delete card
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCard(
            @PathVariable Long id) {

        cardService.deleteCard(id);

        return ResponseEntity.ok(
                "Card deleted successfully"
        );
    }
}