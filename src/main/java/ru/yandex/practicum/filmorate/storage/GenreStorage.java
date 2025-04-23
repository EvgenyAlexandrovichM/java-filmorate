package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.genre.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreStorage {

    List<Genre> findAllGenres();

    Optional<Genre> findGenreById(int id);

    List<Genre> findGenresByFilmId(Long id);
}
