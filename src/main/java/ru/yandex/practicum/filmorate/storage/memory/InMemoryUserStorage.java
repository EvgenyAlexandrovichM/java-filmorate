package ru.yandex.practicum.filmorate.storage.memory;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.user.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
@Qualifier("inMemoryUserStorage")
public class InMemoryUserStorage implements UserStorage {

    private final Map<Long, User> users = new HashMap<>();

    @Override
    public List<User> findAllUsers() {
        log.info("Список всех пользователей");

        return new ArrayList<>(users.values());
    }

    @Override
    public User createUser(User user) {
        log.info("Создание пользователя {}", user);

        nameValidation(user);
        user.setId(getNextId());
        users.put(user.getId(), user);

        log.info("Пользователь создан с id {}", user.getId());
        return user;
    }

    @Override
    public User updateUser(User user) {
        log.info("Обновление пользователя: {}", user);

        if (user.getId() == null || !users.containsKey(user.getId())) {
            log.warn("Пользователь с ID {} не найден", user.getId());
            throw new NotFoundException("Пользователь с id " + user.getId() + " не найден");
        }

        nameValidation(user);
        users.put(user.getId(), user);

        log.info("Пользователь {} обновлен", user);
        return user;
    }

    @Override
    public Optional<User> findUserById(Long id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public void removeUser(Long id) {
        log.info("Удаление пользователя c ID: {}", id);

        if (id == null || !users.containsKey(id)) {
            log.warn("Пользователя с ID {} не существует", id);
            throw new NotFoundException("Пользователь с id " + id + " не найден.");
        }

        for (User user : users.values()) {
            user.getFriends().remove(id);
        }
        users.remove(id);

        log.info("Пользователь с ID: {} удален", id);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        log.info("Поиск пользователя по email: {}", email);

        return users.values()
                .stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst();
    }

    @Override
    public Optional<User> findByLogin(String login) {
        log.info("Поиск пользователя по login: {}", login);

        return users.values()
                .stream()
                .filter(user -> user.getLogin().equals(login))
                .findFirst();
    }

    @Override
    public User addFriend(Long userId, Long friendId) {
        log.info("Добавление в друзья пользователей: {}, {}", userId, friendId);

        User user = getUserOrThrow(userId);
        User friend = getUserOrThrow(friendId);

        user.getFriends().add(friendId);
        friend.getFriends().add(userId);

        updateUser(user);
        updateUser(friend);

        log.info("Пользователи {} и {} теперь друзья", user, friend);

        return user;
    }

    @Override
    public User removeFriend(Long userId, Long friendId) {
        log.info("Удаление из друзей пользователей {}, {}", userId, friendId);

        User user = getUserOrThrow(userId);
        User friend = getUserOrThrow(friendId);

        user.getFriends().remove(friendId);
        friend.getFriends().remove(userId);

        updateUser(user);
        updateUser(friend);

        log.info("Пользователи {} и {} теперь не друзья", user, friend);

        return user;
    }

    @Override
    public List<User> findFriends(Long id) {
        log.info("Получение списка друзей пользователя с id: {}", id);

        User user = getUserOrThrow(id);

        log.debug("Пользователь с id {} найден: {}", user, user);

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

    private User getUserOrThrow(Long id) {
        log.info("Поиск пользователя с id: {}", id);

        return findUserById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь с userId " + id + " не найден."));
    }

    private void nameValidation(User user) {
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());

            log.info("Имя пользователя установлено по умолчанию: {}", user.getName());
        }
    }

    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
