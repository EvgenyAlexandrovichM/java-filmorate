package ru.yandex.practicum.filmorate.storage.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.film.MpaRating;
import ru.yandex.practicum.filmorate.storage.MpaRatingStorage;

import java.util.List;
import java.util.Optional;

@Repository
public class MpaRatingDbStorage extends AbstractDbStorage<MpaRating> implements MpaRatingStorage {

    private static final String FIND_ALL_MPA_RATINGS_QUERY = "SELECT * FROM mpa_ratings";
    private static final String FIND_MPA_RATING_BY_ID_QUERY = "SELECT * FROM mpa_ratings WHERE mpa_rating_id = ?";

    public MpaRatingDbStorage(JdbcTemplate jdbcTemplate, RowMapper<MpaRating> mapper) {
        super(jdbcTemplate, mapper);
    }

    @Override
    public List<MpaRating> findAllMpaRatings() {
        return findMany(FIND_ALL_MPA_RATINGS_QUERY);
    }

    @Override
    public Optional<MpaRating> findMpaRatingById(int id) {
        return findOne(FIND_MPA_RATING_BY_ID_QUERY, id);
    }
}
