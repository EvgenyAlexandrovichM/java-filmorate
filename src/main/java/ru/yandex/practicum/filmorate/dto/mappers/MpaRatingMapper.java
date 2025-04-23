package ru.yandex.practicum.filmorate.dto.mappers;

import ru.yandex.practicum.filmorate.dto.MpaRatingDto;
import ru.yandex.practicum.filmorate.model.mpa.MpaRating;

public class MpaRatingMapper {

    public static MpaRatingDto mapToMpaRatingDto(MpaRating mpaRating) {
        MpaRatingDto dto = new MpaRatingDto();
        dto.setId(mpaRating.getMpaRatingId());
        dto.setName(mpaRating.getName());
        return dto;
    }

    public static MpaRating mapToMpaRatingModel(MpaRatingDto dto) {
        MpaRating mpaRating = new MpaRating();
        mpaRating.setMpaRatingId(dto.getId());
        mpaRating.setName(dto.getName());
        return mpaRating;
    }
}
