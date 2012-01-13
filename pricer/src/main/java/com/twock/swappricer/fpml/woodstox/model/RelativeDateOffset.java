package com.twock.swappricer.fpml.woodstox.model;

import com.twock.swappricer.fpml.woodstox.model.enumeration.DayTypeEnum;
import com.twock.swappricer.fpml.woodstox.model.enumeration.PeriodEnum;

/**
 * @author Chris Pearson (chris@twock.com)
 */
public class RelativeDateOffset extends Offset {
  private final BusinessDayAdjustments businessDayAdjustments;

  public RelativeDateOffset(Integer periodMultiplier, PeriodEnum period, DayTypeEnum dayType, BusinessDayAdjustments businessDayAdjustments) {
    super(periodMultiplier, period, dayType);
    this.businessDayAdjustments = businessDayAdjustments;
  }

  public BusinessDayAdjustments getBusinessDayAdjustments() {
    return businessDayAdjustments;
  }

  @Override
  public String toString() {
    return "RelativeDateOffset{" +
      super.toString() +
      ", businessDayAdjustments=" + businessDayAdjustments +
      '}';
  }
}
