package com.hanfak.airport.domain.planelandstatus;

import com.hanfak.airport.domain.helper.ValueType;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

// Better name
@SuppressFBWarnings("URF_UNREAD_PUBLIC_OR_PROTECTED_FIELD") // Later fix this issue, getters or different type
public class PlaneLandStatus extends ValueType {

  // TODO P1 make optional
  public final SuccessfulPlaneLandStatus successfulPlaneLandStatus;
  public final FailedPlaneLandStatus failedPlaneLandStatus;

  private PlaneLandStatus(SuccessfulPlaneLandStatus successfulPlaneLandStatus, FailedPlaneLandStatus failedPlaneLandStatus) {
    this.successfulPlaneLandStatus = successfulPlaneLandStatus;
    this.failedPlaneLandStatus = failedPlaneLandStatus;
  }

  public static PlaneLandStatus createPlaneLandStatus(SuccessfulPlaneLandStatus successfulPlaneLandStatus, FailedPlaneLandStatus failedPlaneLandStatus) {
    return new PlaneLandStatus(successfulPlaneLandStatus, failedPlaneLandStatus);
  }

  // TODO p1 create static factories for succes and failure, thus placing the null here

}
