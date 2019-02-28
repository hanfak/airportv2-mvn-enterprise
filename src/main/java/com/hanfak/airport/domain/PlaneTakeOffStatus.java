package com.hanfak.airport.domain;
// Better name
public class PlaneTakeOffStatus extends ValueType {

  public final SuccessfulPlaneTakeOffStatus successfulPlaneTakeOffStatus;
  public final FailedPlaneTakeOffStatus failedPlaneTakeOffStatus;

  private PlaneTakeOffStatus(SuccessfulPlaneTakeOffStatus successfulPlaneTakeOffStatus, FailedPlaneTakeOffStatus failedPlaneTakeOffStatus) {
    this.successfulPlaneTakeOffStatus = successfulPlaneTakeOffStatus;
    this.failedPlaneTakeOffStatus = failedPlaneTakeOffStatus;
  }

  public static PlaneTakeOffStatus createPlaneTakeOffStatus(SuccessfulPlaneTakeOffStatus successfulPlaneTakeOffStatus, FailedPlaneTakeOffStatus failedPlaneTakeOffStatus) {
    return new PlaneTakeOffStatus(successfulPlaneTakeOffStatus, failedPlaneTakeOffStatus);
  }
}
