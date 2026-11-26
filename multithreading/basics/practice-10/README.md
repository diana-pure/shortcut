# Practice 10 — Производительность и масштабируемость

## Задача

Реализовать классы, демонстрирующие приёмы повышения масштабируемости из главы 10:
параллельное суммирование, lock striping, сужение области блокировки, lock splitting,
устранение «горячего поля» через `LongAdder`. Изучить закон Амдала на готовом примере.

## Тесты

`PerformanceScalabilityTest` — базовые задачи:

| # | Тест | Описание |
|---|------|----------|
| 1 | `testParallelSumCorrectness` | Параллельная сумма (4 и 8 потоков) даёт тот же результат, что последовательная, для 100 000 элементов |
| 2 | `testParallelSumSmall` | Параллельная сумма массива [1..100] = 5050 (1 и 2 потока) |
| 3 | `testStripedCounterConcurrency` | 10 потоков по 1000 инкрементов одного ключа — счётчик = 10 000 |
| 4 | `testStripedCounterIndependence` | Четыре разных ключа получают независимые счётчики по 500 |
| 5 | `testAmdahlSpeedup` | Закон Амдала: при S=0.1 и N=4 ускорение ≈ 3.077 |
| 6 | `testAmdahlMaxSpeedup` | Предельное ускорение при S=0.2 равно 5.0 (1/S) |

`AdvancedScalabilityTest` — приёмы уменьшения конкуренции:

| # | Тест | Описание |
|---|------|----------|
| 7 | `testScopedLockCacheCachesValue` | Значение вычисляется один раз и затем берётся из кэша |
| 8 | `testScopedLockCacheComputesOutsideLock` | Дорогое вычисление идёт вне блокировки — два потока входят в него параллельно (проверка через `CyclicBarrier`) |
| 9 | `testSplitLocksAreIndependent` | Удержание блокировки `users` не мешает операции с `queries` |
| 10 | `testSplitLockConcurrentAdds` | Конкурентные добавления в `users` и `queries` не теряются |
| 11 | `testLongAdderConcurrency` | 16 потоков по 100 000 инкрементов = 1 600 000 без потерь |
| 12 | `testLongAdderAddAndReset` | `add` накапливает, `reset` обнуляет |
| 13 | `testReadWriteCacheBasic` | `put` сохраняет, `get` читает |
| 14 | `testReadWriteCacheConcurrentReaders` | Три читателя входят в `readLock` параллельно (`CyclicBarrier`) |
| 15 | `testReadWriteCacheConcurrentReadWrite` | Конкурентные читатели и писатели не теряют записи |
| 16 | `testAsyncLoggerBasic` | После `start`/`log`/`stop` сообщение записано |
| 17 | `testAsyncLoggerNoMessagesLost` | Конкурентная запись + корректный `stop` — без потерь и дублей |

## Классы

### ParallelArraySum — **реализовать**

- `sequentialSum(long[])` — обычный цикл.
- `parallelSum(long[], int threads)` — делит массив на `threads` отрезков, каждый считает через `Callable<Long>` в `Executors.newFixedThreadPool(threads)`, результаты собирает через `Future<Long>`.

### StripedCounter — **реализовать**

Счётчик с разделением блокировок по полосам: `ReentrantLock[]` + `ConcurrentHashMap`.
Полоса для ключа: `Math.abs(key.hashCode()) % stripeCount`. `increment` блокирует только нужную полосу.

### ScopedLockCache — **реализовать** (сужение области блокировки, «get in, get out»)

`getOrCompute(key, expensiveCompute)`: под блокировкой — только доступ к `Map`,
дорогое вычисление `expensiveCompute` — **вне** блокировки. Это позволяет нескольким
потокам вычислять значения параллельно, а не сериализоваться на одном мониторе.

### SplitLockServerStatus — **реализовать** (lock splitting)

Два независимых набора — `users` и `queries` — под **разными** блокировками.
Контракт: синхронизироваться на самих коллекциях (`synchronized(users)` / `synchronized(queries)`),
не на общем `this`. Тогда операции с разными наборами не конкурируют.

### LongAdderCounter — **реализовать** (устранение «горячего поля»)

Счётчик на `LongAdder`: каждый поток пишет в свою ячейку, `sum()` складывает их.
Под высокой конкуренцией масштабируется лучше `AtomicLong` (нет битвы за один CAS).
Эксперимент: замените `LongAdder` на `AtomicLong` и сравните время `testLongAdderConcurrency`.

### ReadWriteCache — **реализовать** (ReadWriteLock вместо монопольной блокировки, §4.5)

Кэш на `ReentrantReadWriteLock` для сценария «много чтений, редкие записи».
`get`/`size`/`readSnapshot` — под `readLock` (читатели параллельны), `put` — под `writeLock`
(монопольно). Не оборачивайте чтение в `writeLock` — потеряете параллелизм читателей.

### AsyncLogger — **реализовать** (вынос I/O в отдельный поток, §6)

`log` лишь кладёт сообщение в очередь (producer не блокируется на I/O), отдельный поток-
потребитель пишет их. Главная ловушка — корректный `stop`: нужно дописать **все** принятые
сообщения, не потеряв хвост очереди (флаг + `worker.join()`, цикл дренирует очередь до конца).

### AmdahlCalculator — **уже реализован**, изучите

`Speedup = 1 / (S + (1 - S) / N)`. При фиксированной доле последовательного кода `S`
ускорение ограничено сверху значением `1/S` независимо от числа процессоров.
