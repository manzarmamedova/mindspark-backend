package com.mindspark.backend.controller;

import com.mindspark.backend.dto.UserDto;
import com.mindspark.backend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;


    public UserController(UserService userService) {
        this.userService = userService;
    }


    // Get all users
    @GetMapping
    public List<UserDto> getAllUsers() {

        return userService.getAllUsers();
    }


    // Get user by id
    @GetMapping("/{id}")
    public UserDto getUserById(
            @PathVariable Long id) {

        return userService.getUserById(id);
    }


    // Create user
    @PostMapping
    public UserDto createUser(
            @Valid @RequestBody UserDto dto) {

        return userService.createUser(dto);
    }


    // Update user
    @PutMapping("/{id}")
    public UserDto updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserDto dto) {

        return userService.updateUser(id, dto);
    }


    // Delete user
    @DeleteMapping("/{id}")
    public void deleteUser(
            @PathVariable Long id) {

        userService.deleteUser(id);
    }

}