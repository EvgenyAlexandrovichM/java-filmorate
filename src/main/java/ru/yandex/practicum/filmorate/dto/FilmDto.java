package ru.yandex.practicum.filmorate.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

@Data
public class FilmDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    private String title;
    private String description;
    private LocalDate releaseDate;
    private Integer duration;
    private Set<Integer> genreIds;
    private Integer mpaRatingId;
    private Set<Long> likeUserIds;
}
