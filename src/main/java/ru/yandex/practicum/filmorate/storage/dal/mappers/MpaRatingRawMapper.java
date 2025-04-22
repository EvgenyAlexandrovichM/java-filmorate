package ru.yandex.practicum.filmorate.storage.dal.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.film.MpaRating;


import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class MpaRatingRawMapper implements RowMapper<MpaRating> {
    @Override
    public MpaRating mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        MpaRating mpaRating = new MpaRating();
        mpaRating.setMpaRatingId(resultSet.getInt("mpa_rating_id"));
        mpaRating.setName(resultSet.getString("name"));
        return mpaRating;
    }
}
