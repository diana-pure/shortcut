# Практическое задание: Synchronized и механизмы синхронизации

## Описание тестов

### testSynchronizedCounter
Проверяет потокобезопасный счетчик с использованием `synchronized`. `SynchronizedCounter` должен корректно работать в многопоточной среде. Тест дополнительно проверяет через рефлексию, что счётчик реализован именно на `synchronized`, а не в обход через `Atomic*`/`Lock`.

### testAtomicCounter
Проверяет потокобезопасный счетчик с использованием `AtomicInteger`. `AtomicCounter` должен использовать поле из пакета `java.util.concurrent.atomic` (проверяется рефлексией).

### testReentrantLockCounter
Проверяет счетчик с использованием `ReentrantLock`. `LockCounter` должен использовать поле типа `Lock` (проверяется рефлексией).

### testReadWriteLockCache
Проверяет кэш с использованием `ReadWriteLock`. `ThreadSafeCache` должен использовать поле типа `ReadWriteLock` и не терять данные при параллельных записи и чтении — тест проверяет все записанные значения, а не одно.

### testBoundedBufferFifoUnderLoad / testPutBlocksWhenFull / testTakeBlocksWhenEmpty
Проверяют ограниченный буфер Producer-Consumer на `wait()`/`notifyAll()`. `BoundedBuffer` должен соблюдать FIFO без потерь под нагрузкой, блокировать `put()` на заполненном буфере и `take()` на пустом. Рефлексией проверяется, что не используются классы из `java.util.concurrent` (только `synchronized` + `wait`/`notify`).

## Классы для реализации

- **SynchronizedCounter**: Потокобезопасный счетчик с использованием synchronized
- **AtomicCounter**: Потокобезопасный счетчик с использованием AtomicInteger
- **LockCounter**: Счетчик с использованием ReentrantLock
- **ThreadSafeCache**: Кэш с использованием ReadWriteLock
- **BoundedBuffer**: Ограниченный буфер Producer-Consumer с использованием wait()/notifyAll()
