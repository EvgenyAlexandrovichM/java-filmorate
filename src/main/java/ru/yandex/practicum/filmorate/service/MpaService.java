package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.film.MpaRating;

import java.util.List;
import java.util.Optional;

public interface MpaService {

    List<MpaRating> getAllMpaRatings();

    Optional<MpaRating> getMpaRatingById(int id);
}
