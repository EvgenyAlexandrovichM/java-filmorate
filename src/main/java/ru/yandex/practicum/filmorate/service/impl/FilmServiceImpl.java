package ru.yandex.practicum.filmorate.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.model.genre.Genre;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.GenreStorage;
import ru.yandex.practicum.filmorate.storage.MpaRatingStorage;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class FilmServiceImpl implements FilmService {

    private final FilmStorage filmStorage;
    private final MpaRatingStorage mpaRatingStorage;
    private final GenreStorage genreStorage;

    public FilmServiceImpl(@Qualifier("filmDbStorage") FilmStorage filmStorage,
                           MpaRatingStorage mpaRatingStorage,
                           GenreStorage genreStorage) {
        this.filmStorage = filmStorage;
        this.mpaRatingStorage = mpaRatingStorage;
        this.genreStorage = genreStorage;
    }

    @Override
    public void addLike(Long filmId, Long userId) {
        log.info("Добавление лайка фильму с id {} от пользователя с id {}", filmId, userId);
        filmStorage.addLike(filmId, userId);
        log.info("Пользователь c id {} поставил лайк фильму c id {}", userId, filmId);
    }

    @Override
    public void deleteLike(Long filmId, Long userId) {
        log.info("Удаление лайка фильму с id {} от пользователя с id {}", filmId, userId);
        filmStorage.deleteLike(filmId, userId);
        log.info("Пользователь с id {} удалил лайк с id фильму {}", userId, filmId);
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
}

//TODO Junit на логику