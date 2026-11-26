# Practice 13 — ForkJoinPool

## Задача

Реализовать три задачи на основе `RecursiveTask<T>` для параллельного вычисления через `ForkJoinPool`.
Освоить паттерн divide-and-conquer: разбиение задачи пополам, fork/compute/join, объединение результатов.

## Тесты

| # | Тест | Описание |
|---|------|----------|
| 1 | `testArraySumTask` | Сумма массива [1..1000] должна равняться 500 500 |
| 2 | `testArraySumSingleElement` | Массив из одного элемента 42 — результат 42 (граничный случай, длина < THRESHOLD) |
| 3 | `testArrayMaxTask` | Максимум в случайном массиве из 10 000 элементов совпадает с эталонным значением |
| 4 | `testArrayMaxSmall` | Максимум в массиве [5, 3, 9, 1, 7] = 9 |
| 5 | `testWordCountTask` | 100 строк: 50× "hello world" + 50× "hello java" → hello=100, world=50, java=50 |
| 6 | `testCustomPoolWorkStealing` | Кастомный `ForkJoinPool(4)` суммирует 50 000 единиц = 50 000; проверяет работу work-stealing |

## Классы

### ArraySumTask — **реализовать**

Наследует `RecursiveTask<Long>`. Поля: `long[] array`, `int start`, `int end`.
- `THRESHOLD = 500`: отрезки короче порога считаются последовательно.
- Стратегия fork/join: `leftTask.fork()` → `new Task(mid, end).compute()` → `leftTask.join()` → сложить.

### ArrayMaxTask — **реализовать**

Наследует `RecursiveTask<Integer>`. Поля: `int[] array`, `int start`, `int end`.
- `THRESHOLD = 500`: при достижении порога — линейный поиск максимума.
- Объединение: `Math.max(leftMax, rightMax)`.

### WordCountTask — **реализовать**

Наследует `RecursiveTask<Map<String, Integer>>`. Поля: `String[] texts`, `int start`, `int end`.
- `THRESHOLD = 10`: при достижении порога — разбить каждую строку по пробелам (`split("\\s+")`), накопить счётчики через `Map.merge(word, 1, Integer::sum)`.
- Объединение двух `Map`: `rightResult.forEach((k, v) -> leftResult.merge(k, v, Integer::sum))`.
