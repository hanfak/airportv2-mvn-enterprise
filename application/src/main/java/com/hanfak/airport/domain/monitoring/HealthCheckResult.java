package com.hanfak.airport.domain.monitoring;


import com.hanfak.airport.domain.helper.ValueType;

import java.util.List;


public class HealthCheckResult extends ValueType {

    private final List<ProbeResult> probeResults;

    public HealthCheckResult(List<ProbeResult> probeResults) {
        this.probeResults = probeResults;
    }

    public ProbeStatus getOverallStatus() {
        if (containsAny(ProbeStatus.FAIL)){
            return ProbeStatus.FAIL;
        }
        if (containsAny(ProbeStatus.WARN)){
            return ProbeStatus.WARN;
        }
        return ProbeStatus.OK;
    }

    private boolean containsAny(ProbeStatus probeStatus) {
        return probeResults.stream().anyMatch(probeResult -> probeResult.status.equals(probeStatus));
    }

    public List<ProbeResult> getProbeResults() {
        return probeResults;
    }
}
