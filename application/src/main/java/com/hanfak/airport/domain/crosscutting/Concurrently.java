package com.hanfak.airport.domain.crosscutting;

import java.util.List;
import java.util.concurrent.Callable;

public interface Concurrently {
    <T> List<T> execute(List<? extends Callable<T>> toExecute);
}
