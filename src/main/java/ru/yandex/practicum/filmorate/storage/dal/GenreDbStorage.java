package ru.yandex.practicum.filmorate.storage.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.film.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.List;
import java.util.Optional;

@Repository
public class GenreDbStorage extends AbstractDbStorage<Genre> implements GenreStorage {

    private static final String FIND_ALL_GENRES_QUERY = "SELECT * FROM genres";
    private static final String FIND_GENRE_BY_ID_QUERY = "SELECT * FROM genre WHERE genre_id = ?";
    private static final String FIND_GENRES_BY_FILM_ID_QUERY =
            "SELECT g genre_id, g.name FROM genres g" +
                    "JOIN film_genres fg ON g.genre_id = fg.genre_id" +
                    "WHERE fg.film_id = ?";

    public GenreDbStorage(JdbcTemplate jdbcTemplate, RowMapper<Genre> mapper) {
        super(jdbcTemplate, mapper);
    }

    @Override
    public List<Genre> findAllGenres() {
        return findMany(FIND_ALL_GENRES_QUERY);
    }

    @Override
    public Optional<Genre> findGenreById(int id) {
       return findOne(FIND_GENRE_BY_ID_QUERY, id);
    }

    @Override
    public List<Genre> findGenresByFilmId(Long filmId) {
       return findMany(FIND_GENRES_BY_FILM_ID_QUERY, filmId);
    }
}
