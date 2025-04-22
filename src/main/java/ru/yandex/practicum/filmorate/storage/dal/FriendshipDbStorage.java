package ru.yandex.practicum.filmorate.storage.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.user.User;
import ru.yandex.practicum.filmorate.storage.FriendStorage;


import java.util.List;

@Repository
public class FriendshipDbStorage extends AbstractDbStorage<User> implements FriendStorage {

    private static final String ADD_FRIEND_QUERY = "INSERT INTO friendships (user_id, friend_id) VALUES (?, ?)";
    private static final String DELETE_FRIEND_QUERY = "DELETE FROM friendships WHERE user_id = ? AND friend_id = ?";
    private static final String GET_FRIENDS_QUERY =
            "SELECT u.* FROM users u" +
                    "JOIN friendships f ON u.user_id = f.friend_id" +
                    "WHERE f.user_id = ?";
    private static final String FIND_COMMON_FRIENDS_QUERY =
            "SELECT u.* FROM users u" +
                    "JOIN friendships f1 ON u.user_id = f1.friend_id" +
                    "JOIN friendships f2 ON u.user_id = f2.friend_id" +
                    "WHERE f1.user_id = ? AND f2.user_id = ?";

    public FriendshipDbStorage(JdbcTemplate jdbcTemplate, RowMapper<User> mapper) {
        super(jdbcTemplate, mapper);
    }

    @Override
    public void addFriend(Long userId, Long friendId) {
        update(ADD_FRIEND_QUERY, userId, friendId);
        update(ADD_FRIEND_QUERY, friendId, userId);
    }

    @Override
    public void deleteFriend(Long userId, Long friendId) {
        update(DELETE_FRIEND_QUERY, userId, friendId);
        update(DELETE_FRIEND_QUERY, friendId, userId);
    }

    @Override
    public List<User> getFriends(Long userId) {
        return findMany(GET_FRIENDS_QUERY, userId);
    }

    @Override
    public List<User> findCommonFriends(Long userId, Long otherUserId) {
        return findMany(FIND_COMMON_FRIENDS_QUERY, userId, otherUserId);
    }

}