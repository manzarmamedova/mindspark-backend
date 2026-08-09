package com.mindspark.backend.repository;

import com.mindspark.backend.entity.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    List<Favorite> findByUserId(Long userId);
    Optional<Favorite> findByUserIdAndCardId(Long userId, Long cardId);
    void deleteByUserIdAndCardId(Long userId, Long cardId);
}