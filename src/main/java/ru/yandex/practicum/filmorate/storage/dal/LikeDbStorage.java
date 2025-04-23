package ru.yandex.practicum.filmorate.storage.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.storage.LikeStorage;

import java.util.List;

@Repository
public class LikeDbStorage extends AbstractDbStorage<Long> implements LikeStorage {

    private static final String ADD_LIKE_QUERY = "INSERT INTO likes (film_id, user_id) VALUES (?, ?)";

    private static final String DELETE_LIKE_QUERY = "DELETE FROM likes WHERE film_id = ? AND user_id = ?";

    private static final String FIND_LIKES_BY_FILM_QUERY = "SELECT user_id FROM likes WHERE film_id = ?";

    private static final String COUNT_LIKE_QUERY = "SELECT COUNT(user_id) FROM likes WHERE film_id = ?";

    public LikeDbStorage(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate, ((resultSet, rowNum) -> resultSet.getLong("user_id")));
    }

    @Override
    public void addLike(Long filmId, Long userId) {
        update(ADD_LIKE_QUERY, filmId, userId);
    }

    @Override
    public void removeLike(Long filmId, Long userId) {
        update(DELETE_LIKE_QUERY, filmId, userId);
    }

    @Override
    public List<Long> findLikesByFilmId(Long filmId) {
        return findMany(FIND_LIKES_BY_FILM_QUERY, filmId);
    }

    @Override
    public int countLikesByFilmId(Long filmId) {
        Integer count = jdbcTemplate.queryForObject(COUNT_LIKE_QUERY, Integer.class, filmId);
        if (count == null) {
            throw new NotFoundException("Лайков не найдено.");
        } else {
            return count;
        }
    }
}
