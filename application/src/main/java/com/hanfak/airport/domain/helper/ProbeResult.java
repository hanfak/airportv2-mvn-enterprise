package com.hanfak.airport.domain.helper;

public class ProbeResult extends ValueType {
    public final String name;
    public final String description;
    public final ProbeStatus status;

    private ProbeResult(String name, String description, ProbeStatus status) {
        this.name = name;
        this.description = description;
        this.status = status;
    }

    public static ProbeResult success(String name, String description) {
        return new ProbeResult(name, description, ProbeStatus.OK);
    }

    public static ProbeResult failure(String name, String description) {
        return new ProbeResult(name, description, ProbeStatus.FAIL);
    }

    static ProbeResult warn(String name, String description) {
        return new ProbeResult(name, description, ProbeStatus.WARN);
    }
}
