package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.film.Film;

import java.util.List;
import java.util.Optional;

public interface FilmStorage {

    List<Film> findAll();

    Film createFilm(Film film);

    Film updateFilm(Film film);

    Optional<Film> findFilmById(Long id);

    List<Film> findPopularFilms(int count);
}
