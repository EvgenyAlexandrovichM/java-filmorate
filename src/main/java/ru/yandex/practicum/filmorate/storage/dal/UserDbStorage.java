package ru.yandex.practicum.filmorate.storage.dal;



import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.BadRequestException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.user.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;
import java.util.Optional;

@Repository
@Qualifier("userDbStorage")
@Slf4j
public class UserDbStorage extends AbstractDbStorage<User> implements UserStorage {
    private static final String FIND_ALL_QUERY = "SELECT * FROM users";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM users WHERE user_id = ?";
    private static final String INSERT_QUERY = "INSERT INTO users(email, login, name, birthday) VALUES (?, ?, ?, ?)";
    private static final String UPDATE_QUERY = "UPDATE users SET email = ?, login = ?, name = ?, birthday = ? WHERE user_id = ?";
    private static final String DELETE_QUERY = "DELETE FROM users WHERE user_id = ?";
    private static final String FIND_BY_EMAIL_QUERY = "SELECT * FROM users WHERE email = ?";
    private static final String FIND_BY_LOGIN_QUERY = "SELECT * FROM users WHERE login = ?";
    private static final String ADD_FRIEND_QUERY = "INSERT INTO friendships(user_id, friend_id) VALUES (?, ?)";
    private static final String DELETE_FRIEND_QUERY = "DELETE FROM friendships WHERE user_id = ? AND friend_id = ?";
    private static final String FIND_FRIENDS_QUERY = "SELECT u.* FROM users u " +
            "INNER JOIN friendships f ON u.user_id = f.friend_id " +
            "WHERE f.user_id = ?";
    private static final String FIND_COMMON_FRIENDS_QUERY = "SELECT u.* FROM users u " +
            "INNER JOIN friendships f1 ON u.user_id = f1.friend_id " +
            "INNER JOIN friendships f2 ON u.user_id = f2.friend_id " +
            "WHERE f1.user_id = ? AND f2.user_id = ?";

    public UserDbStorage(JdbcTemplate jdbcTemplate, RowMapper<User> mapper) {
        super(jdbcTemplate, mapper);
    }

    @Override
    public List<User> findAllUsers() {
        log.info("Поиск всех пользователей");

        return findMany(FIND_ALL_QUERY);
    }

    @Override
    public User createUser(User user) {
        log.info("Создание пользователя: {}", user);

        long id = insert(
                INSERT_QUERY,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday()
        );
        user.setId(id);

        log.info("Пользователь {} создан", user);

        return user;
    }

    @Override
    public User updateUser(User user) {
        log.info("Обновление данных пользователя {}", user);

        Optional<User> existingUser = findUserById(user.getId());
        if (existingUser.isEmpty()) {
            throw new NotFoundException("Пользователь с ID " + user.getId() + " не найден");
        }

        if (user.getEmail() == null || user.getLogin() == null || user.getId() == null) {
            throw new BadRequestException("Некорректные данные для обновления.");
        }

        update(
                UPDATE_QUERY,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId()
        );

        log.info("Пользователь {} обновлен", user);

        return user;
    }

    @Override
    public Optional<User> findUserById(Long id) {
        log.info("Поиск пользователя по ID: {}", id);

        return findOne(FIND_BY_ID_QUERY, id);
    }

    @Override
    public void removeUser(Long id) {
        log.info("Удаление пользователя с ID: {}", id);

        update(DELETE_QUERY, id);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        log.info("Поиск пользователя по email {}", email);

        return findOne(FIND_BY_EMAIL_QUERY, email);
    }

    @Override
    public Optional<User> findByLogin(String login) {
        log.info("Поиск пользователя по login {}", login);

        return findOne(FIND_BY_LOGIN_QUERY, login);
    }

    @Override
    public User addFriend(Long userId, Long friendId) {
        log.info("Добавление в друзья пользователей: {}, {}", userId, friendId);

        User user = getUserOrThrow(userId);
        User friend = getUserOrThrow(friendId);
        update(ADD_FRIEND_QUERY, userId, friendId);

        log.info("Пользователи {} и {} теперь друзья", user, friend);

        return user;
    }

    @Override
    public User removeFriend(Long userId, Long friendId) {
        log.info("Удаление из друзей пользователей {}, {}", userId, friendId);

        User user = getUserOrThrow(userId);
        User friend = getUserOrThrow(friendId);
        updateIgnoreResult(DELETE_FRIEND_QUERY, userId, friendId);

        log.info("Пользователи {} и {} теперь не друзья", user, friend);

        return user;
    }

    @Override
    public List<User> findFriends(Long id) {
        log.info("Получение списка друзей пользователя с id: {}", id);

        return findMany(FIND_FRIENDS_QUERY, id);

    }

    @Override
    public List<User> findCommonFriends(Long userId, Long otherUserId) {
        log.info("Получение списка друзей пользователя: {}", userId);

        return findMany(FIND_COMMON_FRIENDS_QUERY, userId, otherUserId);
    }

    private User getUserOrThrow(Long id) {
        log.info("Поиск пользователя с id: {}", id);

        return findUserById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь с userId " + id + " не найден."));
    }
}
