package com.twock.swappricer.fpml.model;

import java.util.Arrays;

import com.twock.swappricer.fpml.model.enumeration.BusinessDayConventionEnum;
import com.twock.swappricer.fpml.model.enumeration.DayTypeEnum;
import com.twock.swappricer.fpml.model.enumeration.PeriodEnum;

/**
 * @author Chris Pearson (chris@twock.com)
 */
public class RelativeDateOffset extends Offset {
  private final BusinessDayConventionEnum businessDayConvention;
  private final String[] businessCenters;

  public RelativeDateOffset(Integer periodMultiplier, PeriodEnum period, DayTypeEnum dayType, BusinessDayConventionEnum businessDayConvention, String[] businessCenters) {
    super(periodMultiplier, period, dayType);
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
    return "RelativeDateOffset{" +
      "periodMultiplier=" + getPeriodMultiplier() +
      ", period=" + getPeriod() +
      ", dayType=" + getDayType() +
      ", businessDayConvention=" + businessDayConvention +
      ", businessCenters=" + (businessCenters == null ? null : Arrays.asList(businessCenters)) +
      '}';
  }
}
