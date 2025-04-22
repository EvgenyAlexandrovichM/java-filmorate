package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.dto.MpaRatingDto;
import ru.yandex.practicum.filmorate.dto.mappers.MpaRatingMapper;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.film.MpaRating;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/mpa")
@Slf4j
@RequiredArgsConstructor
public class MpaRatingController {

    private final MpaService mpaService;

    @GetMapping
    public ResponseEntity<List<MpaRatingDto>> getAllMpaRatings() {
        log.info("Запрос на получение всех рейтингов МРА.");

        List<MpaRatingDto> mpaRatings = mpaService.getAllMpaRatings()
                .stream()
                .map(MpaRatingMapper::mapToMpaRatingDto)
                .collect(Collectors.toList());
        return new ResponseEntity<>(mpaRatings, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MpaRatingDto> getMpaRatingById(@PathVariable Integer id) {
        log.info("Запрос на получение рейтинга МРА по ID: {}", id);

        MpaRating mpaRating = mpaService.getMpaRatingById(id)
                .orElseThrow(() -> new NotFoundException("Рейтинг МРА с id " + id + " не найден."));
        log.info("Рейтинг МРА с ID: {} найден.", id);
        return new ResponseEntity<>(MpaRatingMapper.mapToMpaRatingDto(mpaRating), HttpStatus.OK);
    }
}
