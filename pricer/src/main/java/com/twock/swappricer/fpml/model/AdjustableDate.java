package com.twock.swappricer.fpml.model;

/**
 * @author Chris Pearson (chris@twock.com)
 */
public class AdjustableDate {
  private final DateWithDayCount unadjustedDate;
  private final BusinessDayAdjustments businessDayAdjustments;

  public AdjustableDate(DateWithDayCount unadjustedDate, BusinessDayAdjustments businessDayAdjustments) {
    this.unadjustedDate = unadjustedDate;
    this.businessDayAdjustments = businessDayAdjustments;
  }

  public DateWithDayCount getUnadjustedDate() {
    return unadjustedDate;
  }

  public BusinessDayAdjustments getBusinessDayAdjustments() {
    return businessDayAdjustments;
  }

  @Override
  public String toString() {
    return "AdjustableDate{" +
      "unadjustedDate=" + unadjustedDate +
      ", businessDayAdjustments=" + businessDayAdjustments +
      '}';
  }
}
