# Практическое задание: Конкурентные коллекции

## Описание тестов

### testConcurrentCachePutGet
Проверяет потокобезопасность `ConcurrentCache`: пул из 10 потоков выполняет 100 задач, каждая записывает один элемент — итоговый размер должен быть ровно 100.

### testGetOrCompute
Проверяет, что `computeIfAbsent` не перезаписывает уже существующее значение.

### testComputeValue
Проверяет атомарное обновление через `compute()`: счётчик должен быть ровно 2 после двух вызовов.

### testMergeValue
Проверяет атомарное слияние значений через `merge()`: три слагаемых должны дать корректную сумму.

### testIncrementCounter
Проверяет инкремент счётчиков для двух независимых ключей.

### testProducerConsumer
Проверяет паттерн "Производитель-Потребитель": производитель кладёт 20 элементов, потребитель забирает 20 — счётчики должны совпасть.

### testEventListenerRegistry
Проверяет `CopyOnWriteArrayList`: 5 потоков параллельно рассылают события двум слушателям — итого 10 вызовов.

## Задача-ловушка: InventoryService (`InventoryServiceTrapTest`)

Цель — на практике прочувствовать, что `ConcurrentHashMap` **не** делает составные операции атомарными. Наивная реализация через `get()`+`put()` (check-then-act) валит оба теста; проходит только реализация на атомарных `merge()`/`compute()`.

### testConcurrentAddStock
10 потоков × 10 000 прибавок к одному ключу. Наивный `get`+`put` теряет обновления (получается ~11 000 вместо 100 000) — ловит lost update. Решение: `merge(item, qty, Integer::sum)`.

### testConcurrentReserveNeverOversells
1 000 единиц в наличии, 2 000 потоков пытаются зарезервировать по 1. Наивная проверка-затем-списание допускает двойное списание (>1 000 успешных, остаток в минусе). Решение: проверка и списание внутри одного `compute()`.

## Задача: DelayedScheduler (`DelayedSchedulerTest`)

Планировщик отложенных задач на `DelayQueue`. Нужно реализовать обёртку `Delayed` (`getDelay`/`compareTo`) и методы `schedule`/`take`/`poll`/`size`.

### testOrderByReadiness
Задачи извлекаются в порядке готовности, а не вставки: «поздняя» вставлена первой, но `take()` сначала вернёт задачу с меньшей задержкой.

### testPollReturnsNullBeforeDelay
`poll()` возвращает `null`, пока задержка не истекла; неготовая задача остаётся в очереди (`size()==1`).

### testTakeBlocksUntilReady
`take()` блокируется до истечения задержки (~150 мс) и только потом возвращает задачу.

## Классы для реализации

- **ConcurrentCache** — кэш на основе `ConcurrentHashMap` с методами `put`, `get`, `getOrCompute`, `computeValue`, `mergeValue`, `incrementCounter`, `getCounter`, `size`
- **ProducerConsumer** — паттерн на основе `BlockingQueue` с методами `produce(count)`, `consume(count)`, `getProducedCount`, `getConsumedCount`
- **EventListenerRegistry** — реестр на основе `CopyOnWriteArrayList` с методами `addListener`, `removeListener`, `fireEvent`, `getListenerCount`
- **InventoryService** (задача-ловушка) — учёт остатков на `ConcurrentHashMap` с методами `addStock`, `reserve`, `getStock`; реализовать через атомарные `merge`/`compute`, а не `get`+`put`
- **DelayedScheduler** — планировщик на `DelayQueue` с методами `schedule`, `take`, `poll`, `size`; реализовать обёртку `Delayed` (`getDelay`/`compareTo`)
