# Практическое задание: Выполнение задач (глава 7)

Модуль покрывает всю тему «Выполнение задач»: основы создания потоков
(`Thread`/`Runnable`/`Callable`) и асинхронное программирование с `CompletableFuture`.

---

## Часть 1. Thread, Runnable, Callable

### Описание тестов

#### testRunnableCounter
Проверяет `CounterTask` (Runnable): 10 потоков, каждый инкрементирует `AtomicInteger` 1000 раз → итого 10 000.

#### testCallableSum
Проверяет `SumTask` (Callable<Long>): сумма чисел от 1 до 100 равна 5050.

#### testMultipleCallables
Проверяет параллельное выполнение 5 экземпляров `SumTask` с разными `n` — каждый возвращает свою сумму.

#### testCallableWithException
Проверяет, что при `n < 0` `SumTask.call()` бросает исключение, которое оборачивается в `ExecutionException`.

#### testInterruption
Проверяет `InterruptibleTask`: поток корректно реагирует на `interrupt()` и устанавливает флаг `completed = true`.

### Классы для реализации

- **CounterTask** — `Runnable`, инкрементирует переданный `AtomicInteger` в `run()`
- **SumTask** — `Callable<Long>`, считает сумму 1..n; при n < 0 бросает `IllegalArgumentException`
- **InterruptibleTask** — `Runnable`, спит в цикле, при прерывании корректно завершается и устанавливает `completed = true`

---

## Часть 2. CompletableFuture

Асинхронное программирование с `CompletableFuture`: цепочки преобразований, объединение, обработка ошибок.

### Тесты

| # | Тест | Описание |
|---|------|----------|
| 1 | `testThenApply` | `"Hello"` проходит через цепочку `thenApply` и превращается в `"HELLO WORLD"` |
| 2 | `testThenCompose` | `10` через `thenCompose` (flatMap) превращается в `"Result: 10"` |
| 3 | `testThenCombine` | Два независимых Future объединяются: `"Hello"` + `"World"` → `"Hello World"` |
| 4 | `testAllOf` | `allOf` ожидает завершения трёх независимых Future — все помечены `isDone()` |
| 5 | `testExceptionally` | При выбросе исключения `exceptionally` перехватывает его и возвращает строку с `"Обработано"` |
| 6 | `testHandle` | `handle` работает в обоих случаях: успех → `"УСПЕХ"`, ошибка → `"Обработана ошибка"` |
| 7 | `testThenAccept` | `thenAccept` передаёт результат `42` в `Consumer` без возврата значения |
| 8 | `testTimeout` | `future.get(200ms)` бросает `TimeoutException` для задачи с `sleep(3000)` |

### AsyncTaskProcessor — что нужно реализовать

| Метод | Описание |
|-------|----------|
| `createTransformationChain(String input)` | `supplyAsync(input)` → `thenApply(+ " WORLD")` → `thenApply(toUpperCase)` |
| `createComposition(int value)` | `supplyAsync(value)` → `thenCompose(v → supplyAsync("Result: " + v))` |
| `combineResults(String s1, String s2)` | Два `supplyAsync` → `thenCombine((a, b) -> a + " " + b)` |
| `waitForAll(CompletableFuture<?>... futures)` | `CompletableFuture.allOf(futures)` |
| `handleError(boolean shouldFail)` | `supplyAsync` с условным исключением + `exceptionally("Обработано: " + ex.getMessage())` |
| `handleSuccessOrError(boolean shouldFail)` | `supplyAsync` + `handle((result, ex) -> ex != null ? "Обработана ошибка" : result.toUpperCase())` |
| `processResult(int value, Consumer<Integer> consumer)` | `supplyAsync(value)` → `thenAccept(consumer)` |
