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
        try {
            if (user.getName() == null || user.getName().isEmpty()) {
                user.setName(user.getLogin());
                log.info("Имя пользователя установлено по умолчанию: {}", user.getName());
            }
            user.setId(getNextId());
            users.put(user.getId(), user);
            log.info("Создан пользователь с id: {}", user.getId());
            return new ResponseEntity<>(user, HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Произошла ошибка при создании пользователя: {}", e.getMessage());
            return new ResponseEntity<>(user, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping
    public ResponseEntity<User> update(@Valid @RequestBody User user) {
        log.info("Запрос на обновление пользователя: {}", user);
        try {
            if (user.getId() == null || !users.containsKey(user.getId())) {
                log.warn("Пользователь с id {} не найден", user.getId());
                return new ResponseEntity<>(user, HttpStatus.NOT_FOUND);
            }

            users.put(user.getId(), user);
            log.info("Пользователь обновлен: {}", user);
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Произошла ошибка при обновлении пользователя: {}", e.getMessage());
            return new ResponseEntity<>(user, HttpStatus.INTERNAL_SERVER_ERROR);
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
