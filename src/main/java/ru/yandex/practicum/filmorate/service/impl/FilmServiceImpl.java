package ru.yandex.practicum.filmorate.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.model.genre.Genre;
import ru.yandex.practicum.filmorate.model.user.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.GenreStorage;
import ru.yandex.practicum.filmorate.storage.MpaRatingStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class FilmServiceImpl implements FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final MpaRatingStorage mpaRatingStorage;
    private final GenreStorage genreStorage;

    public FilmServiceImpl(@Qualifier("filmDbStorage") FilmStorage filmStorage,
                           @Qualifier("userDbStorage") UserStorage userStorage,
                           MpaRatingStorage mpaRatingStorage,
                           GenreStorage genreStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.mpaRatingStorage = mpaRatingStorage;
        this.genreStorage = genreStorage;
    }

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
    public List<Film> getPopularFilms(int count) {
        log.info("Получение топ {} популярных фильмов по количеству лайков.", count);
        return filmStorage.findPopularFilms(count);
    }

    @Override
    public List<Film> getAll() {
        log.info("Получение всех фильмов");
        return filmStorage.findAllFilms();
    }

    @Override
    public Film createFilm(Film film) {
        log.info("Создание фильма: {}", film);
        getFilmOrThrow(film);
        return filmStorage.createFilm(film);
    }

    @Override
    public Film updateFilm(Film film) {
        log.info("Обновление фильма: {}", film);
        getFilmOrThrow(film);
        return filmStorage.updateFilm(film);
    }

    @Override
    public Optional<Film> getFilmById(Long id) {
        log.info("Получение фильма по id: {}", id);
        return filmStorage.findFilmById(id);
    }

    private Film getFilmOrThrow(Long id) {
        return filmStorage.findFilmById(id)
                .orElseThrow(() -> new NotFoundException("Фильм с id " + id + " не найден."));
    }

    private User getUserOrThrow(Long id) {
        return userStorage.findUserById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь с userId " + id + " не найден."));
    }

    private void getFilmOrThrow(Film film) {
        if (mpaRatingStorage.findMpaRatingById(film.getMpaRating().getMpaRatingId()).isEmpty()) {
            throw new NotFoundException("МРА рейтинг с id " + film.getMpaRating().getMpaRatingId() + " не найден.");
        }
        if (film.getGenres() != null && !film.getGenres().isEmpty()) {
            for (Genre genre : film.getGenres()) {
                if (genreStorage.findGenreById(genre.getId()).isEmpty()) {
                    throw new NotFoundException("Жанр с id " + genre.getId() + " не найден.");
                }
            }
        }
    }
    //TODO Junit на логику
}