package ru.yandex.practicum.filmorate.storage;

import java.util.List;

public interface LikeStorage {

    void addLike(Long filmId, Long userId);

    void deleteLike(Long filmId, Long userId);

    List<Long> findLikesByFilmId(Long filmId);

    int countLikesByFilmId(Long filmId);
}
