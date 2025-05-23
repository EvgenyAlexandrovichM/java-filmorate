# java-filmorate
Template repository for Filmorate project.
![Схема базы данных](diagram.png)

### USERS
* user_id (PK -<): Уникальный идентификатор.
* email (varchar): Электронная почта пользователя.
* login (varchar): Логин пользователя.
* name (varchar): Имя пользователя
* birthday (date): Дата рождения пользователя.

### FILMS
* film_id (PK -<): Уникальный идентификатор.
* title (varchar): Название фильма.
* description (varchar(200)): Описание фильма.
* release_date (date): Дата выпуска фильма.
* duration (int): Длительность фильма.
* mpa_rating_id (FK -): Внешний ключ, ссылающийся на таблицу MPA_RATINGS.


### MPA_RATINGS
* mpa_rating_id (PK -): Уникальный идентификатор рейтинга МРА.
* name (varchar): Название рейтинга МРА.

### GENRES
* genre_id (PK -<): Уникальный идентификатор.
* name varchar

### FILM_GENRES

* film_id (PK FK >-): Уникальный идентификатор, внешний ключ, ссылающийся на таблицу FILMS.
* genre_id (PK FK >-): Уникальный идентификатор, внешний ключ, ссылающийся на таблицу GENRES.

### LIKES

* film_id (PK FK >-): Уникальный идентификатор, внешний ключ, ссылающийся на таблицу FILMS.
* user_id (PK FK >-): Уникальный идентификатор, внешний ключ, ссылающийся на таблицу USERS.

### FRIENDSHIPS

* user_id (PK FK >-): Уникальный идентификатор, внешний ключ, ссылающийся на таблицу USERS.
* friend_id (PK FK >-): Уникальный идентификатор, внешний ключ, ссылающийся на таблицу USERS.