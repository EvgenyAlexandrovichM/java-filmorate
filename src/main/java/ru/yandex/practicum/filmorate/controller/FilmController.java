package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.*;

@RestController
@RequestMapping("/films")
@Slf4j
@RequiredArgsConstructor
public class FilmController {

    private final FilmService filmService;

    @GetMapping
    public ResponseEntity<List<Film>> findAll() {
        log.info("Запрос на получение всех фильмов");
        List<Film> films = filmService.findAll();
        return new ResponseEntity<>(films, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Film> create(@Valid @RequestBody Film film) {
        log.info("Запрос на создание фильма: {}", film);
        Film createdFilm = filmService.createFilm(film);
        return new ResponseEntity<>(createdFilm, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<Film> update(@Valid @RequestBody Film film) {
        log.info("Запрос на обновление фильма: {}", film);
        Film updatedFilm = filmService.updateFilm(film);
        return new ResponseEntity<>(updatedFilm, HttpStatus.OK);
    }
}
