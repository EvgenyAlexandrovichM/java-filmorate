package ru.yandex.practicum.filmorate.model.film;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ru.yandex.practicum.filmorate.model.mpa.MpaRating;
import ru.yandex.practicum.filmorate.model.genre.Genre;
import ru.yandex.practicum.filmorate.validators.ValidFilmReleaseDate;


import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class Film {
    private Long id;

    @NotBlank(message = "Название фильма не может быть пустым.")
    private String name;

    @Size(max = 200, message = "Описание фильма не может превышать 200 символов.")
    private String description;

    @NotNull(message = "Дата релиза не может быть пустой.")
    @ValidFilmReleaseDate
    private LocalDate releaseDate;

    @Positive(message = "Продолжительность фильма должна быть положительным числом.")
    @NotNull(message = "Длительность не может быть пустой.")
    private Integer duration;

    private Set<Long> likes = new HashSet<>();

    private Set<Genre> genres = new HashSet<>();

    private MpaRating mpaRating;

}
