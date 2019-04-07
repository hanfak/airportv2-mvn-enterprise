package com.hanfak.airport.domain.planetakeoffstatus;

import com.hanfak.airport.domain.helper.ValueType;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

// Better name
@SuppressFBWarnings("URF_UNREAD_PUBLIC_OR_PROTECTED_FIELD") // Later fix this issue, getters or different type
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
