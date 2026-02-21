[![Codacy Badge](https://app.codacy.com/project/badge/Grade/e9d3a6d28ee444a59b7bc68d373d7315)](https://app.codacy.com/gh/AleksandrChistov/topjava/dashboard)

# Веб-приложение для управления учетными записями и ролями пользователей

Проект демонстрирует создание enterprise-приложения на базе Spring экосистемы.

### Используемые технологии и библиотеки:

**Ядро и фреймворки:**
- Java 21
- Spring Framework 5.3.37 (Core, Context, WebMVC)
- Spring Data JPA 2.7.18
- Spring Security 5.8.13

**ORM и база данных:**
- Hibernate 5.6.15 (JPA имплементация)
- PostgreSQL 42.7.3 (основная БД)
- H2 Database (для тестов)

**Веб-технологии:**
- JSP и JSTL для серверного рендеринга
- WebJars: Bootstrap 4.6.2, jQuery 3.7.1, DataTables 1.13.5
- REST API с поддержкой JSON

**Дополнительные библиотеки:**
- Jackson 2.17.2 для работы с JSON
- Hibernate Validator 6.2.5 для валидации данных
- Ehcache 3.10.8 для кэширования
- Swagger 2.9.2 для документирования API

**Тестирование:**
- JUnit 5
- Mockito
- AssertJ
- JSONassert

## Выводы

В ходе реализации проекта были получены практические навыки:

- Разработки многоуровневой архитектуры приложения (DAO, Service, Controller)
- Интеграции Spring Security для аутентификации и авторизации
- Настройки JPA и Hibernate для работы с реляционными базами данных
- Создания RESTful API и веб-интерфейса
- Применения паттернов проектирования в enterprise-приложениях
- Настройки системы сборки Maven для сложных проектов

Проект успешно демонстрирует возможности Spring Framework для создания масштабируемых веб-приложений и может служить основой для дальнейшего масштабирования.

## Инструкция по развёртыванию

### Системные требования
- Java 21 или выше
- Apache Maven 3.6+
- PostgreSQL 12+ (опционально, по умолчанию используется H2)
- Браузер для доступа к веб-интерфейсу

### Запуск проекта

1. Клонируйте репозиторий:
```bash
git clone https://github.com/AleksandrChistov/topjava.git
```

2. Перейдите в директорию проекта:
```bash
cd topjava
```

3. Соберите проект:
```bash
mvn clean package
```

4. Запустите приложение:
```bash
mvn cargo:run
```

5. Откройте в браузере:
```
http://localhost:8080/topjava
```

Приложение будет автоматически использовать H2 базу данных в режиме in-memory. Для использования PostgreSQL активируйте профиль `postgres`:

```bash
mvn cargo:run -Ppostgres
```

## Topics
Java, Spring, SpringBoot, Hibernate, JPA, REST, API, Web, Security, PostgreSQL, Maven, JUnit, Testing, WebMVC, JSP, JSTL, Bootstrap, jQuery