# Практическое задание: Синхронизаторы

## Описание тестов

### testRaceCoordinatorStartAndFinish
Проверяет `CountDownLatch`: организатор даёт старт, все 5 участников стартуют и регистрируют финиш. Метод `awaitAllFinish()` должен вернуть управление только после финиша всех.

### testConnectionPoolLimit
Проверяет `Semaphore`: одновременно активных соединений не должно быть больше 3, несмотря на то что 10 потоков соперничают за доступ.

### testConnectionPoolCapacity
Проверяет, что `getMaxConnections()` и `getActiveCount()` возвращают корректные значения.

### testPhaseProcessor
Проверяет `CyclicBarrier`: 4 потока выполняют 3 фазы — барьер синхронизирует каждую фазу. `getCompletedPhases()` должен вернуть 3.

### testCountDownLatchStartGun
Проверяет "стартовый пистолет": 5 потоков ждут `CountDownLatch(1)`. До `countDown()` ни один не продвинулся; после — все выполнились.

## Классы для реализации

- **RaceCoordinator** — координатор старта/финиша на двух `CountDownLatch` с методами `readyUp`, `awaitStart`, `startRace`, `finish`, `awaitAllFinish`
- **ConnectionPool** — пул на `Semaphore` с методами `acquire`, `release`, `getActiveCount`, `getMaxConnections`
- **PhaseProcessor** — поэтапный процессор на `CyclicBarrier` с методами `runPhase(threadIndex, action)`, `getCompletedPhases`
