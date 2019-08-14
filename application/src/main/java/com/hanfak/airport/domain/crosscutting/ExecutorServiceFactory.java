package com.hanfak.airport.domain.crosscutting;

import java.util.concurrent.ExecutorService;

public interface ExecutorServiceFactory {
    ExecutorService newFixedThreadPool(int size);
}
