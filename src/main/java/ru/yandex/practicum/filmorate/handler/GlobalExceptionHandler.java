/*package ru.yandex.practicum.filmorate.handler;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception e, HttpServletRequest request) {
        log.error("Произошла необработанная ошибка: {}", e.getMessage(), e);
        Map<String, Object> result = new HashMap<>();
        result.put("error", "Внутренняя ошибка сервера");
        result.put("message", e.getMessage());
        return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
    }
} */