package com.multithreading.practice;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Потокобезопасный кэш с использованием ReadWriteLock.
 *
 * Реализуйте методы put(K, V) и get(K) так, чтобы несколько читателей
 * могли работать одновременно, а запись была эксклюзивной.
 */
public class ThreadSafeCache<K, V> {

}
