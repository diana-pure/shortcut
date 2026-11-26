# Практическое задание: Java Memory Model

> ⚠️ **Важно о тестах и природе багов JMM.** Ошибки модели памяти (потеря видимости,
> переупорядочивание, частичная публикация) **недетерминированы**: они зависят от JIT,
> архитектуры CPU и тайминга, поэтому надёжно «поймать» их юнит-тестом почти невозможно.
> Следствия для этого модуля:
>
> - Тесты `testVolatilePublication` / `testSynchronizedPublication` используют `future.get()`,
>   а возврат из `Future.get()` **сам создаёт happens-before** (правило `Future` из главы).
>   Поэтому они прошли бы и с обычным non-volatile полем — тесты проверяют *поведение*,
>   но **не доказывают необходимость** `volatile`/`synchronized`.
> - Тесты `testVolatileDCLSingleton` / `testSynchronizedSingleton` / `testHolderSingleton`
>   проверяют только `assertSame` (один экземпляр на 100 потоков). DCL **без** `volatile`
>   тоже почти всегда вернёт один экземпляр и тест пройдёт — тест не отличает корректный
>   DCL от некорректного.
>
> Реально «зависнуть/упасть» без правильной синхронизации способны лишь тесты на видимость
> (`testVolatileStopsThread`, `PiggybackPublisherTest`). Вывод: **эти задания тренируют
> правильное написание идиом (мышечную память), а не доказывают их необходимость.**
> «Необходимость» обоснована теорией главы 13 и наблюдательным заданием `ReorderingDemo`.

## Описание тестов

### testVolatileStopsThread
Проверяет `StopFlag`: запускает поток, который крутится в цикле `while (!flag.isStopped())`.
После вызова `requestStop()` поток должен остановиться. Без `volatile` JIT может закешировать
значение поля и поток никогда не увидит изменение — тест зависнет навсегда.

### testVolatilePublication
Проверяет `SafePublisher.publishWithVolatile()`: объект публикуется из одного потока
через volatile поле, читается из другого. Поле должно быть видимо сразу после записи.

### testSynchronizedPublication
Проверяет `SafePublisher.publishWithSynchronized()`: запись и чтение Config защищены
блоком `synchronized(this)`. Гарантирует happens-before между потоками.

### testEagerInitialization
Проверяет `SafePublisher.getEagerConfig()`: статическое поле `EAGER_CONFIG` инициализируется
при загрузке класса — безопасна без синхронизации, объект всегда не null.

### testSynchronizedSingleton
Проверяет `DoubleCheckedHolder.getSynchronizedInstance()`: 100 потоков одновременно
запрашивают экземпляр. Все должны получить ровно один и тот же объект.

### testVolatileDCLSingleton
Проверяет `DoubleCheckedHolder.getVolatileDCL()`: double-checked locking с volatile.
100 потоков — один экземпляр. Корректная и производительная реализация.

### testHolderSingleton
Проверяет `DoubleCheckedHolder.getHolderInstance()`: паттерн Initialization-on-demand holder.
JVM гарантирует однократную инициализацию статического поля внутреннего класса.

### NumberRangeTest
Проверяет `NumberRange`: атомарное обновление двух связанных полей (`lower`, `upper`)
с инвариантом `lower <= upper` через `AtomicReference<IntPair>` и CAS-цикл.
**Детерминированный** тест: 16 потоков конкурентно двигают границы, инвариант обязан
держаться; отдельные тесты проверяют отказ (`IllegalArgumentException`) при нарушении диапазона.

### PiggybackPublisherTest
Проверяет `PiggybackPublisher`: комбинирование (piggybacking) — публикация обычного
(non-volatile) поля `number` через volatile-флаг `ready`. `@RepeatedTest(50)` с таймаутом:
читатель, увидевший `ready == true`, обязан увидеть актуальный `number`. Без `volatile`
у флага тест может зависнуть или прочитать устаревший 0.

### ReorderingDemoTest
Проверяет `ReorderingDemo` (сценарий `PossibleReordering` из главы). **Наблюдательное**
задание: 100 000 прогонов двух несинхронизированных потоков; тест проверяет лишь
валидность исходов и печатает, сколько раз встретился «невозможный» результат `(0,0)`.
Assert на наличие `(0,0)` намеренно отсутствует — на сильно упорядоченной x86 он может
не появиться ни разу, на слабых ARM/PowerPC встречается чаще.

## Классы для реализации

- **StopFlag** — поле `volatile boolean stopped`, методы `requestStop()` и `isStopped()`

- **SafePublisher** — три способа безопасной публикации:
  - `publishWithVolatile(Config)` / `getVolatileConfig()` — через volatile поле
  - `publishWithSynchronized(Config)` / `getSynchronizedConfig()` — через synchronized
  - `getEagerConfig()` — через статическую финальную константу

- **DoubleCheckedHolder** — три паттерна ленивой инициализации синглтона:
  - `getSynchronizedInstance()` — synchronized метод
  - `getVolatileDCL()` — double-checked locking с volatile
  - `getHolderInstance()` — Initialization-on-demand holder (внутренний класс Holder)

- **NumberRange** — атомарное обновление двух полей через `AtomicReference<IntPair>`:
  - `getLower()` / `getUpper()` — чтение через `values.get()`
  - `setLower(int)` / `setUpper(int)` — CAS-цикл: снимок → проверка инварианта → новый
    `IntPair` → `compareAndSet` → повтор при промахе

- **PiggybackPublisher** — комбинирование (piggybacking):
  - `publish(int)` — `number = value; ready = true;` (порядок важен!)
  - `isReady()` / `read()` — `number` остаётся non-volatile, `ready` объявить `volatile`

- **ReorderingDemo** — демонстрация переупорядочивания:
  - `runOnce()` — запускает два несинхронизированных потока (`a=1; x=b;` и `b=1; y=a;`)
    и возвращает наблюдённую пару `{x, y}`
