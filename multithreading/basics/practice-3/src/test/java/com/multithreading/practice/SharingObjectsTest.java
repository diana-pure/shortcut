package com.multithreading.practice;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Задача: реализуйте классы так, чтобы все тесты проходили.
 * Не изменяйте этот файл.
 */
class SharingObjectsTest {

    @Test
    @Timeout(10)
    @DisplayName("MutableValue: методы getValue/setValue объявлены synchronized и значение читается корректно")
    void testMutableValueSynchronization() throws Exception {
        // Проверяем именно наличие синхронизации, а не только результат:
        // функциональный тест на int прошёл бы и без неё (запись int атомарна,
        // а CountDownLatch сам создаёт happens-before), поэтому проверяем модификатор.
        Method getter = MutableValue.class.getDeclaredMethod("getValue");
        Method setter = MutableValue.class.getDeclaredMethod("setValue", int.class);

        assertTrue(Modifier.isSynchronized(getter.getModifiers()),
                "getValue() должен быть объявлен synchronized — иначе читающий поток может увидеть устаревшее значение");
        assertTrue(Modifier.isSynchronized(setter.getModifiers()),
                "setValue() должен быть объявлен synchronized — синхронизации только одного метода недостаточно");

        // Функциональная проверка корректности
        MutableValue value = new MutableValue();
        int threadCount = 10;
        CountDownLatch latch = new CountDownLatch(threadCount);
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);

