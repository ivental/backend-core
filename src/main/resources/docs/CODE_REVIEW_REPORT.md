# Code Review Report: InviteeController

## Issue #1: Неправильный HTTP метод (POST вместо GET)
**Приоритет:** CRITICAL  
**Местоположение:** Строка 24, метод `getInvitees()`  
**Что плохо:** `@PostMapping("/getInvitees")` используется для получения данных  
**Почему плохо:** Нарушает семантику HTTP (RFC 7231). POST предназначен для создания ресурсов, GET для чтения.
Кэширование и безопасность нарушаются  
**Как исправить:** Заменить на `@GetMapping("/invitees")`

## Issue #2: Глагол в URL (RPC стиль)
**Приоритет:** MAJOR  
**Местоположение:** Строка 24, URL `/getInvitees`  
**Что плохо:** Использование глагола `get` в пути ресурса  
**Почему плохо:** REST использует существительные для ресурсов (/invitees),
действие определяется HTTP методом  
**Как исправить:** URL должен быть `/invitees`

## Issue #3: Возврат Entity вместо DTO
**Приоритет:** CRITICAL  
**Местоположение:** Строка 25, 31, 37, 56, 66  
**Что плохо:** Методы возвращают тип `Invitee` (JPA Entity)  
**Почему плохо:** Exposure внутренних полей (version, audit fields), coupling API с базой данных. Риск утечки данных   
**Как исправить:** Создать `InviteeResponse` DTO и возвращать его

## Issue #4: SQL Injection уязвимость
**Приоритет:** CRITICAL  
**Местоположение:** Строка 42, метод `create()`  
**Что плохо:** Конкатенация строки в SQL запросе: `"WHERE email = '" + email + "'"`  
**Почему плохо:** Позволяет атакующему выполнить произвольный SQL код (например, `' OR '1'='1`)  
**Как исправить:** Использовать Spring Data JPA метод `existsByEmail(String email)` или PreparedStatement

## Issue #5: Отсутствие валидации входных данных
**Приоритет:** CRITICAL  
**Местоположение:** Строка 37, метод `create()`  
**Что плохо:** Параметр `@RequestBody Map<String, Object> params` без валидации  
**Почему плохо:** Можно передать null, пустые строки или некорректный email. Нет гарантии целостности данных  
**Как исправить:** Использовать DTO record с аннотациями `@Valid`, `@NotBlank`, `@Email`

## Issue #6: Возврат null вместо 404 Not Found
**Приоритет:** MAJOR  
**Местоположение:** Строка 32, метод `getById()`  
**Что плохо:** `return repository.findById(id).orElse(null);`  
**Почему плохо:** Клиент получает 200 OK с пустым телом, вместо 404. Нарушает HTTP семантику  
**Как исправить:** Выбросить `EntityNotFoundException`, обработать в GlobalExceptionHandler (вернет 404)

## Issue #7: Неправильный статус код для DELETE
**Приоритет:** MAJOR  
**Местоположение:** Строка 61, метод `delete()`  
**Что плохо:** Возвращает тело ответа (Entity) при удалении  
**Почему плохо:** Согласно REST best practices, успешное удаление должно возвращать 204 No Content без тела  
**Как исправить:** `return ResponseEntity.noContent().build();`

## Issue #8: Бизнес-логика в контроллере
**Приоритет:** MAJOR  
**Местоположение:** Строка 72-76, метод `updateStatus()`  
**Что плохо:** Проверка `if (status.equals("ACTIVE") ...)` внутри контроллера  
**Почему плохо:** Нарушение Single Responsibility Principle.
Контроллер должен только принимать запрос и делегировать Service  
**Как исправить:** Перенести проверку статусов в `InviteeService`

## Issue #9: Пустой catch блок (Swallowing Exception)
**Приоритет:** MAJOR  
**Местоположение:** Строка 79-82, метод `updateStatus()`  
**Что плохо:** `catch (Exception e) { return null; }`  
**Почему плохо:** Ошибка скрывается, логирование отсутствует, клиент получает некорректный ответ  
**Как исправить:** Убрать try-catch, пусть exception всплывает до GlobalExceptionHandler

## Issue #10: Field Injection вместо Constructor Injection
**Приоритет:** MAJOR  
**Местоположение:** Строка 20-21  
**Что плохо:** `@Autowired InviteeRepository repository;`  
**Почему плохо:** Усложняет тестирование (нельзя подставить mock в конструктор), делает поле изменяемым  
**Как исправить:** Использовать конструктор: `public InviteeController(InviteeRepository repository) { ... }`    
или @RequiredArgsConstructor

## Issue #11: Отсутствие пагинации
**Приоритет:** MAJOR  
**Местоположение:** Строка 25, метод `getInvitees()`  
**Что плохо:** `repository.findAll()` возвращает все записи сразу  
**Почему плохо:** При росте базы данных (10к+ записей) сервер упадет по памяти  
**Как исправить:** Добавить `Pageable pageable` и возвращать `Page<InviteeResponse>`

## Issue #12: Использование Map вместо DTO
**Приоритет:** MAJOR  
**Местоположение:** Строка 37, 66  
**Что плохо:** `@RequestBody Map<String, Object>`  
**Почему плохо:** Нет типобезопасности, сложно документировать API (OpenAPI), нет авто-валидации  
**Как исправить:** Создать `CreateInviteeRequest` и `UpdateInviteeStatusRequest` records  