package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserService {

    User addFriend(Long userId, Long friendId);

    User deleteFriend(Long userId, Long friendId);

    List<User> getFriends(Long id);

    List<User> findCommonFriends(Long id, Long otherUserId);
    /*В сервисе фильмов я вынужден проверять Optional<Пользователь>,
     но проверка - приватный метод имплементации интерфейса, которую выносить в контракт сомнительно, а добавлять
     зависимость сервиса фильмов от сервиса пользователей кажется плохим решением.
     Один из вариантов решения в FilmServiceImpl. */

}
