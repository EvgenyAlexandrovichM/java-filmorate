package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.UserDto;
import ru.yandex.practicum.filmorate.dto.mappers.UserMapper;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.user.User;
import ru.yandex.practicum.filmorate.service.UserService;


import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
@Slf4j
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<UserDto>> getAll() {
        log.info("Запрос на получение всех пользователей.");

        List<UserDto> users = userService.getAll()
                .stream()
                .map(UserMapper::mapToUserDto)
                .collect(Collectors.toList());
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<UserDto> create(@Valid @RequestBody UserDto userDto) {
        log.info("Запрос на создание пользователя: {}", userDto);

        User user = UserMapper.mapToUserModel(userDto);
        User createdUser = userService.createUser(user);
        return new ResponseEntity<>(UserMapper.mapToUserDto(createdUser), HttpStatus.CREATED);
    }


    @PutMapping
    public ResponseEntity<UserDto> update(@Valid @RequestBody UserDto userDto) {
        log.info("Запрос на обновление пользователя: {}", userDto);

        User user = UserMapper.mapToUserModel(userDto);
        User updatedUser = userService.updateUser(user);
        return new ResponseEntity<>(UserMapper.mapToUserDto(updatedUser), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        log.info("Запрос на получение пользователя по id: {}", id);

        Optional<User> userOptional = userService.getUserById(id);
        User user = userOptional.orElseThrow(() -> new NotFoundException("Пользователь с id " + id + " не найден."));
        log.info("Пользователь с id: {} успешно найден", id);
        return new ResponseEntity<>(UserMapper.mapToUserDto(user), HttpStatus.OK);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public ResponseEntity<UserDto> addFriend(@PathVariable Long id, @PathVariable Long friendId) {
        log.info("Запрос пользователя с id {} на добавления в друзья пользователя с id {}", id, friendId);

        User user = userService.addFriend(id, friendId);
        log.info("Пользователь {} добавил в друзья пользователя {}", id, friendId);
        return new ResponseEntity<>(UserMapper.mapToUserDto(user), HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public ResponseEntity<UserDto> deleteFriend(@PathVariable("id") Long id, @PathVariable("friendId") Long friendId) {
        log.info("Запрос пользователя с id {} на удаление из друзей пользователя с id {}", id, friendId);

        User user = userService.deleteFriend(id, friendId);
        log.info("Пользователь с id {} удалил из друзей пользователя с id {}", id, friendId);
        return new ResponseEntity<>(UserMapper.mapToUserDto(user), HttpStatus.OK);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public ResponseEntity<List<UserDto>> getCommonFriends(@PathVariable Long id, @PathVariable Long otherId) {
        log.info("Запрос на получение общих друзей у пользователей с id {} и {}", id, otherId);

        List<UserDto> commonFriends = userService.findCommonFriends(id, otherId)
                .stream()
                .map(UserMapper::mapToUserDto)
                .collect(Collectors.toList());
        log.info("Получен список общих друзей пользователей с id {} и {}", id, otherId);
        return new ResponseEntity<>(commonFriends, HttpStatus.OK);
    }

    @GetMapping("/{id}/friends")
    public ResponseEntity<List<UserDto>> getFriends(@PathVariable Long id) {
        log.info("Запрос на получение списка друзей пользователя с id: {}", id);

        List<UserDto> friends = userService.getFriends(id)
                .stream()
                .map(UserMapper::mapToUserDto)
                .collect(Collectors.toList());
        log.info("Получен список друзей пользователя с id: {}, количество {}", id, friends.size());
        return new ResponseEntity<>(friends, HttpStatus.OK);
    }
}
