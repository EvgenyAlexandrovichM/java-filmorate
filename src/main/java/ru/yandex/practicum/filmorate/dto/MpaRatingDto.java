package ru.yandex.practicum.filmorate.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class MpaRatingDto {


    private Integer mpaRatingId;


    private String name;
}
