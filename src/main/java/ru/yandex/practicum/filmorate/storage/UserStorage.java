package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.user.User;

import java.util.List;
import java.util.Optional;


public interface UserStorage {

    List<User> findAllUsers();

    User createUser(User user);

    User updateUser(User user);

    Optional<User> findUserById(Long id);

    void removeUser(Long id);

    Optional<User> findByEmail(String email);

    Optional<User> findByLogin(String login);

    User addFriend(Long userId, Long friendId);

    User removeFriend(Long userId, Long friendId);

    List<User> findFriends(Long id);

    List<User> findCommonFriends(Long userId, Long otherUserId);
}
