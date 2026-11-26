package com.multithreading.practice;

import java.util.concurrent.ConcurrentHashMap;

/**
 * ЗАДАЧА-ЛОВУШКА: учёт складских остатков под конкурентной нагрузкой.
 *
 * Использование ConcurrentHashMap само по себе НЕ делает составные операции
 * атомарными. Цель задания — увидеть, как «проверить-потом-действовать»
 * (check-then-act) теряет обновления и уводит остаток в минус, и переписать
 * методы на атомарные операции ConcurrentHashMap (merge / compute).
 *
 * --- КАК НЕ НАДО (именно это ловят тесты) ---
 *
 *   // addStock — теряет инкременты:
 *   Integer cur = stock.get(item);          // читаем
 *   stock.put(item, (cur == null ? 0 : cur) + qty); // ...между read и put другой
 *                                            // поток успел записать своё значение
 *
 *   // reserve — уходит в минус / резервирует больше, чем есть:
 *   Integer cur = stock.get(item);          // проверяем
 *   if (cur != null && cur >= qty) {         // ...здесь другой поток тоже прошёл проверку
 *       stock.put(item, cur - qty);          // оба списывают — двойное списание
 *       return true;
 *   }
 *   return false;
 *
 * --- КАК НАДО ---
 *
 *   addStock: stock.merge(item, qty, Integer::sum)
 *   reserve:  атомарно внутри одного stock.compute(item, (k, cur) -> ...),
 *             где решение «хватает ли остатка» и списание происходят под одной
 *             блокировкой bin. Признак успеха верните через внешний флаг.
 */
public class InventoryService {

    private final ConcurrentHashMap<String, Integer> stock = new ConcurrentHashMap<>();

    /**
     * Атомарно прибавляет qty к остатку товара item.
     * Используйте merge() — НЕ get()+put().
     */
    public void addStock(String item, int qty) {
    }

    /**
     * Атомарно резервирует qty единиц товара item.
     * Если остатка достаточно — уменьшает остаток и возвращает true.
     * Если не хватает — остаток не меняется, возвращает false.
     * Проверка и списание должны быть единой атомарной операцией (compute()).
     */
    public boolean reserve(String item, int qty) {
        return false;
    }

    /**
     * Текущий остаток товара (0, если товара нет).
     */
    public int getStock(String item) {
        return 0;
    }
}
