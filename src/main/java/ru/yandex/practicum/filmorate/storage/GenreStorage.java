package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.genre.Genre;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public interface GenreStorage {

    List<Genre> findAllGenres();

    Optional<Genre> findGenreById(int id);

    List<Genre> findGenresByFilmId(Long id);

    Map<Long, Set<Genre>> findAllFilmGenres();
}
