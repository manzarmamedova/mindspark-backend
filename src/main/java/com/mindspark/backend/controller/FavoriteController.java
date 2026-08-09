package com.mindspark.backend.controller;

import com.mindspark.backend.dto.CardDto;
import com.mindspark.backend.service.FavoriteService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/favorites")
public class FavoriteController {

    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @GetMapping
    public ResponseEntity<List<CardDto>> getFavorites(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(favoriteService.getFavorites(userDetails.getUsername()));
    }

    @PostMapping("/{cardId}")
    public ResponseEntity<Void> addFavorite(@AuthenticationPrincipal UserDetails userDetails,
                                            @PathVariable Long cardId) {
        favoriteService.addFavorite(userDetails.getUsername(), cardId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{cardId}")
    public ResponseEntity<Void> removeFavorite(@AuthenticationPrincipal UserDetails userDetails,
                                               @PathVariable Long cardId) {
        favoriteService.removeFavorite(userDetails.getUsername(), cardId);
        return ResponseEntity.ok().build();
    }
}