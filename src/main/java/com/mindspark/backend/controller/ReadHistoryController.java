package com.mindspark.backend.controller;

import com.mindspark.backend.dto.CardDto;
import com.mindspark.backend.service.ReadHistoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/history")
public class ReadHistoryController {

    private final ReadHistoryService readHistoryService;

    public ReadHistoryController(ReadHistoryService readHistoryService) {
        this.readHistoryService = readHistoryService;
    }

    @GetMapping
    public ResponseEntity<List<CardDto>> getHistory(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(readHistoryService.getHistory(userDetails.getUsername()));
    }

    @PostMapping("/{cardId}")
    public ResponseEntity<Void> markAsRead(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long cardId
    ) {
        readHistoryService.markAsRead(userDetails.getUsername(), cardId);
        return ResponseEntity.ok().build();
    }


}