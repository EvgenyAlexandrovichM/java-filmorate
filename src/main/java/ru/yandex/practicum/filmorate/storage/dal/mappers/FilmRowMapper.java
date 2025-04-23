package ru.yandex.practicum.filmorate.storage.dal.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.model.mpa.MpaRating;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class FilmRowMapper implements RowMapper<Film> {

    //Почитал на форумах, что принято делать интерфейсами для гибкости с дефолтным методом, пока оставляю классом)
    @Override
    public Film mapRow(ResultSet resultSet, int rowNum) throws SQLException {

        Film film = new Film();
        film.setId(resultSet.getLong("film_id"));
        film.setName(resultSet.getString("name"));
        film.setDescription(resultSet.getString("description"));
        film.setReleaseDate(resultSet.getDate("release_date").toLocalDate());
        film.setDuration(resultSet.getInt("duration"));
        MpaRating mpaRating = new MpaRating();
        mpaRating.setMpaRatingId(resultSet.getInt("mpa_rating_id"));
        mpaRating.setName(resultSet.getString("mpa_name"));

        film.setMpaRating(mpaRating);
        return film;
    }
}
