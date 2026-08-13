package com.skills.gc;

/**
 * Слушатель событий о новых заказах.
 */
public interface OrderListener {

    void onOrder(Order order);
}

