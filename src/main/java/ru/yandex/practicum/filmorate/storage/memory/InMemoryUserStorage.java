package ru.yandex.practicum.filmorate.storage.memory;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.user.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;

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
    public void deleteUser(Long id) {
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
        return users.values()
                .stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst();
    }

    @Override
    public Optional<User> findByLogin(String login) {
        return users.values()
                .stream()
                .filter(user -> user.getLogin().equals(login))
                .findFirst();
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
