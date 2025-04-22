package ru.yandex.practicum.filmorate.storage.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.user.User;

@Repository
public class FriendshipStorage extends AbstractDbStorage<User> {

    public FriendshipStorage(JdbcTemplate jdbcTemplate, RowMapper<User> mapper) {
        super(jdbcTemplate, mapper);
    }

    private static final String ADD_FRIEND_QUERY = "INSERT INTO friendships (user_id, friend_id) VALUES (?, ?)";
    private static final String REMOVE_FRIEND_QUERY = "DELETE FROM friendships WHERE user_id = ? AND friend_id = ?";
    private static final String GET_FRIENDS_QUERY = "SELECT friend_id FROM friendships WHERE user_id = ?";

    public void addFriend(Long userId, Long friendId) P



}
