package com.twock.swappricer.fpml.model;

import com.twock.swappricer.fpml.model.enumeration.DayTypeEnum;
import com.twock.swappricer.fpml.model.enumeration.PeriodEnum;

/**
 * @author Chris Pearson (chris@twock.com)
 */
public class Offset {
  private final Integer periodMultiplier;
  private final PeriodEnum period;
  private final DayTypeEnum dayType;

  public Offset(Integer periodMultiplier, PeriodEnum period, DayTypeEnum dayType) {
    this.periodMultiplier = periodMultiplier;
    this.period = period;
    this.dayType = dayType;
  }

  public Integer getPeriodMultiplier() {
    return periodMultiplier;
  }

  public PeriodEnum getPeriod() {
    return period;
  }

  public DayTypeEnum getDayType() {
    return dayType;
  }

  @Override
  public String toString() {
    return "Offset{" +
      "periodMultiplier=" + periodMultiplier +
      ", period=" + period +
      ", dayType=" + dayType +
      '}';
  }
}
