## Сравнение: new внутри vs DI через конструктор

### BAD: `new InMemoryLeadRepository()` внутри класса

```java
    public class LeadService {
        private final LeadRepository repository =
        new InMemoryLeadRepository();                        
    }
```

#### Проблемы:
- Невозможно подставить mock в тестах
- Невозможно заменить на PostgreSQL без изменения кода
- Скрытая зависимость - не видно, что нужно для работы

### GOOD: DI через конструктор
```java
    public class LeadService {
    private final LeadRepository repository;

        public LeadService(LeadRepository repository) {
            this.repository = repository;
        }
    }
```

#### Преимущества:
- В тестах передаём mock(`LeadRepository.class`)
- В production передаём InMemoryLeadRepository
- В будущем передаём `JpaLeadRepository` (Sprint 7)
- Зависимость явная — видно в конструкторе

## Сравнение стеков Servlet vs Spring Boot
### Результаты интеграционного теста


| Метрика            | Servlet | Spring Boot | Комментарий                    |
|--------------------|---------|-------------|--------------------------------|
| Время старта       | ~500 ms | ~2500 ms    | Spring загружает IoC контейнер |
| HTTP 200 на /leads | ✅       | ✅           | Оба работают идентично         |
| Количество лидов   | N       | N           | Данные одинаковые              |
| Строк Java кода    | ~150    | ~30         | Контраст 5:1                   |

### Вывод

Оба стека возвращают идентичные данные,
но Spring Boot требует в 5 раз меньше кода за счёт auto-configuration.
Trade-off: Spring стартует медленнее из-за инициализации IoC контейнера.

*Данные получены из `StackComparisonTest.java`*