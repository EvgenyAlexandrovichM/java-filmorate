package ru.yandex.practicum.filmorate.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.user.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserStorage userStorage;

    @Override
    public User addFriend(Long userId, Long friendId) {
        log.info("Добавление в друзья пользователей: {}, {}", userId, friendId);

        User user = getUserOrThrow(userId);
        User friend = getUserOrThrow(friendId);

        user.getFriends().add(friendId);
        friend.getFriends().add(userId);

        userStorage.updateUser(user);
        userStorage.updateUser(friend);
        log.info("Пользователи {} и {} теперь друзья", user, friend);

        return user;
    }

    @Override
    public User deleteFriend(Long userId, Long friendId) {
        log.info("Удаление из друзей пользователей {}, {}", userId, friendId);

        User user = getUserOrThrow(userId);
        User friend = getUserOrThrow(friendId);

        user.getFriends().remove(friendId);
        friend.getFriends().remove(userId);

        userStorage.updateUser(user);
        userStorage.updateUser(friend);
        log.info("Пользователи {} и {} теперь не друзья", user, friend);

        return user;
    }

    @Override
    public List<User> getFriends(Long id) {
        log.info("Получение списка друзей пользователя с id: {}", id);

        User user = getUserOrThrow(id);
        log.debug("Пользователь с id {} найден: {}", id, user);

        List<Long> friendsId = new ArrayList<>(user.getFriends());
        return friendsId.stream()
                .map(this::getUserOrThrow)
                .collect(Collectors.toList());
    }

    @Override
    public List<User> findCommonFriends(Long userId, Long otherUserId) {
        log.info("Получение списка друзей пользователя: {}", userId);
        User user = getUserOrThrow(userId);
        User otherUser = getUserOrThrow(otherUserId);

        Set<Long> userFriends = user.getFriends();
        Set<Long> otherUserFriends = otherUser.getFriends();

        return userFriends.stream()
                .filter(otherUserFriends::contains)
                .map(this::getUserOrThrow)
                .collect(Collectors.toList());

    }

    @Override
    public List<User> getAll() {
        log.info("Получение всех пользователей");
        return userStorage.findAll();
    }

    @Override
    public User createUser(User user) {
        log.info("Создание пользователя: {}", user);
        return userStorage.createUser(user);
    }

    @Override
    public User updateUser(User user) {
        log.info("Обновление пользователя: {}", user);
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
