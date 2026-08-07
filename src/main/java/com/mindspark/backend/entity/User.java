package com.mindspark.backend.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name="users")
public class User {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    @Column(nullable=false)
    private String name;

    @NotBlank
    @Column(nullable=false, unique = true)
    private String email;

    @NotBlank
    @Column(nullable=false)
    @Size(min = 8)
    private String password;


}
