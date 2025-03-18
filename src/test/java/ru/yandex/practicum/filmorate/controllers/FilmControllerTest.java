package ru.yandex.practicum.filmorate.controllers;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
class FilmControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCreateFilmWithInvalidName() throws Exception {
        Film film = new Film();
        film.setDescription("Valid description");
        film.setReleaseDate(LocalDate.of(2023, 10, 26));
        film.setDuration(120);

        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(film)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0]").exists())
                .andExpect(jsonPath("$.errors[0]").value("Название фильма не может быть пустым."));
    }

    @Test
    void testCreateFilmWithLongDescription() throws Exception {
        Film film = new Film();
        film.setName("Name");
        film.setDescription("Это очень длинное описание, которое превышает лимит в 200 символов, на которое у меня не" +
                "хватает воображения что-то придумать, поэтому я просто печатаю пасту, которая мне буквально придумывается" +
                "на ходу, ну что поделать, слова заканчиваются, возможно, мне придется ктрл с + ктрл  в ещё раз эту же" +
                "пасту");
        film.setReleaseDate(LocalDate.of(2023, 10, 26));
        film.setDuration(120);

        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(film)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0]").exists())
                .andExpect(jsonPath("$.errors[0]").value("Описание фильма не может превышать 200 символов."));
    }

    @Test
    void testCreateFilmWithNegativeDuration() throws Exception {
        Film film = new Film();
        film.setName("Name");
        film.setDescription("Description");
        film.setReleaseDate(LocalDate.of(2023, 10, 26));
        film.setDuration(-10);

        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(film)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0]").exists())
                .andExpect(jsonPath("$.errors[0]")
                        .value("Продолжительность фильма должна быть положительным числом."));
    }

    @Test
    void testCreateFilmWithZeroDuration() throws Exception {
        Film film = new Film();
        film.setName("Name");
        film.setDescription("Description");
        film.setReleaseDate(LocalDate.of(2023, 10, 26));
        film.setDuration(0);

        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(film)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0]").exists())
                .andExpect(jsonPath("$.errors[0]")
                        .value("Продолжительность фильма должна быть положительным числом."));
    }

    @Test
    void testCreateFilmWithInvalidReleaseDate() throws Exception {
        Film film = new Film();
        film.setName("Name");
        film.setDescription("Description");
        film.setReleaseDate(LocalDate.of(1895, 12, 27));
        film.setDuration(120);

        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(film)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0]").exists())
                .andExpect(jsonPath("$.errors[0]")
                        .value("Дата релиза должна быть не раньше 28 декабря 1895 года"));
    }

    @Test
    void testCreateFilmWithValidData() throws Exception {
        Film film = new Film();
        film.setName("Name");
        film.setDescription("Description");
        film.setReleaseDate(LocalDate.of(2023, 10, 26));
        film.setDuration(120);

        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(film)))
                .andExpect(status().isCreated());
    }
}