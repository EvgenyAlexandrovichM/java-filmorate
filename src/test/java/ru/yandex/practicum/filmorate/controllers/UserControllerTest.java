/* package ru.yandex.practicum.filmorate.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.model.user.User;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCreateUserWithInvalidEmail() throws Exception {
        User user = new User();
        user.setEmail("invalid-email");
        user.setLogin("validLogin");
        user.setName("validName");
        user.setBirthday(LocalDate.of(1990, 1, 1));

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0]").exists())
                .andExpect(jsonPath("$.errors[0]").value("Некорректный формат электронной почты."));
    }

    @Test
    void testCreateUserWithBlankLogin() throws Exception {
        User user = new User();
        user.setEmail("valid@email.com");
        user.setLogin(" ");
        user.setName("validName");
        user.setBirthday(LocalDate.of(1990, 1, 1));

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0]").exists())
                .andExpect(jsonPath("$.errors[0]").value("Логин не может быть пустым."));
    }

    @Test
    void testCreateUserWithFutureBirthday() throws Exception {
        User user = new User();
        user.setEmail("valid@email.com");
        user.setLogin("validLogin");
        user.setName("validName");
        user.setBirthday(LocalDate.now().plusDays(1));

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0]").exists())
                .andExpect(jsonPath("$.errors[0]").value("Дата рождения не может быть в будущем."));
    }

    @Test
    void testCreateUserWithValidData() throws Exception {
        User user = new User();
        user.setEmail("valid@email.com");
        user.setLogin("validLogin");
        user.setName("validName");
        user.setBirthday(LocalDate.of(1990, 1, 1));

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated());
    }
} */