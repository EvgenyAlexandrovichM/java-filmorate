package ru.yandex.practicum.filmorate.storage.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.*;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Long, Film> films = new HashMap<>();

    @Override
    public List<Film> findAll() {
        log.info("Список всех фильмов");
        return new ArrayList<>(films.values());
    }

    @Override
    public Film createFilm(Film film) {
        log.info("Создание фильма: {}", film);
        film.setId(getNextId());
        films.put(film.getId(), film);
        log.info("Фильм создан с id: {}", film.getId());
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        log.info("Обновление фильма: {}", film);
        if (film.getId() == null || !films.containsKey(film.getId())) {
            log.warn("Фильм с id {} не найден", film.getId());
            throw new NotFoundException("Фильм с id " + film.getId() + " не найден");
        }
        films.put(film.getId(), film);
        log.info("Фильм {} обновлен", film);
        return film;
    }

    @Override
    public Optional<Film> findFilmById(Long id) {
        return Optional.ofNullable(films.get(id));
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
