package ru.yandex.practicum.filmorate.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.BadRequestException;
import ru.yandex.practicum.filmorate.exception.DuplicatedDataException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.user.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;
import java.util.Optional;


@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserStorage userStorage;

    public UserServiceImpl(@Qualifier("userDbStorage") UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    @Override
    public User addFriend(Long userId, Long friendId) {
        log.info("Удаление из друзей пользователя {}, {}", userId, friendId);
        return userStorage.addFriend(userId, friendId);
    }

    @Override
    public User removeFriend(Long userId, Long friendId) {
        log.info("Удаление из друзей пользователей {}, {}", userId, friendId);
        return userStorage.removeFriend(userId, friendId);
    }

    @Override
    public List<User> getFriends(Long id) {
        log.info("Получение списка друзей пользователя с id: {}", id);
        getUserOrThrow(id);
        List<User> friends = userStorage.findFriends(id);
        if (friends.isEmpty()) {
            log.info("У пользователя с id {} пока нет друзей", id);
        }
        return friends;
    }

    @Override
    public List<User> getCommonFriends(Long userId, Long otherUserId) {
        log.info("Получение списка друзей пользователя: {}", userId);
        return userStorage.findCommonFriends(userId, otherUserId);
    }

    @Override
    public List<User> getAll() {
        log.info("Получение всех пользователей");
        return userStorage.findAllUsers();
    }

    @Override
    public User createUser(User user) {
        log.info("Создание пользователя: {}", user);
        if (userStorage.findByEmail(user.getEmail()).isPresent()) {
            throw new DuplicatedDataException("Пользователь с таким email уже существует: " + user.getEmail());
        }

        if (userStorage.findByLogin(user.getLogin()).isPresent()) {
            throw new DuplicatedDataException("Пользователь с таким login уже существует: " + user.getLogin());
        }
        return userStorage.createUser(user);
    }

    @Override
    public User updateUser(User user) {
        log.info("Обновление пользователя: {}", user);
        if (user.getId() == null) {
            throw new BadRequestException("ID обязателен для обновления.");
        }
        return userStorage.updateUser(user);
    }

    @Override
    public Optional<User> getUserById(Long id) {
        log.info("Получение пользователя по id: {}", id);
        return userStorage.findUserById(id);
    }

    private User getUserOrThrow(Long id) {
        log.info("Поиск пользователя с id: {}", id);
        return userStorage.findUserById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь с userId " + id + " не найден."));
    }
    //TODO Junit на логику
}