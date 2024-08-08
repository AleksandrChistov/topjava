DELETE
FROM meals;
DELETE
FROM user_role;
DELETE
FROM users;
ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password)
VALUES ('User', 'user@yandex.ru', 'password'),
       ('Admin', 'admin@gmail.com', 'admin'),
       ('Guest', 'guest@gmail.com', 'guest');

INSERT INTO user_role (role, user_id)
VALUES ('USER', 100000),
       ('ADMIN', 100001);

INSERT INTO meals (date_time, description, calories, user_id)
VALUES ('2020-01-29 19:45:000', 'Ужин', 385, 100000),
       ('2020-01-30 10:00:000', 'Завтрак', 500, 100000),
       ('2020-01-30 13:00:000', 'Обед', 1000, 100000),
       ('2020-01-30 20:00:000', 'Ужин', 500, 100000),
       ('2020-01-31 00:00:000', 'Еда на граничное значение', 100, 100000),
       ('2020-01-31 10:00:000', 'Завтрак', 1000, 100000),
       ('2020-01-31 13:00:000', 'Обед', 500, 100000),
       ('2020-01-31 20:00:000', 'Ужин', 410, 100000),
       ('2020-02-01 10:00:000', 'Завтрак', 615, 100000),
       ('2015-06-01 14:00:000', 'Админ ланч', 510, 100001),
       ('2015-06-01 21:00:000', 'Админ ужин', 1500, 100001);
