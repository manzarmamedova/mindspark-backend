package com.mindspark.backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class UserDto {
    private Long id;

    @NotBlank(message = "User name cannot be empty")
    private String name;

    @Email(message = "Email format is not valid")
    @NotBlank(message = "email cannot be empty")
    private String email;

    @Size(min = 8, message = "Password must be at least 8 characters")
    @NotBlank(message = "Password cannot be empty")
    private String password;
}
