package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;

@RestController
@RequestMapping("/users")
@Slf4j
@RequiredArgsConstructor
public class UserController {

    private final UserStorage userStorage;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<User>> getAll() {
        log.info("Запрос на получение всех пользователей.");

        List<User> users = userStorage.findAll();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<User> create(@Valid @RequestBody User user) {
        log.info("Запрос на создание пользователя: {}", user);

        User createdUser = userStorage.createUser(user);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }


    @PutMapping
    public ResponseEntity<User> update(@Valid @RequestBody User user) {
        log.info("Запрос на обновление пользователя: {}", user);

        User updatedUser = userStorage.updateUser(user);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        log.info("Запрос на получение пользователя по id: {}", id);

        Optional<User> userOptional = userStorage.findUserById(id);
        User user = userOptional.orElseThrow(() -> new NotFoundException("Пользователь с id " + id + " не найден."));
        log.info("Пользователь с id: {} успешно найден", id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public ResponseEntity<User> addFriend(@PathVariable Long id, @PathVariable Long friendId) {
        log.info("Запрос пользователя с id {} на добавления в друзья пользователя с id {}", id, friendId);

        User user = userService.addFriend(id, friendId);
        log.info("Пользователь {} добавил в друзья пользователя {}", id,friendId);
        return new ResponseEntity<>(user, HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public ResponseEntity<User> deleteFriend(@PathVariable("id") Long id, @PathVariable("friendId") Long friendId) {
        log.info("Запрос пользователя с id {} на удаление из друзей пользователя с id {}", id, friendId);

        User user = userService.deleteFriend(id, friendId);
        log.info("Пользователь с id {} удалил из друзей пользователя с id {}", id , friendId);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public ResponseEntity<List<User>> getCommonFriends(@PathVariable Long id, @PathVariable Long otherId) {
        log.info("Запрос на получение общих друзей у пользователей с id {} и {}", id, otherId);

        List<User> commonFriends = userService.findCommonFriends(id, otherId);
        log.info("Получен список общих друзей пользователей с id {} и {}", id, otherId);
        return new ResponseEntity<>(commonFriends, HttpStatus.OK);
    }

    @GetMapping("/{id}/friends")
    public ResponseEntity<List<User>> getFriends(@PathVariable Long id) {
        log.info("Запрос на получение списка друзей пользователя с id: {}", id);

        List<User> friends = userService.getFriends(id);
        log.info("Получен список друзей пользователя с id: {}, количество {}", id, friends.size());
        return new ResponseEntity<>(friends, HttpStatus.OK);
    }
}
