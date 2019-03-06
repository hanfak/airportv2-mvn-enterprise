package com.hanfak.airport.domain.planelandstatus;

import com.hanfak.airport.domain.helper.ValueType;

// Better name
public class PlaneLandStatus extends ValueType {

  public final SuccessfulPlaneLandStatus successfulPlaneLandStatus;
  public final FailedPlaneLandStatus failedPlaneLandStatus;

  private PlaneLandStatus(SuccessfulPlaneLandStatus successfulPlaneLandStatus, FailedPlaneLandStatus failedPlaneLandStatus) {
    this.successfulPlaneLandStatus = successfulPlaneLandStatus;
    this.failedPlaneLandStatus = failedPlaneLandStatus;
  }

  public static PlaneLandStatus createPlaneLandStatus(SuccessfulPlaneLandStatus successfulPlaneLandStatus, FailedPlaneLandStatus failedPlaneLandStatus) {
    return new PlaneLandStatus(successfulPlaneLandStatus, failedPlaneLandStatus);
  }
}
