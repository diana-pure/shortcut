# Practice 8 — ExecutorService

## Описание

Практика по работе с `ExecutorService`, `ScheduledExecutorService` и параллельной обработкой задач.

## Тесты

| # | Тест | Описание |
|---|------|----------|
| 1 | `testFixedThreadPool` | 10 задач через пул 5 потоков — каждый `Future` возвращает правильный результат `taskId * 2` |
| 2 | `testParallelProcessing` | Список `[1,2,3,4,5]` обрабатывается параллельно: результат `[2,4,6,8,10]` с сохранением порядка |
| 3 | `testExceptionHandling` | Исключение внутри `Callable` оборачивается в `ExecutionException` при вызове `future.get()` |
| 4 | `testTimeout` | `future.get(timeout)` бросает `TimeoutException`, если задача не укладывается в тайм-аут |
| 5 | `testScheduleOnce` | Однократная задача выполняется ровно один раз после заданного `delay` |
| 6 | `testScheduleRepeating` | Периодическая задача выполняется не менее 3 раз за 400 мс при интервале 100 мс |

## Что нужно реализовать

### TaskProcessor

Обёртка над `ExecutorService` с фиксированным пулом потоков (`newFixedThreadPool`).

- `TaskProcessor(int poolSize)` — создаёт пул заданного размера
- `<T> Future<T> processTask(Callable<T> task)` — отправляет задачу в пул через `submit()`
- `boolean shutdownGracefully(long timeout, TimeUnit unit)` — вызывает `shutdown()`, затем `awaitTermination()`, возвращает результат
- `boolean isTerminated()` — делегирует к `ExecutorService.isTerminated()`

### ParallelProcessor

Параллельная обработка списка с сохранением порядка.

- `<T, R> List<R> processInParallel(List<T> items, Function<T, R> transform, int threads)` — создаёт пул на `threads` потоков, отправляет каждый элемент как `Callable`, собирает результаты в исходном порядке через список `Future`, после завершения вызывает `shutdown()`

### ScheduledTaskManager

Менеджер отложенных и периодических задач.

- `ScheduledTaskManager()` — создаёт `newScheduledThreadPool(2)`
- `ScheduledFuture<?> scheduleOnce(Runnable task, long delayMs)` — однократный запуск через `schedule()`
- `ScheduledFuture<?> scheduleRepeating(Runnable task, long initialDelayMs, long periodMs)` — периодический запуск через `scheduleAtFixedRate()`
- `void shutdown()` — завершает работу планировщика
