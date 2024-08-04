package com.github.javarar.rejected.task;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.*;

@RequiredArgsConstructor
public class DelayedOnRejectedThreadExecutor {

    private final Executor singleExecutor = Executors.newSingleThreadScheduledExecutor();
    private final BlockingQueue<Runnable> workQueue;
    private final BlockingQueue<Runnable> delayedWorkQueue = new LinkedBlockingQueue<>();

    @Getter
    private final ThreadPoolExecutor threadPoolExecutor;

    private boolean isSingleExecutorRunning;

    public DelayedOnRejectedThreadExecutor(
            int corePoolSize, int maximumPoolSize, long keepAliveTime,
            TimeUnit unit, int workQueueSize, int latchCount
    ) {
        workQueue = new ArrayBlockingQueue<>(workQueueSize);
        RejectedExecutionHandler myHandler = (runnable, executor) -> {
            try {
                if (executor.getQueue().size() > workQueueSize) {
                    executor.getQueue().put(runnable);
                } else {
                    delayedWorkQueue.put(runnable);
                }
            } catch (InterruptedException e) {
                isSingleExecutorRunning = false;
                throw new RuntimeException(e);
            }
        };

        threadPoolExecutor = new ThreadPoolExecutor(
                corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue,
                Executors.defaultThreadFactory(), myHandler
        );

        runSingleExecutor();
    }

    private void runSingleExecutor() {
        isSingleExecutorRunning = true;
        singleExecutor.execute(() -> {
            while (isSingleExecutorRunning) {
                try {
                    if (!delayedWorkQueue.isEmpty()) {
                        Runnable runnable = delayedWorkQueue.take();
                        threadPoolExecutor.execute(runnable);
                    }
                } catch (InterruptedException e) {
                    isSingleExecutorRunning = false;
                    throw new RuntimeException(e);
                }

            }
        });
    }
}
