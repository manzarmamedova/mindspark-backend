package com.mindspark.backend.service;

import com.mindspark.backend.dto.UserDto;
import com.mindspark.backend.entity.User;
import com.mindspark.backend.exception.UserNotFoundException;
import com.mindspark.backend.repository.UserRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class UserServiceTest {


    @Mock
    private UserRepository userRepository;


    @InjectMocks
    private UserService userService;



    @Test
    void createUser_shouldCreateUser() {

        UserDto dto = new UserDto(
                null,
                "Test User",
                "test@gmail.com",
                "password123"
        );


        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setName("Test User");
        savedUser.setEmail("test@gmail.com");
        savedUser.setPassword("password123");


        when(userRepository.save(any(User.class)))
                .thenReturn(savedUser);


        UserDto result = userService.createUser(dto);


        assertEquals("Test User", result.getName());
        assertEquals("test@gmail.com", result.getEmail());
    }




    @Test
    void getAllUsers_shouldReturnUserList() {

        User user = new User();
        user.setId(1L);
        user.setName("Test User");
        user.setEmail("test@gmail.com");


        when(userRepository.findAll())
                .thenReturn(List.of(user));


        List<UserDto> result = userService.getAllUsers();


        assertEquals(1, result.size());
        assertEquals("Test User", result.get(0).getName());
    }




    @Test
    void getUserById_shouldReturnUser() {

        User user = new User();
        user.setId(1L);
        user.setName("Test User");


        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));


        UserDto result = userService.getUserById(1L);


        assertEquals("Test User", result.getName());
    }





    @Test
    void getUserById_shouldThrowException_whenUserNotFound() {


        when(userRepository.findById(99L))
                .thenReturn(Optional.empty());


        assertThrows(
                UserNotFoundException.class,
                () -> userService.getUserById(99L)
        );
    }




    @Test
    void updateUser_shouldUpdateUser() {

        User user = new User();

        user.setId(1L);
        user.setName("Old");
        user.setEmail("old@gmail.com");
        user.setPassword("12345678");


        UserDto dto = new UserDto(
                null,
                "New",
                "new@gmail.com",
                "87654321"
        );


        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));


        when(userRepository.save(any(User.class)))
                .thenReturn(user);



        UserDto result = userService.updateUser(1L, dto);



        assertEquals("New", result.getName());
        assertEquals("new@gmail.com", result.getEmail());
    }





    @Test
    void deleteUser_shouldDeleteUser() {


        User user = new User();

        user.setId(1L);


        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));


        userService.deleteUser(1L);


        verify(userRepository)
                .delete(user);
    }

}