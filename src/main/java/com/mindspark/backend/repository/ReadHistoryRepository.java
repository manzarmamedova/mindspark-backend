package com.mindspark.backend.repository;

import com.mindspark.backend.entity.ReadHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReadHistoryRepository extends JpaRepository<ReadHistory, Long> {
    List<ReadHistory> findByUserIdOrderByReadAtDesc(Long userId);

    Optional<ReadHistory> findByUserIdAndCardId(Long userId, Long cardId);
}