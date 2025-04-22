package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.user.User;

import java.util.List;
import java.util.Optional;


public interface UserStorage {

    List<User> findAll();

    User createUser(User user);

    User updateUser(User user);

    Optional<User> findUserById(Long id);

    void deleteUser(Long id);

}