        for (int i = 0; i < threadCount; i++) {
            final int val = i;
            executor.submit(() -> {
                try {
                    value.setValue(val);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executor.shutdown();

        int result = value.getValue();
        assertTrue(result >= 0 && result < threadCount,
                "getValue() должен возвращать одно из записанных значений, а не мусор");
    }

    @Test
    @DisplayName("UnsafePlaylist: getPlaylist() возвращает защитную копию, а не оригинальный массив")
    void testUnsafePlaylistReturnsDefensiveCopy() {
        UnsafePlaylist playlist = new UnsafePlaylist();

        String[] returned = playlist.getPlaylist();
        returned[0] = "MODIFIED";

        assertNotEquals("MODIFIED", playlist.getPlaylist()[0],
                "getPlaylist() должен возвращать копию: изменение возвращённого массива не должно влиять на внутренний");
    }

    @Test
    @Timeout(10)
    @DisplayName("ThreadLocalCounter: каждый поток имеет свой изолированный счётчик")
    void testThreadLocalIsolation() throws InterruptedException {
        ThreadLocalCounter counter = new ThreadLocalCounter();
        int[] threadResults = new int[3];
        CountDownLatch start = new CountDownLatch(1);
        CountDownLatch done = new CountDownLatch(3);

        for (int i = 0; i < 3; i++) {
            final int threadId = i;
            new Thread(() -> {
                try {
                    start.await();
                    for (int j = 0; j <= threadId; j++) {
                        counter.increment();
                    }
                    threadResults[threadId] = counter.get();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    done.countDown();
                }
            }).start();
        }

        start.countDown();
        done.await();

        assertEquals(1, threadResults[0], "Поток 0 сделал 1 инкремент — счётчик должен быть 1");
        assertEquals(2, threadResults[1], "Поток 1 сделал 2 инкремента — счётчик должен быть 2");
        assertEquals(3, threadResults[2], "Поток 2 сделал 3 инкремента — счётчик должен быть 3");
    }

    @Test
    @Timeout(10)
    @DisplayName("ThreadLocalCounter: reset() очищает значение через remove(), предотвращая утечку между задачами в пуле")
    void testThreadLocalRemovePreventsLeak() throws Exception {
        // Пул из одного потока гарантирует переиспользование того же потока для обеих задач.
        ThreadLocalCounter counter = new ThreadLocalCounter();
        ExecutorService single = Executors.newSingleThreadExecutor();
        try {
            // Задача 1: насчитываем 5 и очищаем ThreadLocal через reset()
            single.submit(() -> {
                for (int i = 0; i < 5; i++) {
                    counter.increment();
                }
                counter.reset();
            }).get();

            // Задача 2 на ТОМ ЖЕ потоке: значение не должно «протечь» из задачи 1
            int leaked = single.submit(counter::get).get();

            assertEquals(0, leaked,
                    "После reset() (ThreadLocal.remove()) переиспользованный поток должен начинать с 0 — "
                            + "иначе значение из предыдущей задачи протекает в следующую");
        } finally {
            single.shutdown();
        }
    }

    @Test
    @DisplayName("ImmutablePoint: все поля final, сеттеров нет, значения корректны")
    void testImmutablePoint() {
        ImmutablePoint point = new ImmutablePoint(3, 4);

        assertEquals(3, point.getX(), "getX() должен вернуть 3");
        assertEquals(4, point.getY(), "getY() должен вернуть 4");

        for (Field field : ImmutablePoint.class.getDeclaredFields()) {
            assertTrue(Modifier.isFinal(field.getModifiers()),
                    "Поле '" + field.getName() + "' должно быть объявлено final");
        }

        boolean hasSetters = Arrays.stream(ImmutablePoint.class.getMethods())
                .anyMatch(m -> m.getName().startsWith("set"));
        assertFalse(hasSetters, "ImmutablePoint не должен содержать сеттеров");
    }

    @Test
    @Timeout(10)
    @DisplayName("SafeListener: слушатель регистрируется только после полного конструирования (нет this-escape)")
    void testSafeListenerNoThisEscape() {
        // Источник «стреляет» событием прямо в момент регистрации слушателя.
        // Если слушатель зарегистрирован из незавершённого конструктора,
        // обработчик увидит поле name ещё не инициализированным (null).
        Event event = new Event("CLICK");
        EventSource source = new EventSource(event);

        SafeListener listener = SafeListener.newInstance(source, "Alice");

        assertEquals(List.of("Alice:CLICK"), listener.getReceived(),
                "В момент срабатывания слушателя объект должен быть полностью сконструирован "
                        + "(name = \"Alice\", а не null) — регистрацию нужно выносить в фабричный метод");
    }

    @Test
    @DisplayName("SafeListener: конструктор приватный, публикация — только через фабричный метод")
    void testSafeListenerHasPrivateConstructor() {
        boolean allConstructorsPrivate = Arrays.stream(SafeListener.class.getDeclaredConstructors())
                .allMatch(c -> Modifier.isPrivate(c.getModifiers()));

        assertTrue(allConstructorsPrivate,
                "Все конструкторы SafeListener должны быть приватными — объект публикуется только через newInstance()");

        boolean hasStaticFactory = Arrays.stream(SafeListener.class.getDeclaredMethods())
                .anyMatch(m -> m.getName().equals("newInstance")
                        && Modifier.isStatic(m.getModifiers())
                        && Modifier.isPublic(m.getModifiers()));

        assertTrue(hasStaticFactory, "Должен быть публичный статический фабричный метод newInstance()");
    }

    @Test
    @Timeout(10)
    @DisplayName("SafeSingleton: под нагрузкой создаётся ровно один экземпляр")
    void testSafeSingletonSingleInstance() throws InterruptedException {
        int threadCount = 64;
        CountDownLatch start = new CountDownLatch(1);
        CountDownLatch done = new CountDownLatch(threadCount);
        Set<SafeSingleton> instances = Collections.newSetFromMap(new ConcurrentHashMap<>());
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executor.submit(() -> {
                try {
                    start.await();
                    instances.add(SafeSingleton.getInstance());
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    done.countDown();
                }
            });
        }

        start.countDown();        // одновременный старт максимизирует гонку
        done.await();
        executor.shutdown();

        assertEquals(1, instances.size(),
                "getInstance() должен всегда возвращать один и тот же экземпляр");
        assertSame(SafeSingleton.getInstance(), instances.iterator().next(),
                "Экземпляр из потоков должен совпадать с возвращаемым повторно");
    }

    @Test
    @DisplayName("SafeSingleton: поле instance объявлено volatile (корректный double-checked locking)")
    void testSafeSingletonInstanceIsVolatile() {
        Field instanceField = Arrays.stream(SafeSingleton.class.getDeclaredFields())
                .filter(f -> Modifier.isStatic(f.getModifiers()))
                .filter(f -> f.getType().equals(SafeSingleton.class))
                .findFirst()
                .orElseThrow(() -> new AssertionError(
                        "Не найдено статическое поле типа SafeSingleton для хранения экземпляра"));

        assertTrue(Modifier.isVolatile(instanceField.getModifiers()),
                "Поле '" + instanceField.getName() + "' должно быть volatile — иначе DCL небезопасен: "
                        + "другой поток может получить ссылку на частично сконструированный объект");
    }
}
