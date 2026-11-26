# Практическое задание: Виртуальные потоки (Java 21+)

> **Требования:** Java 21+

## Описание тестов

### testExecuteWithVirtualThreads
Проверяет `VirtualThreadProcessor.executeWithVirtualThreads()`: 1000 задач с `Thread.sleep(1)` должны выполниться через `newVirtualThreadPerTaskExecutor()`. Возвращаемое значение — количество завершённых задач.

### testExecuteAndCollect
Проверяет `executeAndCollect()`: 10 Callable возвращают `i * 2`, результаты должны быть в исходном порядке.

### testVirtualThreadIsVirtual
Проверяет `isVirtualThread()`: поток, созданный через `Thread.ofVirtual()`, должен возвращать `true` из `isVirtual()`.

### testPlatformThreadIsNotVirtual
Проверяет, что обычный поток (`Thread.ofPlatform()`) не является виртуальным.

### testVirtualThreadsFasterThanFixedPool
Сравнивает время: 200 запросов по 50 мс задержки. Виртуальные потоки должны завершиться значительно быстрее фиксированного пула из 10 потоков (200 × 50ms ÷ 10 = 1000 мс минимум для фиксированного, ~50 мс для виртуальных).

## Классы для реализации

- **VirtualThreadProcessor** — три метода:
  - `executeWithVirtualThreads(List<Runnable>)` → int (количество выполненных)
  - `executeAndCollect(List<Callable<T>>)` → List<T> (результаты в порядке submission)
  - `isVirtualThread(Thread)` → boolean

- **IoSimulator** — два метода:
  - `simulateWithVirtualThreads(int, int)` → long (мс)
  - `simulateWithFixedPool(int, int, int)` → long (мс)
