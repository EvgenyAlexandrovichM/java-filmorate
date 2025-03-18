package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {

    private final Map<Long, Film> films = new HashMap<>();

    @GetMapping
    public ResponseEntity<List<Film>> findAll() {
        log.info("Запрос на получение всех фильмов");
        return new ResponseEntity<>(new ArrayList<>(films.values()), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Film> create(@Valid @RequestBody Film film) {
        log.info("Запрос на создание фильма: {}", film);
        try {
            film.setId(getNextId());
            films.put(film.getId(), film);
            log.info("Создан фильм с id: {}", film.getId());
            return new ResponseEntity<>(film, HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Произошла ошибка при добавлении фильма: {}", e.getMessage());
            throw e;
        }
    }

    @PutMapping
    public ResponseEntity<Film> update(@Valid @RequestBody Film film) {
        log.info("Запрос на обновление фильма: {}", film);
        try {
            if (film.getId() == null || !films.containsKey(film.getId())) {
                log.warn("Фильм с id {} не найден", film.getId());
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            films.put(film.getId(), film);
            log.info("Обновлен фильм: {}", film);
            return new ResponseEntity<>(film, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Произошла ошибка при обновлении фильма: {}", e.getMessage());
            throw e;
        }
    }

    private long getNextId() {
        long currentMaxId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
