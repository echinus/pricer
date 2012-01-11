package com.twock.swappricer.fpml.woodstox.model;

import java.util.Arrays;

/**
 * @author Chris Pearson (chris@twock.com)
 */
public class BusinessDayAdjustments {
  private final BusinessDayConventionEnum businessDayConvention;
  private final String[] businessCenters;

  public BusinessDayAdjustments(BusinessDayConventionEnum businessDayConvention, String[] businessCenters) {
    this.businessDayConvention = businessDayConvention;
    this.businessCenters = businessCenters;
  }

  public BusinessDayConventionEnum getBusinessDayConvention() {
    return businessDayConvention;
  }

  public String[] getBusinessCenters() {
    return businessCenters;
  }

  @Override
  public String toString() {
    return "BusinessDayAdjustments{" +
      "businessDayConvention=" + businessDayConvention +
      ", businessCenters=" + (businessCenters == null ? "null" : Arrays.toString(businessCenters)) +
      '}';
  }
}
