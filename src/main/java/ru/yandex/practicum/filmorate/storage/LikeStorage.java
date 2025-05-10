package ru.yandex.practicum.filmorate.storage;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface LikeStorage {

    void addLike(Long filmId, Long userId);

    void deleteLike(Long filmId, Long userId);

    List<Long> findLikesByFilmId(Long id);

    int countLikesByFilmId(Long id);

    Map<Long, Set<Long>> findAllFilmLikes();
}
