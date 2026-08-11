# Задание 1 — Базовые операции: filter, map, collect ⭐

## Цель
Освоить основной набор операций Stream API: `filter`, `map`, `collect`, `forEach`.

## Условие

В классе `StreamBasics` реализуйте четыре метода:

### Часть A
```java
public List<String> filterLongWords(List<String> words, int minLength)
```
Вернуть список слов, длина которых **строго больше** `minLength`.

### Часть B
```java
public List<String> toUpperCase(List<String> words)
```
Вернуть список слов, переведённых в верхний регистр.

### Часть C
```java
public List<Integer> wordLengths(List<String> words)
```
Вернуть список длин слов (в том же порядке).

### Часть D
```java
public List<String> filterAndTransform(List<String> words, int minLength)
```
Вернуть список слов длиннее `minLength`, переведённых в верхний регистр.
Объединить `filter` и `map` в одну цепочку.

## Требования
- Реализовать каждый метод через Stream API (без циклов `for`/`while`).
- Не изменять входной список.
- Методы должны корректно работать с пустым списком.

## Как проверить
Запустите тесты:
```bash
mvn test
```
