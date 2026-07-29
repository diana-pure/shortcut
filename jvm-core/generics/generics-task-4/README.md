# Задание 4 — Wildcards и PECS

## Цель
Понять разницу между `? extends` и `? super` и применить принцип PECS:
- Producer Extends — источник «производит» элементы (мы читаем из него)
- Consumer Super — приёмник «потребляет» элементы (мы добавляем в него)

## Условие
В классе `CopyUtils` реализуйте метод:
```java
public static void copy(List<? extends Number> source,
                        List<? super Number> destination)
```
Метод должен скопировать все элементы из `source` в `destination`.

## Требования
- Не использовать cast и raw-типы.
- Сигнатуру метода менять нельзя (нужно решить именно через wildcard'ы).
- Код должен компилироваться без unchecked warning.

## Как проверить
```bash
mvn test
```
