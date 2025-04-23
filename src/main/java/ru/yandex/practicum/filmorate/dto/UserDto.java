package ru.yandex.practicum.filmorate.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class UserDto {

    private Long id;

    @NotBlank(message = "Электронная почта не может быть пустой")
    @Email(message = "Некорректный формат электронной почты.")
    private String email;

    @NotBlank(message = "Логин не может быть пустым.")
    private String login;

    private String name;

    @Past(message = "Дата рождения не может быть в будущем.")
    @NotNull(message = "Дата рождения не может быть пустой.")
    private LocalDate birthday;

    private Set<Long> friendIds = new HashSet<>();
}
