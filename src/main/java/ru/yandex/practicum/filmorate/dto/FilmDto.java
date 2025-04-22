package ru.yandex.practicum.filmorate.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

@Data
public class FilmDto {

    private Long id;
    private String title;
    private String description;
    private LocalDate releaseDate;
    private Integer duration;
    private Set<Integer> genreIds;
    private Integer mpaRatingId;
    private Set<Long> likeUserIds;
}
