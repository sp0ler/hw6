package com.github.javarar.rejected.task;

import org.junit.jupiter.api.Test;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class DelayedOnRejectedThreadExecutorTest {

    private final AtomicInteger counter = new AtomicInteger(0);

    @Test
    void testExecutor() throws InterruptedException {
        final int corePoolSize = 1;
        final int maximumPoolSize = 1;
        final long keepAliveTime = 0L;
        final TimeUnit unit = TimeUnit.SECONDS;
        final int workQueueSize = 1;

        var executor = new DelayedOnRejectedThreadExecutor(
                corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueueSize, 2
        ).getThreadPoolExecutor();

        for (int i = 0; i < 2; i++) {
            executor.submit(this::increment);
        }

        executor.awaitTermination(10L, unit);
        executor.shutdown();

        assertAll(
                () -> assertEquals(2, executor.getCompletedTaskCount()),
                () -> assertEquals(2, counter.get())
        );
    }

    private void increment() {
        try {
            Thread.sleep(5_000);
            counter.getAndIncrement();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
