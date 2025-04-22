package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.dto.mappers.FilmMapper;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/films")
@Slf4j
@RequiredArgsConstructor
public class FilmController {

    private final FilmService filmService;

    @GetMapping
    public ResponseEntity<List<FilmDto>> getAll() {
        log.info("Запрос на получение всех фильмов");

        List<FilmDto> films = filmService.getAll()
                .stream()
                .map(FilmMapper::mapToFilmDto)
                .collect(Collectors.toList());
        return new ResponseEntity<>(films, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<FilmDto> create(@Valid @RequestBody FilmDto filmDto) {
        log.info("Запрос на создание фильма: {}", filmDto);

        Film film = FilmMapper.mapToFilmModel(filmDto);
        Film createdFilm = filmService.createFilm(film);
        return new ResponseEntity<>(FilmMapper.mapToFilmDto(createdFilm), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<FilmDto> update(@Valid @RequestBody FilmDto filmDto) {
        log.info("Запрос на обновление фильма: {}", filmDto);

        Film film = FilmMapper.mapToFilmModel(filmDto);
        Film updatedFilm = filmService.updateFilm(film);
        return new ResponseEntity<>(FilmMapper.mapToFilmDto(updatedFilm), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FilmDto> getFilmById(@PathVariable Long id) {
        log.info("Запрос на получение пользователя по id: {}", id);

        Optional<Film> filmOptional = filmService.getFilmById(id);
        Film film = filmOptional.orElseThrow(() -> new NotFoundException("Фильм с id " + id + " не найден."));
        log.info("Фильм с id: {} успешно найден", id);
        return new ResponseEntity<>(FilmMapper.mapToFilmDto(film), HttpStatus.OK);
    }

    @PutMapping("/{id}/like/{userId}")
    public ResponseEntity<Void> addLike(@PathVariable Long id, @PathVariable Long userId) {
        log.info("Запрос на добавление лайка фильму с id: {} от пользователя с id {}", id, userId);

        filmService.addLike(id, userId);
        log.info("Пользователь с id {} поставил лайк фильму с id {}", userId, id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public ResponseEntity<Void> deleteLike(@PathVariable Long id, @PathVariable Long userId) {
        log.info("Запрос на удаление лайка фильму с id: {} от пользователя с id {}", id, userId);

        filmService.deleteLike(id, userId);
        log.info("Пользователь с id {} удалил лайк фильму с id {}", userId, id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/popular")
    public ResponseEntity<List<FilmDto>> getPopularFilms(@RequestParam(defaultValue = "10") Integer count) {
        log.info("Запрос на получение списка популярных фильмов");

        List<FilmDto> popularFilms = filmService.getPopularFilms(count)
                .stream()
                .map(FilmMapper::mapToFilmDto)
                .collect(Collectors.toList());
        log.info("Получен список популярных фильмов (количество: {})", popularFilms.size());
        return new ResponseEntity<>(popularFilms, HttpStatus.OK);
    }
}
