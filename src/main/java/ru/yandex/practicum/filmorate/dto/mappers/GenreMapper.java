package ru.yandex.practicum.filmorate.dto.mappers;

import ru.yandex.practicum.filmorate.dto.GenreDto;
import ru.yandex.practicum.filmorate.model.film.Genre;

public class GenreMapper {

    public static GenreDto mapToGenreDto(Genre genre) {
        GenreDto dto = new GenreDto();
        dto.setGenreId(genre.getGenreId());
        dto.setName(genre.getName());
        return dto;
    }

    public static Genre mapToGenreModel(GenreDto dto) {
        Genre genre = new Genre();
        genre.setGenreId(dto.getGenreId());
        genre.setName(dto.getName());
        return genre;
    }
}
