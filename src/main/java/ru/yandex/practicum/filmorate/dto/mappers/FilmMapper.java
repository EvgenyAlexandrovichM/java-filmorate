package ru.yandex.practicum.filmorate.dto.mappers;

import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.dto.GenreDto;
import ru.yandex.practicum.filmorate.dto.MpaRatingDto;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.model.genre.Genre;
import ru.yandex.practicum.filmorate.model.mpa.MpaRating;

import java.util.stream.Collectors;

public class FilmMapper {

    public static FilmDto mapToFilmDto(Film film) {
        FilmDto dto = new FilmDto();
        dto.setId(film.getId());
        dto.setName(film.getName());
        dto.setDescription(film.getDescription());
        dto.setReleaseDate(film.getReleaseDate());
        dto.setDuration(film.getDuration());

        if (film.getMpaRating() != null) {
            MpaRatingDto mpa = new MpaRatingDto();
            mpa.setId(film.getMpaRating().getMpaRatingId());
            mpa.setName(film.getMpaRating().getName());
            dto.setMpa(mpa);
        }
        dto.setGenres(film.getGenres()
                .stream()
                .map(genre -> new GenreDto(genre.getId(),genre.getName()))
                .collect(Collectors.toSet()));
        dto.setLikeUserIds(film.getLikes());
        return dto;
    }

    public static Film mapToFilmModel(FilmDto dto) {
        Film film = new Film();
        film.setId(dto.getId());
        film.setName(dto.getName());
        film.setDescription(dto.getDescription());
        film.setReleaseDate(dto.getReleaseDate());
        film.setDuration(dto.getDuration());

        if (dto.getMpa() != null) {
            MpaRating mpaRating = new MpaRating();
            mpaRating.setMpaRatingId(dto.getMpa().getId());
            film.setMpaRating(mpaRating);
        }
        film.setGenres(dto.getGenres()
                .stream()
                .map(genreDto -> new Genre(genreDto.getId(), genreDto.getName()))
                .collect(Collectors.toSet()));
        film.setLikes(dto.getLikeUserIds());

        return film;
    }
}
