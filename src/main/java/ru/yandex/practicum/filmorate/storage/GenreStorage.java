package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.film.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreStorage {

    List<Genre> findAll();

    Optional<Genre> findById(int id);

    List<Genre> findGenresByFilmId(Long filmId);
}
