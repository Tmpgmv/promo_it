login: promo_it
password: awerdWdgy!24



User
---
id
login
password // Хешированный
role



OtpConfig // Таблица может содержать не более 1 записи.
------
id
lifespan
number_of_symbols

Code
-----
id
timestamp
code // Уникальный
operation_id



Operation
---------
id
operation_name






create database promo_it;
create role promo_it with password 'awerdWdgy!24' login;
postgres=# create database promo_it;
\c promo_it

-- grant all on existing tables and sequences in public
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO promo_it;
GRANT USAGE, SELECT ON ALL SEQUENCES IN SCHEMA public TO promo_it;

-- grant all on future tables/sequences
ALTER DEFAULT PRIVILEGES IN SCHEMA public
GRANT ALL PRIVILEGES ON TABLES TO promo_it;

ALTER DEFAULT PRIVILEGES IN SCHEMA public
GRANT USAGE, SELECT ON SEQUENCES TO promo_it;


SET TIME ZONE 'Europe/Moscow';

-- Таблица пользователей
CREATE TABLE application_user (
    id SERIAL PRIMARY KEY,
    login VARCHAR(255) NOT NULL UNIQUE,
    password TEXT NOT NULL,
    role VARCHAR(50) NOT NULL
);

CREATE UNIQUE INDEX one_admin_only ON application_user(role) WHERE role = 'admin';

CREATE TABLE OtpConfig (
    -- Ограничение id = 1 гарантирует синглтон на уровне схемы
    id INTEGER PRIMARY KEY CHECK (id = 1),
    lifespan INTERVAL NOT NULL,
    -- Бизнес-валидация: длина кода не менее 4 символов
    number_of_symbols INTEGER NOT NULL CHECK (number_of_symbols >= 4)
);
-- Начальная инициализация (опционально)
INSERT INTO OtpConfig (id, lifespan, number_of_symbols)
VALUES (1, '5 minutes'::interval, 6)
ON CONFLICT DO NOTHING;

-- Таблица операций
CREATE TABLE Operation (
    id SERIAL PRIMARY KEY,
    operation_name VARCHAR(255) NOT NULL UNIQUE
);

CREATE UNIQUE INDEX operation_name ON Operation ((TRUE));

-- Таблица кодов
CREATE TABLE Code (
    id SERIAL PRIMARY KEY,
    timestamp TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    code VARCHAR(255) NOT NULL UNIQUE,
    operation_id INTEGER NOT NULL REFERENCES Operation(id) ON DELETE CASCADE
);



sudo docker run --rm -p 8081:8080 \
  -e SWAGGER_JSON=/spec/swagger.yaml \
  -v "$PWD":/spec \
  --name swaggerui swaggerapi/swagger-ui


Для каждого теста:
1. Название теста.
2. Сценарий.
3. Что на входе.
4. Ожидаемый результат.




POST /api/otp/validate

Тело запроса:
{
  "operationId": "123",
  "otpCode": "6087"
}
Что делает сервер:
Ищет в БД запись, где operation_id = '123' и status = 'ACTIVE'.
Проверка 1: Если запись не найдена или код не совпадает — ответ 403 Forbidden (или 400 Bad Request).
Проверка 2: Сравнивает текущее время с временем истечения. Если время вышло — меняет статус на EXPIRED и отвечает 403.
Успех: Если всё ок — меняет статус на USED и отвечает 200 OK.