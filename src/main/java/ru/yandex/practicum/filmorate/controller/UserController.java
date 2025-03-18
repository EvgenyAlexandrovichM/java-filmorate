package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    private final Map<Long, User> users = new HashMap<>();

    @GetMapping
    public ResponseEntity<List<User>> findAll() {
        log.info("Запрос на получение всех пользователей.");
        return new ResponseEntity<>(new ArrayList<>(users.values()), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<User> create(@Valid @RequestBody User user) {
        log.info("Запрос на создание пользователя: {}", user);

        user.setId(getNextId());
        users.put(user.getId(), user);
        log.info("Создан пользователь с id: {}", user.getId());
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<User> update(@Valid @RequestBody User user) {
        log.info("Запрос на обновление пользователя: {}", user);
        if (user.getId() == null || users.containsKey(user.getId())) {
            log.warn("Пользователь с id {} не найден", user.getId());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        users.put(user.getId(), user);
        log.info("Пользователь обновлен: {}", user);
        return new ResponseEntity<>(user, HttpStatus.OK);
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
