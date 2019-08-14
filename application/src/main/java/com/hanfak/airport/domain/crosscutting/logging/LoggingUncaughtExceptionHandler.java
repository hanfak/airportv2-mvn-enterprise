package com.hanfak.airport.domain.crosscutting.logging;

import org.slf4j.Logger;

import static java.lang.String.format;

/**
 * Threads that are not managed by Jetty (e.g. {@link java.util.concurrent.Executor#execute(Runnable)}) will default to
 * writing to {@link System#err}, which is not what we want. Instead, we want to {@link Logger#error(String, Throwable)}.
 */
public class LoggingUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {

    private final Logger logger;

    public LoggingUncaughtExceptionHandler(Logger logger) {
        this.logger = logger;
    }

    @Override
    public void uncaughtException(Thread thread, Throwable uncaught) {
        logger.error(format("Uncaught exception in thread '%s'", thread.getName()), uncaught);
    }
}
