# Задание 6 — Optional, findFirst, anyMatch ⭐⭐

## Цель
Понять роль `Optional` как способа явно обработать отсутствие значения.
Научиться использовать `findFirst`, `anyMatch`, `allMatch`, `noneMatch`.

## Предварительное чтение

`Optional<T>` — обёртка над значением, которого **может не быть**.
Это замена ненадёжному `null`.

```java
Optional<String> opt = Optional.of("hello");
opt.isPresent();      // true
opt.get();            // "hello"
opt.orElse("default"); // "hello"

Optional<String> empty = Optional.empty();
empty.isPresent();    // false
empty.orElse("default"); // "default"
```

## Условие

В классе `StreamSearch` реализуйте четыре метода:

### Часть A
```java
public Optional<String> findFirstStartingWith(List<String> words, char letter)
```
Найти первое слово, начинающееся с указанной буквы.
Вернуть `Optional.empty()` если ни одного нет.

### Часть B
```java
public boolean hasNegative(List<Integer> numbers)
```
Вернуть `true`, если в списке есть хотя бы одно отрицательное число.
Использовать `anyMatch`.

### Часть C
```java
public boolean allPositive(List<Integer> numbers)
```
Вернуть `true`, если все числа положительные (> 0).
Использовать `allMatch`.

### Часть D
```java
public String findFirstOrDefault(List<String> words, int minLength, String defaultValue)
```
Найти первое слово длиннее `minLength`.
Если не найдено — вернуть `defaultValue`.
Использовать `findFirst().orElse(...)`.

## Требования
- Реализовать каждый метод через Stream API.
- Для пустых списков: `hasNegative` → `false`, `allPositive` → `true`.

## Вопрос для размышления
Почему `allMatch` на пустом стриме возвращает `true`?
(Подсказка: это математическая концепция «универсальной квантификации над пустым множеством».)

## Как проверить
Запустите тесты:
```bash
mvn test
```
