# Задание 8 — Параллельные потоки и корректность ⭐⭐⭐

## Цель
Понять, как работает `parallelStream`, когда его использовать безопасно, а когда — нет.

## Предварительное чтение

`parallelStream()` разбивает данные на части и обрабатывает их параллельно с помощью ForkJoinPool.
Это **не всегда быстрее** — есть накладные расходы на разбиение и слияние.

**Безопасно**: операции над независимыми элементами (`map`, `filter`, `reduce` с ассоциативной операцией).

**Опасно**: изменение общего состояния внутри стрима:

```java
// НЕПРАВИЛЬНО — race condition:
List<Integer> result = new ArrayList<>();
numbers.parallelStream().forEach(result::add);

// ПРАВИЛЬНО — collect безопасен:
List<Integer> result = numbers.parallelStream()
    .filter(n -> n > 0)
    .collect(Collectors.toList());
```

## Условие

В классе `ParallelStreams` реализуйте три метода:

### Часть A
```java
public long parallelSum(List<Long> numbers)
```
Вернуть сумму всех чисел с использованием `parallelStream()` и `reduce`.

### Часть B
```java
public List<String> parallelFilter(List<String> words, int minLength)
```
Вернуть список слов длиннее `minLength`, используя `parallelStream()`.
Результат должен быть **детерминированным** (один и тот же при каждом запуске).
Для этого в конце добавьте `.sorted()` перед `collect`.

### Часть C
```java
public boolean isSafeToUseSharedList(List<Integer> numbers)
```
Метод демонстрирует **опасность изменения общей коллекции** в параллельном потоке.
Реализация уже написана — **запустите тесты и посмотрите, что происходит**.

```java
public boolean isSafeToUseSharedList(List<Integer> numbers) {
    List<Integer> result = new ArrayList<>();
    numbers.parallelStream().forEach(result::add);  // НЕБЕЗОПАСНО — race condition
    return result.size() == numbers.size();
}
```

Изучите вывод теста: иногда метод возвращает `true` (повезло), иногда `false` (потеряны элементы).
Объясните в комментарии к методу: **почему `ArrayList` небезопасен для параллельной записи?**

### Часть D
```java
public List<Integer> safeParallelCollect(List<Integer> numbers, int minValue)
```
Теперь реализуйте **безопасный** аналог.

Используйте `parallelStream()` + `filter()` + `collect()`.
В отличие от `forEach` + `add()`, метод `collect()` создаёт **отдельный аккумулятор в каждом потоке**
и объединяет их только в конце — без конкурентной записи в одну коллекцию.

Тест всегда должен возвращать ровно столько элементов, сколько прошло через фильтр.

## Требования
- Части A, B и D реализовать самостоятельно через `parallelStream()`.
- Часть C уже реализована — только объясните в комментарии, почему это небезопасно.
- Тесты для D должны проходить стабильно при любом количестве запусков.

## Вопросы для размышления
1. Когда `parallelStream()` даёт реальный прирост производительности?
2. Почему `reduce` безопасен в параллельном режиме, а `forEach` с `ArrayList` — нет?
3. Что такое "thread-safe" коллекция? Назовите примеры из JDK.
4. Почему `collect()` безопасен в параллельном потоке, хотя внутри тоже накапливает результат?
   (Подсказка: изучите понятие «combiner» в `Collector`.)
5. Чем `CopyOnWriteArrayList` отличается от `ArrayList`? Когда её стоит использовать?

## Как проверить
Запустите тесты:
```bash
mvn test
```
