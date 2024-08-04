package com.github.javarar.limit.scheduler;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Log4j2
public class LimitSchedulerThreadExecutorTest {

    private static AtomicInteger counter = new AtomicInteger(0);

    @BeforeEach
    void init() {
        counter.set(0);
    }

    @Test
    void runWithFixedDelayTest() throws InterruptedException {
        var executor = new LimitSchedulerThreadExecutor(1, 3, TimeUnit.MILLISECONDS);
        executor.runWithFixedDelay(this::increment, 100, 1000);

        Thread.sleep(5000);

        Assertions.assertEquals(3, counter.get());
    }

    @Test
    void runWithFixedRateTest() throws InterruptedException {
        var executor = new LimitSchedulerThreadExecutor(1, 3, TimeUnit.MILLISECONDS);
        executor.runWithFixedRate(this::increment, 100, 1000);

        Thread.sleep(5000);

        Assertions.assertEquals(3, counter.get());
    }

    private void increment() {
        log.info("Задача №{}", counter.getAndIncrement());
    }
}
