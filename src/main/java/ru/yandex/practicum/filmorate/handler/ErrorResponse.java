package ru.yandex.practicum.filmorate.handler;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
public class ErrorResponse {
    private Map<String, List<String>> errors;
}
