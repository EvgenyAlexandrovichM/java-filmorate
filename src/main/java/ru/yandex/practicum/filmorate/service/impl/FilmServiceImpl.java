package ru.yandex.practicum.filmorate.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class FilmServiceImpl implements FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Override
    public void addLike(Long filmId, Long userId) {
        log.info("Добавление лайка фильму с id {} от пользователя с id {}", filmId, userId);

        Film film = getFilmOrThrow(filmId);
        User user = getUserOrThrow(userId);

        film.getLikes().add(userId);
        filmStorage.updateFilm(film);
        log.info("Пользователь {} поставил лайк фильму {}", user, film);
    }

    @Override
    public void deleteLike(Long filmId, Long userId) {
        log.info("Удаление лайка фильму с id {} от пользователя с id {}", filmId, userId);

        Film film = getFilmOrThrow(filmId);
        User user = getUserOrThrow(userId);

        film.getLikes().remove(userId);
        filmStorage.updateFilm(film);
        log.info("Пользователь {} удалил лайк фильму {}", user, film);
    }

    @Override
    public List<Film> findPopularFilms(int count) {
        log.info("Получение топ 10 популярных фильмов по количеству лайков.");

        return filmStorage.findAll().stream()
                .sorted((f1, f2) -> f2.getLikes().size() - f1.getLikes().size())
                .limit(count)
                .collect(Collectors.toList());
    }

    private Film getFilmOrThrow(Long id) {
        return filmStorage.findFilmById(id)
                .orElseThrow(() -> new NotFoundException("Фильм с id " + id + " не найден."));
    }

    private User getUserOrThrow(Long id) {
        return userStorage.findUserById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь с userId " + id + " не найден."));
    } /* дублированный метод из сервиса пользователей, который является приватным, но необходим здесь (найти вариант
     как использовать этот метод, не добавляя сюда зависимость от пользовательского сервиса.
     Возможно в реализацию пользовательского хранилища имеет смысл вынести этот метод, а в контракт добавить что-то
     типа метода
     boolean isFriend(Long id, Long friendId) {
     User user = getUserOrThrow(userId);
     return user.getFriends().contains(friendId); */
    //TODO Junit на логику
}
