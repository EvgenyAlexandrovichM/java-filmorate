package ru.yandex.practicum.filmorate.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.exception.BadRequestException;
import ru.yandex.practicum.filmorate.exception.DuplicatedDataException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;

import java.util.ArrayList;
import java.util.List;


@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> notValid(MethodArgumentNotValidException e) {
        List<String> errors = new ArrayList<>();
        e.getAllErrors().forEach(err -> errors.add(err.getDefaultMessage()));
        log.warn("Ошибка валидации: {}", errors);
        return createErrorResponse(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<?> handleNotFoundException(NotFoundException e) {
        log.warn("Не найдено: {}", e.getMessage());
        List<String> errors = new ArrayList<>();
        errors.add("Не найдено: " + e.getMessage());
        return createErrorResponse(errors, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<?> handleBadRequestException(BadRequestException e) {
        log.warn("Некорректный запрос: {}", e.getMessage());
        List<String> errors = new ArrayList<>();
        return createErrorResponse(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DuplicatedDataException.class)
    public ResponseEntity<?> handleDuplicatedDataException(DuplicatedDataException e) {
        log.warn("Ошибка дублирования данных: {}", e.getMessage());
        List<String> errors = new ArrayList<>();
        errors.add("Пользователь с таким логином или email уже существует.");
        return createErrorResponse(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleOtherException(Exception e) {
        log.error("Произошла непредвиденная ошибка: {}", e.getMessage(), e);
        List<String> errors = new ArrayList<>();
        errors.add("Произошла непредвиденная ошибка: " + e.getMessage());
        return createErrorResponse(errors, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    private ResponseEntity<ErrorResponse> createErrorResponse(List<String> errors, HttpStatus status) {
        ErrorResponse response = new ErrorResponse(errors);
        return new ResponseEntity<>(response, status);
    }
}