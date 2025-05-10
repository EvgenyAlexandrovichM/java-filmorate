package ru.yandex.practicum.filmorate.dto.mappers;

import ru.yandex.practicum.filmorate.dto.GenreDto;
import ru.yandex.practicum.filmorate.model.genre.Genre;

public class GenreMapper {

    public static GenreDto mapToGenreDto(Genre genre) {
       return new GenreDto(genre.getId(), genre.getName());
    }

    public static Genre mapToGenreModel(GenreDto dto) {
        return new Genre(dto.getId(), dto.getName());
    }
}
