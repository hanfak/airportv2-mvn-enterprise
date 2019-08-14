package com.hanfak.airport.infrastructure.crosscutting;

import com.google.common.base.Suppliers;
import com.hanfak.airport.domain.crosscutting.SupplierCaching;

import java.time.Duration;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public class GuavaSupplierCaching implements SupplierCaching {

    @Override
    public <T> Supplier<T> cacheForDuration(Supplier<T> supplier, Duration duration) {
        if (duration.isZero()) {
            return supplier;
        }
        return Suppliers.memoizeWithExpiration(supplier::get, duration.toMillis(), TimeUnit.MILLISECONDS);
    }
}
