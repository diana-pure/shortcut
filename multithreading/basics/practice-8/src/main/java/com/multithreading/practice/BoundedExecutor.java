package com.multithreading.practice;

import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.Semaphore;

/**
 * Задание 4. Ограничение частоты отправки задач через Semaphore (блокировка вместо отказа).
 *
 * В JDK нет готовой политики насыщения, которая БЛОКИРУЕТ отправителя при заполнении.
 * Похожий эффект даёт обёртка с семафором: разрешение захватывается перед отправкой и
 * освобождается по завершении задачи. Когда разрешений нет — submitTask блокируется,
 * ограничивая число одновременно отправленных + выполняемых задач (BoundedExecutor из главы 8).
 *
 * Семафор уже создан с bound разрешениями. Реализуйте submitTask(Runnable command):
 *
 * 1. semaphore.acquire() — блокируется, пока нет свободного разрешения;
 * 2. в блоке try отправьте в exec обёртку над command, которая в finally
 *    вызывает semaphore.release() (разрешение освобождается ПОСЛЕ выполнения задачи);
 * 3. если exec.execute(...) бросил RejectedExecutionException — освободите разрешение
 *    в catch (semaphore.release()), иначе оно «утечёт».
 *
 * Метод объявлен throws InterruptedException (его бросает acquire()).
 */
public class BoundedExecutor {

    private final Executor exec;
    private final Semaphore semaphore;

    public BoundedExecutor(Executor exec, int bound) {
        this.exec = exec;
        this.semaphore = new Semaphore(bound);
    }

    public void submitTask(Runnable command) throws InterruptedException {
    }
}
