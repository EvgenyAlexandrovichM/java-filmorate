package ru.yandex.practicum.filmorate.storage.dal;


import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.List;
import java.util.Optional;

@Repository
@Qualifier("filmDbStorage")
public class FilmDbStorage extends AbstractDbStorage<Film> implements FilmStorage {

    private static final String FIND_ALL_QUERY = "SELECT  * FROM films";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM films WHERE film_id = ?";
    private static final String INSERT_QUERY = "INSERT INTO films" +
            "(title, description, release_date, duration, mpa_rating_id) VALUES (?, ?, ?, ?, ?)";
    private static final String UPDATE_QUERY = "UPDATE films SET title = ?, description = ?, release_date = ?," +
            "duration = ?, mpa_rating_id = ? WHERE film_id = ?";
    private static final String DELETE_QUERY = "DELETE FROM films WHERE film_id = ?";
    private static final String FIND_POPULAR_FILMS_QUERY = "SELECT f.* COUNT(l.user_id) AS like_count" +
            "FROM films f" +
            "LEFT JOIN likes l ON f.film_id = l.film_id" +
            "GROUP BY f.film_id" +
            "ORDER BY like_count DESC" +
            "LIMIT ?";

    public FilmDbStorage(JdbcTemplate jdbcTemplate, RowMapper<Film> mapper) {
        super(jdbcTemplate, mapper);
    }

    @Override
    public List<Film> findAll() {
        return findMany(FIND_ALL_QUERY);
    }

    @Override
    public Film createFilm(Film film) {
        long filmId = insert(
                INSERT_QUERY,
                film.getTitle(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpaRating().getMpaRatingId()
        );
        film.setId(filmId);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        update(
                UPDATE_QUERY,
                film.getTitle(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpaRating().getMpaRatingId(),
                film.getId()
        );
        return film;
    }

    @Override
    public Optional<Film> findFilmById(Long id) {
        return findOne(FIND_BY_ID_QUERY, id);
    }

    @Override
    public List<Film> findPopularFilms(int count) {
        return findMany(FIND_POPULAR_FILMS_QUERY, count);
    }

    @Override
    public void deleteFilm(Long id) {
        update(DELETE_QUERY, id);
    }
}