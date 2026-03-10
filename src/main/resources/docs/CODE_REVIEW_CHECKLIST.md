# REST Controller Code Review Checklist

## Категория 1: API Design

1. **Неправильные HTTP методы** (CRITICAL)
    - Что искать: POST для чтения, GET для модификации
    - Пример плохо: `@PostMapping("/getItems")`
    - Пример хорошо: `@GetMapping("/items")`
2. **Неправильные статус коды** (CRITICAL)
    - Что искать: 200 OK для всех операций, отсутствие 201/204/404
    - Пример плохо: `return service.save();` (всегда 200)
    - Пример хорошо: `return ResponseEntity.created(location).body(dto);` (201)
3. **Плохой naming: глаголы в URL** (MAJOR)
    - Что искать: `/getItems`, `/createItem`
    - Пример плохо: `/getItems` (глагол в пути)
    - Пример хорошо: `/items` (существительные)
4. **Entity вместо DTO в response** (CRITICAL)
    - Что искать: Возврат JPA Entity классов напрямую
    - Риск: Exposure внутренних полей, coupling
5. **Нет пагинации для списков** (MAJOR)
    - Что искать: `List<Entity> getAll()` без `Pageable`
    - Риск: Performance (OOM при большом объеме)

## Категория 2: Security

1. **SQL injection через конкатенацию** (CRITICAL)
    - Что искать: `"SELECT ... WHERE col = '" + var + "'"`
    - Решение: PreparedStatement или Spring Data JPA методы
2. **Exposure внутренних полей** (CRITICAL)
    - Что искать: password, version, internalId в JSON response
    - Решение: DTO с только публичными полями
3. **Нет валидации входных данных** (CRITICAL)
    - Что искать: `@RequestBody` без `@Valid` и аннотаций Bean Validation
    - Решение: DTO records с `@NotBlank`, `@Email` + `@Valid`
4. **Stack trace в error response** (CRITICAL)
    - Что искать: `e.printStackTrace()` или дефолтный Spring error
    - Решение: GlobalExceptionHandler + RFC 7807 Problem Details
5. **Missing authorization checks** (CRITICAL)
    - Что искать: Отсутствие `@PreAuthorize` на чувствительных методах

## Категория 3: Error Handling

1. **Пустые catch блоки** (MAJOR)
    - Что искать: `catch (Exception e) {}` или `return null`
    - Решение: GlobalExceptionHandler + RFC 7807 Problem Details
2. **500 на бизнес-ошибки** (MAJOR)
    - Что искать: `throw new RuntimeException("Business error")`
    - Решение: Специфичные исключения + 4xx статусы
3. **Generic error messages** (MINOR)
    - Что искать: "Error occurred" без деталей
    - Решение: Problem Details с полями ошибок
4. **Нет логирования ошибок** (MAJOR)
    - Что искать: Exception съедается без лога

## Категория 4: Code Quality

1. **Бизнес-логика в контроллере** (MAJOR)
    - Что искать: if/else правила, расчеты в контроллере
    - Решение: Вынести в @Service
2. **Дублирование кода** (MAJOR)
    - Что искать: Одинаковый try-catch в каждом методе
    - Решение: GlobalExceptionHandler
3. **God Controller** (MINOR)
    - Что искать: >20 методов в одном контроллере
    - Решение: Декомпозиция по bounded contexts
4. **Hardcoded values** (MINOR)
    - Что искать: Magic numbers, hardcoded URLs/roles
    - Решение: @ConfigurationProperties