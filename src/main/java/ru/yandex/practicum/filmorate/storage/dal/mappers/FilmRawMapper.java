package ru.yandex.practicum.filmorate.storage.dal.mappers;

import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.model.film.Genre;
import ru.yandex.practicum.filmorate.model.film.MpaRating;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FilmRawMapper implements RowMapper<Film> {

    //Почитал на форумах, что принято делать интерфейсами для гибкости с дефолтным методом, пока оставляю классом)
    @Override
    public Film mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Film film = new Film();
        film.setId(resultSet.getLong("film_id"));
        film.setTitle(resultSet.getString("title"));
        film.setDescription(resultSet.getString("description"));

        LocalDate releaseDate = resultSet.getDate("release_date").toLocalDate();
        film.setReleaseDate(releaseDate);
        film.setDuration(resultSet.getInt("duration"));

        MpaRating mpaRating = new MpaRating();
        mpaRating.setMpaRatingId(resultSet.getInt("mpa_rating_id"));
        mpaRating.setName(resultSet.getString("name"));
        film.setMpaRating(mpaRating);

        Set<Genre> genres = new HashSet<>();
        Genre genre = new Genre();
        genre.setGenreId(resultSet.getInt("genre_id"));
        genre.setName(resultSet.getString("name"));
        genres.add(genre);
        film.setGenre(genres);

        return film;
    }
}
