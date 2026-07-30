# Задание 3 — Ограничения сверху (`<T extends Number>`)

## Цель
Понять, зачем нужны ограничения типа (`extends`) и как с ними работать.

## Условие
В классе `NumberUtils` реализуйте метод:
```java
public static <T extends Number> double sum(List<T> numbers)
```
Метод должен возвращать сумму всех элементов списка в виде `double`.

## Требования
- Метод должен работать как минимум с `List<Integer>` и `List<Double>`.
- Не использовать `Object`, cast и raw-типы.
- Обработать `null` и/или пустой список (например, выбросить `IllegalArgumentException`).

## Вопрос для размышления
Почему нельзя написать:
```java
public static <T> double sum(List<T> numbers)
```
(подсказка: компилятор не знает, что у `T` есть `doubleValue()`).

## Как проверить
```bash
mvn test
```
