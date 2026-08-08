package com.mindspark.backend.controller;

import com.mindspark.backend.dto.UserDto;
import com.mindspark.backend.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.json.JsonMapper;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JsonMapper jsonMapper;

    @MockitoBean
    private UserService userService;

    @Test
    void getAllUsers_shouldReturnUserList() throws Exception {
        UserDto user1 = new UserDto(1L, "Ali Veli", "ali@example.com", "password123");
        UserDto user2 = new UserDto(2L, "Ayşe Yılmaz", "ayse@example.com", "password456");

        when(userService.getAllUsers()).thenReturn(List.of(user1, user2));

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Ali Veli"))
                .andExpect(jsonPath("$[1].email").value("ayse@example.com"));
    }

    @Test
    void getUserById_shouldReturnUser_whenUserExists() throws Exception {
        UserDto user = new UserDto(1L, "Ali Veli", "ali@example.com", "password123");

        when(userService.getUserById(1L)).thenReturn(user);

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Ali Veli"));
    }

    @Test
    void createUser_shouldReturnCreatedUser_whenValidInput() throws Exception {
        UserDto request = new UserDto(null, "Ali Veli", "ali@example.com", "password123");
        UserDto response = new UserDto(1L, "Ali Veli", "ali@example.com", "password123");

        when(userService.createUser(any(UserDto.class))).thenReturn(response);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Ali Veli"));
    }

    @Test
    void createUser_shouldReturnBadRequest_whenEmailInvalid() throws Exception {
        UserDto invalidRequest = new UserDto(null, "Ali Veli", "not-an-email", "password123");

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createUser_shouldReturnBadRequest_whenPasswordTooShort() throws Exception {
        UserDto invalidRequest = new UserDto(null, "Ali Veli", "ali@example.com", "short");

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateUser_shouldReturnUpdatedUser() throws Exception {
        UserDto request = new UserDto(null, "Ali Veli Güncel", "ali@example.com", "password123");
        UserDto response = new UserDto(1L, "Ali Veli Güncel", "ali@example.com", "password123");

        when(userService.updateUser(eq(1L), any(UserDto.class))).thenReturn(response);

        mockMvc.perform(put("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Ali Veli Güncel"));
    }

    @Test
    void deleteUser_shouldReturnOk() throws Exception {
        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isOk());
    }
}