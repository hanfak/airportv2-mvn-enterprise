package com.hanfak.airport.domain.crosscutting;

import java.time.Duration;
import java.util.function.Supplier;

public interface SupplierCaching {
    <T> Supplier<T> cacheForDuration(Supplier<T> supplier, Duration duration);
}
