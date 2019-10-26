package com.hanfak.airport.domain.planelandstatus;

import com.hanfak.airport.domain.helper.ValueType;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

@SuppressFBWarnings("URF_UNREAD_PUBLIC_OR_PROTECTED_FIELD") // Later fix this issue, getters or different type
public class PlaneLandStatus extends ValueType {

  // TODO P1 make optional
  public final SuccessfulPlaneLandStatus successfulPlaneLandStatus;
  public final FailedPlaneLandStatus failedPlaneLandStatus;

  private PlaneLandStatus(SuccessfulPlaneLandStatus successfulPlaneLandStatus, FailedPlaneLandStatus failedPlaneLandStatus) {
    this.successfulPlaneLandStatus = successfulPlaneLandStatus;
    this.failedPlaneLandStatus = failedPlaneLandStatus;
  }

  public static PlaneLandStatus planeLandedSuccessfully(SuccessfulPlaneLandStatus successfulPlaneLandStatus) {
    return new PlaneLandStatus(successfulPlaneLandStatus, null);
  }
  public static PlaneLandStatus planeFailedToLand(FailedPlaneLandStatus failedPlaneLandStatus) {
    return new PlaneLandStatus(null, failedPlaneLandStatus);
  }
}
