package com.hanfak.airport.infrastructure.crosscutting;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.hanfak.airport.domain.crosscutting.ExecutorServiceFactory;
import com.hanfak.airport.domain.crosscutting.logging.LoggingUncaughtExceptionHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import static java.util.concurrent.TimeUnit.MINUTES;

public class TrackingExecutorServiceFactory implements ExecutorServiceFactory {

    private final ThreadFactory threadFactory;

    private List<ExecutorService> tracked = new ArrayList<>();

    public TrackingExecutorServiceFactory(LoggingUncaughtExceptionHandler loggingUncaughtExceptionHandler) {
        this.threadFactory = new ThreadFactoryBuilder()
                .setUncaughtExceptionHandler(loggingUncaughtExceptionHandler)
                .setNameFormat("hydra-executor-thread-%d") // TODO: MON-256 should this name be a constructor parameter so we can be more specific about who created the threads?
                .build();
    }

    @Override
    public ExecutorService newFixedThreadPool(int size) {
        ExecutorService executorService = Executors.newFixedThreadPool(size, threadFactory);
        tracked.add(executorService);
        return executorService;
    }

    void shutdown() throws InterruptedException {
        for (ExecutorService executorService : tracked) {
            executorService.shutdown();
        }
        for (ExecutorService executorService : tracked) {
            executorService.awaitTermination(1, MINUTES);
        }
    }
}
