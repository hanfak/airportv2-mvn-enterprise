package com.hanfak.airport.domain.planelandstatus;

import com.hanfak.airport.domain.helper.ValueType;
import com.hanfak.airport.domain.planetakeoffstatus.FailedPlaneTakeOffStatus;
import com.hanfak.airport.domain.planetakeoffstatus.SuccessfulPlaneTakeOffStatus;

// Better name
public class PlaneLandStatus extends ValueType {

  public final SuccessfulPlaneTakeOffStatus successfulPlaneTakeOffStatus;
  public final FailedPlaneTakeOffStatus failedPlaneTakeOffStatus;

  private PlaneLandStatus(SuccessfulPlaneTakeOffStatus successfulPlaneTakeOffStatus, FailedPlaneTakeOffStatus failedPlaneTakeOffStatus) {
    this.successfulPlaneTakeOffStatus = successfulPlaneTakeOffStatus;
    this.failedPlaneTakeOffStatus = failedPlaneTakeOffStatus;
  }

  public static PlaneLandStatus createPlaneTakeOffStatus(SuccessfulPlaneTakeOffStatus successfulPlaneTakeOffStatus, FailedPlaneTakeOffStatus failedPlaneTakeOffStatus) {
    return new PlaneLandStatus(successfulPlaneTakeOffStatus, failedPlaneTakeOffStatus);
  }
}
