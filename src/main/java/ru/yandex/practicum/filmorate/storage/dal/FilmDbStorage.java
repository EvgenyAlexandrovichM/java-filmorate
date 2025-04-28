package ru.yandex.practicum.filmorate.storage.dal;


import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.model.genre.Genre;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Repository
@Qualifier("filmDbStorage")
public class FilmDbStorage extends AbstractDbStorage<Film> implements FilmStorage {

    private static final String FIND_ALL_FILM_QUERY = "SELECT f.*, m.name AS mpa_name " +
            "FROM films f " +
            "JOIN mpa_ratings m ON f.mpa_rating_id = m.mpa_rating_id";

    private static final String FIND_FILM_BY_ID_QUERY = "SELECT f.*, m.name AS mpa_name " +
            "FROM films f " +
            "JOIN mpa_ratings m ON f.mpa_rating_id = m.mpa_rating_id " +
            "WHERE f.film_id = ?";

    private static final String INSERT_FILM_QUERY = "INSERT INTO films" +
            "(name, description, release_date, duration, mpa_rating_id) VALUES (?, ?, ?, ?, ?)";

    private static final String UPDATE_FILM_QUERY = "UPDATE films SET name = ?, description = ?, release_date = ?," +
            "duration = ?, mpa_rating_id = ? WHERE film_id = ?";

    private static final String DELETE_FILM_QUERY = "DELETE FROM films WHERE film_id = ?";

    private static final String FIND_POPULAR_FILMS_QUERY = "SELECT f.*, m.name AS mpa_name, " +
            "COUNT(l.user_id) AS like_count " +
            "FROM films f " +
            "JOIN mpa_ratings m ON f.mpa_rating_id = m.mpa_rating_id " +
            "LEFT JOIN likes l ON f.film_id = l.film_id " +
            "GROUP BY f.film_id, m.name " +
            "ORDER BY like_count DESC, f.film_id ASC " +
            "LIMIT ?";

    private static final String INSERT_FILM_GENRES_QUERY = "INSERT INTO film_genres (film_id, genre_id) VALUES (?, ?)";

    private static final String FIND_GENRES_BY_FILM_ID_QUERY = "SELECT g.genre_id, g.name " +
            "FROM genres g " +
            "JOIN film_genres fg ON g.genre_id = fg.genre_id " +
            "WHERE fg.film_id = ?";

    private final RowMapper<Genre> genreRowMapper;

    public FilmDbStorage(JdbcTemplate jdbcTemplate, RowMapper<Film> filmRowMapper, RowMapper<Genre> genreRowMapper) {
        super(jdbcTemplate, filmRowMapper);
        this.genreRowMapper = genreRowMapper;
    }

    @Override
    public List<Film> findAllFilms() {
        return findMany(FIND_ALL_FILM_QUERY);
    }

    @Override
    public Film createFilm(Film film) {
        long filmId = insert(
                INSERT_FILM_QUERY,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpaRating().getMpaRatingId()
        );
        film.setId(filmId);

        if (film.getGenres() != null && !film.getGenres().isEmpty()) {
            for (Genre genre : film.getGenres()) {
                update(INSERT_FILM_GENRES_QUERY, filmId, genre.getId());
            }
        }

        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        update(
                UPDATE_FILM_QUERY,
                film.getName(),
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
        Optional<Film> optionalFilm = findOne(FIND_FILM_BY_ID_QUERY, id);
        if (optionalFilm.isPresent()) {
            Film film = optionalFilm.get();

            List<Genre> genres = jdbcTemplate.query(FIND_GENRES_BY_FILM_ID_QUERY, genreRowMapper, id);
            film.setGenres(new HashSet<>(genres));
            return Optional.of(film);
        }
        return Optional.empty();
    }

    @Override
    public List<Film> findPopularFilms(int count) {
        return findMany(FIND_POPULAR_FILMS_QUERY, count);
    }

    @Override
    public void removeFilm(Long id) {
        update(DELETE_FILM_QUERY, id);
    }
}