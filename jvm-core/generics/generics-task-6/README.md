# Задание 6 — Практика PECS

## Цель
Понять:
- границы типов при использовании wildcards
- понять PECS
- понять комбинацию возможностей границы типов и PECS, какого типа может быть входящий парамерт метода, какие действия возможны и с каким типами на чтение и запись

## Контекст
В проекте уже есть:
- `User`, `AdminUser extends User`, `SuperAdmin extends AdminUser`
- класс `ReadMethodsClass`, `WriteMethodsClass`

## Что нужно сделать студенту
Откройте класс:

- `src/main/java/com/example/generics/task6/ReadMethodsClass.java`

и реализуйте методы:

1. `printObject(List<? extends Object> list)`
2. `printUser(List<? extends User> list)`
3. `printAdmin(List<? extends AdminUser> list)`
4. `printSuperAdmin(List<? extends SuperAdmin> list)`

Откройте класс:

- `src/main/java/com/example/generics/task6/WriteMethodsClass.java`

и реализуйте методы:

1. `addObject(List<? super Object> list)`
2. `addUser(List<? super User> list)`
3. `addAdmin(List<? super AdminUser> list)`
4. `addSuperAdmin(List<? super SuperAdmin> list)`

## Условия и ограничения
- решение должно использовать wildcard'ы в сигнатурах методов.

## Подсказка: PECS
- **Producer Extends** — источник, из которого мы читаем элементы: `? extends ...`
- **Consumer Super** — приёмник, в который мы добавляем элементы: `? super ...`

## Как проверить
Запустите Main.main:


