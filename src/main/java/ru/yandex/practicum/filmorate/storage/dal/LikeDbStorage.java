package ru.yandex.practicum.filmorate.storage.dal;

import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.storage.LikeStorage;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
public class LikeDbStorage extends AbstractDbStorage<Long> implements LikeStorage {

    private static final String ADD_LIKE_QUERY = "INSERT INTO likes (film_id, user_id) VALUES (?, ?)";

    private static final String DELETE_LIKE_QUERY = "DELETE FROM likes WHERE film_id = ? AND user_id = ?";

    private static final String FIND_LIKES_BY_FILM_QUERY = "SELECT user_id FROM likes WHERE film_id = ?";

    private static final String COUNT_LIKE_QUERY = "SELECT COUNT(user_id) FROM likes WHERE film_id = ?";

    private static final String FIND_ALL_FILM_LIKES_QUERY = "SELECT film_id, user_id FROM likes";

    public LikeDbStorage(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate, ((resultSet, rowNum) -> resultSet.getLong("user_id")));
    }

    @Override
    public void addLike(Long filmId, Long userId) {
        update(ADD_LIKE_QUERY, filmId, userId);
    }

    @Override
    public void deleteLike(Long filmId, Long userId) {
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

    @Override
    public Map<Long, Set<Long>> findAllFilmLikes() {
        List<Map<String, Object>> rows = findMany(FIND_ALL_FILM_LIKES_QUERY, new ColumnMapRowMapper());
        return rows.stream()
                .collect(Collectors.groupingBy(row -> ((Number) row.get("film_id")).longValue(),
                        Collectors.mapping(row -> ((Number) row.get("user_id")).longValue(),
                                Collectors.toSet()
                        )
                ));
    }
    //Есть вайб, что я зря вообще делал логику с лайками отдельно, ведь это не какая-то сущность, а просто поле фильмов
    // по аналогии с друзьями пользователя, которые я удалил, стоит и это удалить и убрать логику к фильмам
}
