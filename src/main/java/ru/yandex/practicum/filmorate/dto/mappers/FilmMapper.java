package ru.yandex.practicum.filmorate.dto.mappers;

import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.model.film.Genre;
import ru.yandex.practicum.filmorate.model.film.MpaRating;

import java.util.stream.Collectors;

public class FilmMapper {

    public static FilmDto mapToFilmDto(Film film) {
        FilmDto dto = new FilmDto();
        dto.setId(film.getId());
        dto.setTitle(film.getTitle());
        dto.setDescription(film.getDescription());
        dto.setReleaseDate(film.getReleaseDate());
        dto.setDuration(film.getDuration());
        dto.setGenreIds(film.getGenre()
                .stream()
                .map(Genre::getGenreId)
                .collect(Collectors.toSet()));
        dto.setMpaRatingId(film.getMpaRating().getMpaRatingId());
        dto.setLikeUserIds(film.getLikes());
        return dto;
    }

    public static Film mapToFilmModel(FilmDto dto) {
        Film film = new Film();
        film.setId(dto.getId());
        film.setTitle(dto.getTitle());
        film.setDescription(dto.getDescription());
        film.setReleaseDate(dto.getReleaseDate());
        film.setDuration(dto.getDuration());
        film.setGenre(dto.getGenreIds()
                .stream()
                .map(genreId -> {
                    Genre genre = new Genre();
                    genre.setGenreId(genreId);
                    return genre;
                })
                .collect(Collectors.toSet()));
        MpaRating mpaRating = new MpaRating();
        mpaRating.setMpaRatingId(dto.getMpaRatingId());
        film.setMpaRating(mpaRating);
        film.setLikes(dto.getLikeUserIds());
        return film;
    }
}
