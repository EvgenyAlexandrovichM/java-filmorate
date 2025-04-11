package ru.yandex.practicum.filmorate.model.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.Data;
import ru.yandex.practicum.filmorate.model.user.enums.FriendshipStatus;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class User {
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

    private Set<Long> friends = new HashSet<>();
    // Для дальнейшей логики замена на private Map<Long, FriendshipStatus> friends = new HashMap<>();
    // где Long - ID друга, FriendshipStatus - статус дружбы

    private FriendshipStatus status;
}
