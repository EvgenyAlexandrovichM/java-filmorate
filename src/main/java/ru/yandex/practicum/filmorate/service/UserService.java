package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.user.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    User addFriend(Long userId, Long friendId);

    User removeFriend(Long userId, Long friendId);

    List<User> getFriends(Long id);

    List<User> getCommonFriends(Long userId, Long otherUserId);

    List<User> getAll();

    User createUser(User user);

    User updateUser(User user);

    Optional<User> getUserById(Long id);

}
