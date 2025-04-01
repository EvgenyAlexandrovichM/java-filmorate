package ru.yandex.practicum.filmorate.storage.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {

    private final Map<Long, User> users = new HashMap<>();

    @Override
    public List<User> findAll() {
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
            log.warn("Пользователь с id {} не найден", user.getId());
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
