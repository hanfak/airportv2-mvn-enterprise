package com.hanfak.airport.infrastructure.crosscutting;

import org.junit.Test;

import java.time.Duration;
import java.util.function.Supplier;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class GuavaSupplierCachingTest {

  private final Supplier supplier = mock(Supplier.class);
  private final GuavaSupplierCaching supplierCaching = new GuavaSupplierCaching();

  @Test
  public void cachesSupplierWithNonZeroDurationForTheCacheDuration() {
    Supplier cachedSupplier = supplierCaching.cacheForDuration(supplier, Duration.ofMillis(50));
    cachedSupplier.get();
    cachedSupplier.get();

    verify(supplier).get();
  }
}