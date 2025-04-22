package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.film.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreService {

     List<Genre> getAllGenres();

     Optional<Genre> getGenreById(int id);

}
