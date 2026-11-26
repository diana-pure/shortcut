package com.multithreading.practice;

/**
 * Класс с внутренним массивом, который «ускользает» наружу через геттер.
 * Задача: исправьте getPlaylist() — возвращайте защитную копию (Arrays.copyOf),
 * а не прямую ссылку на внутренний массив.
 */
public class UnsafePlaylist {

    private String[] playlist = {"Ты так красива", "Районы-кварталы", "Выхода нет"};

    public String[] getPlaylist() {
        return playlist;
    }
}
