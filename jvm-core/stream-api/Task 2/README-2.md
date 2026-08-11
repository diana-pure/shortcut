# Задание 2 — Агрегирующие операции: count, reduce, min, max ⭐

## Цель
Научиться использовать терминальные операции для агрегации данных в стриме.

## Условие

В классе `StreamAggregation` реализуйте четыре метода:

### Часть A
```java
public long countPositive(List<Integer> numbers)
```
Вернуть количество положительных чисел в списке (> 0).

### Часть B
```java
public int sumAll(List<Integer> numbers)
```
Вернуть сумму всех чисел через `reduce`.
Если список пуст — вернуть `0`.

### Часть C
```java
public Optional<Integer> findMin(List<Integer> numbers)
```
Вернуть минимальный элемент списка через `min`.

### Часть D
```java
public int sumOfSquares(List<Integer> numbers)
```
Вернуть сумму квадратов всех чисел.
Например: `[1, 2, 3]` → `1 + 4 + 9 = 14`.
Использовать `map` + `reduce`.

## Требования
- Реализовать каждый метод через Stream API.
- Не использовать циклы.
- Методы должны корректно работать с пустым списком.

## Вопрос для размышления
Чем `reduce(0, Integer::sum)` отличается от `reduce(Integer::sum)`?
Почему во втором случае возвращается `Optional<Integer>`?

## Как проверить
Запустите тесты:
```bash
mvn test
```
