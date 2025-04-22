package ru.yandex.practicum.filmorate.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class MpaRatingDto {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer mpaRatingId;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String name;
}
