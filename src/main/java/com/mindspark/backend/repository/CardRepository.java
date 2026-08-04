package com.mindspark.backend.repository;

import com.mindspark.backend.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CardRepository extends JpaRepository<Card, Long> {
    List<Card> findByCategoryId(Long categoryId);
}
