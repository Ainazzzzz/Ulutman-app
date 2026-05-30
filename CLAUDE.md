# Ulutman — Backend

Spring Boot 3.3.1 REST API. Java 17. PostgreSQL. Port 8081.

## Stack

- **Framework**: Spring Boot 3.3.1
- **Language**: Java 17
- **Database**: PostgreSQL 16
- **Auth**: JWT + Google OAuth2 (Spring Security)
- **Storage**: AWS S3 (bucket `ulutman-images`, region `eu-north-1`)
- **Docs**: Swagger UI at `/swagger-ui/index.html`
- **Email**: Gmail SMTP (hardcoded in `EmailConfig.java`)
- **Firebase**: отключён (`FirebaseConfig.java` — заглушка)

## Package structure

```
com.ulutman/
  config/       — Spring конфигурации (Email, Firebase, Swagger, Locale)
  controller/   — REST endpoints
  exception/    — кастомные исключения + GlobalExceptionHandler
  mapper/       — DTO ↔ Entity маппинг
  model/
    dto/        — Request/Response классы
    entities/   — JPA сущности
    enums/      — Category, Role, Status и др.
  repository/   — Spring Data JPA репозитории
  security/jwt/ — JwtFilter, JwtUtil, SecurityConfig
  service/      — бизнес-логика
```

## Запуск локально (Docker — рекомендуется)

```bash
# 1. Скопировать и заполнить env
cp .env.example .env
# отредактировать .env — вставить реальные ключи

# 2. Запустить
docker compose up --build
```

API будет доступен на `http://localhost:8081`.

## Запуск без Docker (требуется PostgreSQL локально)

```bash
# 1. Создать базу данных
psql -c "CREATE USER ulutmanuser WITH PASSWORD 'localpassword';"
psql -c "CREATE DATABASE ulutman OWNER ulutmanuser;"

# 2. Запустить приложение
./mvnw spring-boot:run -Dspring-boot.run.profiles=local
```

## Переменные окружения

| Переменная | Описание |
|---|---|
| `SPRING_DATASOURCE_URL` | JDBC URL к PostgreSQL |
| `SPRING_DATASOURCE_USERNAME` | Пользователь БД |
| `SPRING_DATASOURCE_PASSWORD` | Пароль БД |
| `JWT_SECRET` | Секрет для подписи JWT (минимум 256 бит) |
| `AWS_ACCESS_KEY` | AWS Access Key ID |
| `AWS_SECRET_KEY` | AWS Secret Access Key |
| `GOOGLE_CLIENT_ID` | Google OAuth2 Client ID |
| `GOOGLE_CLIENT_SECRET` | Google OAuth2 Client Secret |

## Сборка

```bash
./mvnw clean package -DskipTests
```

## Ключевые эндпоинты

- `POST /api/auth/register` — регистрация
- `POST /api/auth/login` — вход
- `GET  /api/publishes/**` — объявления (публичный доступ)
- `GET  /api/main-page/**` — главная страница
- `/api/manage/**` — только `ADMIN`
- `/swagger-ui/index.html` — Swagger UI

## CORS

Разрешённые origins: `localhost:5173`, `ulutman-api.com`, `api.ulutman-api.com`, Amplify dev URL.
