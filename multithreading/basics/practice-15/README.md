# Практическое задание: Project Reactor

## Описание тестов

### testMonoCreation
Проверяет `createMono("Hello")`: поток должен содержать ровно одно значение.

### testFluxCreation
Проверяет `createFlux(1,2,3,4,5)`: поток должен содержать 5 элементов в исходном порядке.

### testMapOperator
Проверяет `transformToUpperCase`: `["hello", "world"]` → `["HELLO", "WORLD"]`.

### testFilterOperator
Проверяет `filterEvenNumbers`: `[1,2,3,4,5,6]` → `[2,4,6]`.

### testFlatMap
Проверяет `expandWithFlatMap`: `[1,2,3]` → 6 элементов (каждый `n` разворачивается в `[1,..,n]`).

### testMerge
Проверяет `mergeFluxes`: два потока по 2 элемента объединяются в 4.

### testZip
Проверяет `zipFluxes`: `["Alice","Bob"]` + `[25,30]` → `["Alice is 25", "Bob is 30"]`.

### testErrorHandlingWithReturn
Проверяет `handleErrorWithReturn`: поток `["A","B",ERROR]` → `["A","B","Error"]`.

### testOnErrorResume
Проверяет `handleErrorWithResume`: при ошибке поток переключается на recovery.

### testConcat
Проверяет `concatFluxes`: `[1,3,5]` затем `[2,4,6]` строго последовательно → `[1,3,5,2,4,6]`.

### testConcatMapPreservesOrder
Проверяет `transformWithConcatMap`: как `flatMap`, но порядок сохраняется → `[1, 1,2, 1,2,3]`.

### testSubscribeOnBoundedElastic
Проверяет `runOnBoundedElastic`: блокирующий вызов выполняется на потоке пула `boundedElastic`.

### testRetryOnError
Проверяет `retryOnError`: источник падает дважды, успех с третьей попытки при `retry(3)`.

## Классы для реализации

- **ReactiveProcessor** — реализуйте 13 методов, каждый возвращает `Mono<T>` или `Flux<T>`.
  Все методы должны быть **ленивыми** (без `subscribe()` внутри реализации):
  - `createMono(T)` — `Mono.just`
  - `createFlux(T...)` — `Flux.just`
  - `transformToUpperCase(Flux<String>)` — `map`
  - `filterEvenNumbers(Flux<Integer>)` — `filter`
  - `expandWithFlatMap(Flux<Integer>)` — `flatMap` + `Flux.range`
  - `mergeFluxes(Flux<T>, Flux<T>)` — `Flux.merge`
  - `zipFluxes(Flux<String>, Flux<Integer>)` — `Flux.zip`
  - `handleErrorWithReturn(Flux<String>, String)` — `onErrorReturn`
  - `handleErrorWithResume(Flux<String>, Flux<String>)` — `onErrorResume`
  - `concatFluxes(Flux<T>, Flux<T>)` — `Flux.concat`
  - `transformWithConcatMap(Flux<Integer>)` — `concatMap` + `Flux.range`
  - `runOnBoundedElastic(Supplier<String>)` — `Mono.fromSupplier` + `subscribeOn(Schedulers.boundedElastic())`
  - `retryOnError(Flux<T>, int)` — `retry(n)`

> **Зависимости:** `io.projectreactor:reactor-core:3.7.5` и `io.projectreactor:reactor-test:3.7.5`
