package com.github.javarar.limit.scheduler;

import lombok.Getter;
import lombok.SneakyThrows;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class LimitSchedulerThreadExecutor {

    @Getter
    private final ScheduledExecutorService executor;
    private final TimeUnit timeUnit;
    private final AtomicInteger counter;

    public LimitSchedulerThreadExecutor(int threads, int retryCount, TimeUnit timeUnit) {
        this.executor = Executors.newScheduledThreadPool(threads);
        this.counter = new AtomicInteger(retryCount);
        this.timeUnit = timeUnit;
    }

    public void runWithFixedRate(Runnable runnable, long initDelay, long delay) {
        executor.scheduleAtFixedRate(() -> {
            if (counter.getAndDecrement() > 0) {
                runnable.run();
            }
        }, initDelay, initDelay, timeUnit);
    }

    @SneakyThrows
    public void runWithFixedDelay(Runnable runnable, long initDelay, long delay) {
        executor.scheduleWithFixedDelay(() -> {
            if (counter.getAndDecrement() > 0) {
                runnable.run();
            }
        }, initDelay, initDelay, timeUnit);
    }
}
