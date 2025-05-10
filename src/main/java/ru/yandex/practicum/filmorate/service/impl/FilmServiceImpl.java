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
import ru.yandex.practicum.filmorate.storage.LikeStorage;
import ru.yandex.practicum.filmorate.storage.MpaRatingStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilmServiceImpl implements FilmService {

    private final FilmStorage filmStorage;
    private final MpaRatingStorage mpaRatingStorage;
    private final GenreStorage genreStorage;
    private final LikeStorage likeStorage;

    public FilmServiceImpl(@Qualifier("filmDbStorage") FilmStorage filmStorage,
                           MpaRatingStorage mpaRatingStorage,
                           GenreStorage genreStorage, LikeStorage likeStorage) {
        this.filmStorage = filmStorage;
        this.mpaRatingStorage = mpaRatingStorage;
        this.genreStorage = genreStorage;
        this.likeStorage = likeStorage;
    }

    @Override
    public void addLike(Long filmId, Long userId) {
        log.info("Добавление лайка фильму с id {} от пользователя с id {}", filmId, userId);
        likeStorage.addLike(filmId, userId);
        log.info("Пользователь c id {} поставил лайк фильму c id {}", userId, filmId);
    }

    @Override
    public void deleteLike(Long filmId, Long userId) {
        log.info("Удаление лайка фильму с id {} от пользователя с id {}", filmId, userId);
        likeStorage.deleteLike(filmId, userId);
        log.info("Пользователь с id {} удалил лайк с id фильму {}", userId, filmId);
    }

    @Override
    public List<Film> getPopularFilms(int count) {
        log.info("Получение топ {} популярных фильмов по количеству лайков.", count);
        List<Film> films = filmStorage.findPopularFilms(count);
        return enrichFilmsWithGenresAndLikes(films);
    }

    @Override
    public List<Film> getAll() {
        log.info("Получение всех фильмов");
        List<Film> films = filmStorage.findAllFilms();
        return enrichFilmsWithGenresAndLikes(films);
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

    private List<Film> enrichFilmsWithGenresAndLikes(List<Film> films) {
        Map<Long, Set<Genre>> filmGenres = genreStorage.findAllFilmGenres();
        Map<Long, Set<Long>> filmLikes = likeStorage.findAllFilmLikes();

        return films.stream()
                .peek(film -> {
                    film.setGenres(filmGenres.getOrDefault(film.getId(), new HashSet<>()));
                    film.setLikes(filmLikes.getOrDefault(film.getId(), new HashSet<>()));
                })
                .collect(Collectors.toList());
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