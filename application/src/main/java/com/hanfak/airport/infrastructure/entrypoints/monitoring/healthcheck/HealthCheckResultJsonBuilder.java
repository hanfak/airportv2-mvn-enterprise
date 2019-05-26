package com.hanfak.airport.infrastructure.entrypoints.monitoring.healthcheck;

import com.hanfak.airport.domain.monitoring.HealthCheckResult;
import com.hanfak.airport.domain.monitoring.ProbeResult;
import org.json.JSONObject;

import java.util.List;

import static java.util.Collections.singletonMap;
import static java.util.stream.Collectors.toList;

public class HealthCheckResultJsonBuilder {
    // TODO display application version
    public JSONObject build(HealthCheckResult healthCheckResult) {
        JSONObject expectedJson = new JSONObject();
        expectedJson.accumulate("overallStatus", healthCheckResult.getOverallStatus());
        expectedJson.put("optionalValues", singletonMap("applicationName","Airport"));
        expectedJson.put("probes", probesJson(healthCheckResult));
        return expectedJson;
    }

    private List<JSONObject> probesJson(HealthCheckResult healthCheckResult) {
        return healthCheckResult.getProbeResults().stream().map(this::createProbeJson).collect(toList());
    }

    private JSONObject createProbeJson(ProbeResult probeResult) {
        JSONObject probeJson = new JSONObject();
        probeJson.accumulate("status", probeResult.status);
        probeJson.accumulate("description", probeResult.description);
        probeJson.accumulate("name", probeResult.name);
        return probeJson;
    }
}
