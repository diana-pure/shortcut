# Задание 7 — Числовые потоки: IntStream, mapToInt ⭐⭐

## Цель
Познакомиться со специализированными числовыми потоками (`IntStream`, `LongStream`, `DoubleStream`),
которые избегают лишнего boxing/unboxing и предоставляют удобные методы для чисел.

## Предварительное чтение

`Stream<Integer>` хранит объекты `Integer` (boxing). При большом объёме данных это медленно.
`IntStream` работает с примитивами `int` напрямую — это быстрее.

```java
// Диапазон чисел
IntStream.range(1, 5);        // 1, 2, 3, 4
IntStream.rangeClosed(1, 5);  // 1, 2, 3, 4, 5

// Переход из Stream<String>
IntStream lengths = list.stream().mapToInt(String::length);

// Встроенные агрегаты
IntStream.range(1, 6).sum();            // 15
IntStream.range(1, 6).average();        // OptionalDouble(3.0)
IntStream.of(3, 1, 4).max().getAsInt(); // 4
```

## Условие

В классе `NumericStreams` реализуйте четыре метода:

### Часть A
```java
public int sumRange(int from, int to)
```
Вернуть сумму всех целых чисел от `from` до `to` включительно.
Использовать `IntStream.rangeClosed`.

### Часть B
```java
public double averageWordLength(List<String> words)
```
Вернуть среднюю длину слов в списке.
Использовать `mapToInt(String::length).average()`.
Если список пуст — вернуть `0.0`.

### Часть C
```java
public long countDistinctLengths(List<String> words)
```
Вернуть количество **различных** длин слов в списке.
Пример: `["cat", "dog", "elephant"]` → 2 (длины: 3 и 8).

### Часть D
```java
public List<Integer> generateSquares(int n)
```
Вернуть список квадратов чисел от 1 до `n` включительно.
Пример: `n=4` → `[1, 4, 9, 16]`.
Использовать `IntStream.rangeClosed(1, n).map(i -> i * i).boxed()`.

## Требования
- Использовать числовые потоки (`IntStream`, `mapToInt`, `mapToLong`).
- Не использовать циклы.

## Вопрос для размышления
Зачем вызывать `.boxed()` при переходе от `IntStream` к `List<Integer>`?
Что произойдёт без него?

## Как проверить
Запустите тесты:
```bash
mvn test
```
