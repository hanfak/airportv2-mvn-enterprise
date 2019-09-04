package com.hanfak.airport.infrastructure.httpclient;

import java.time.Duration;
import java.time.Instant;

public class Timer {

    private final Instant startTime;

    private Timer(Instant startTime) {
        this.startTime = startTime;
    }

    public static Timer start() {
        return new Timer(Instant.now());
    }

    public Duration elapsedTime() {
        return Duration.between(startTime, Instant.now());
    }
}
