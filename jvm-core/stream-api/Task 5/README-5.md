# Задание 5 — Collectors: groupingBy, partitioningBy, joining ⭐⭐

## Цель
Научиться использовать продвинутые сборщики из `java.util.stream.Collectors`.

## Условие

В классе `StreamCollectors` реализуйте четыре метода:

### Часть A
```java
public Map<Integer, List<String>> groupByLength(List<String> words)
```
Сгруппировать слова по их длине.
Пример: `["cat", "dog", "elephant"]` → `{3: ["cat", "dog"], 8: ["elephant"]}`

### Часть B
```java
public Map<Boolean, List<Integer>> partitionEvenOdd(List<Integer> numbers)
```
Разделить числа на чётные (`true`) и нечётные (`false`).

### Часть C
```java
public String joinWithComma(List<String> words)
```
Объединить все слова через `", "` (запятую и пробел).
Пример: `["a", "b", "c"]` → `"a, b, c"`

### Часть D
```java
public Map<Character, Long> countByFirstLetter(List<String> words)
```
Для каждой первой буквы подсчитать, сколько слов начинается с этой буквы.
Пример: `["apple", "ant", "banana"]` → `{'a': 2, 'b': 1}`

## Требования
- Использовать `Collectors.groupingBy`, `Collectors.partitioningBy`, `Collectors.joining`, `Collectors.counting`.
- Не использовать циклы.

## Вопрос для размышления
Чем `groupingBy` отличается от `partitioningBy`?
Когда лучше использовать `partitioningBy`?

## Как проверить
Запустите тесты:
```bash
mvn test
```
