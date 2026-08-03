package com.mindspark.backend.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = "category")
@EqualsAndHashCode(exclude = "category")
@Table(name="cards")

public class Card {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false)
    private String title;

    @Column(length = 500, nullable = false)
    private String description;

    @Column(length = 500)
    private String funFact;

    private String imageUrl;

    private String sourceUrl;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;
}
