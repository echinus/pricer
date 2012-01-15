package com.twock.swappricer.fpml.model;

import com.twock.swappricer.fpml.model.enumeration.PeriodEnum;
import com.twock.swappricer.fpml.model.enumeration.RollConventionEnum;

/**
 * @author Chris Pearson (chris@twock.com)
 */
public class CalculationPeriodFrequency {
  private final Integer periodMultiplier;
  private final PeriodEnum period;
  private final RollConventionEnum rollConvention;

  public CalculationPeriodFrequency(Integer periodMultiplier, PeriodEnum period, RollConventionEnum rollConvention) {
    this.periodMultiplier = periodMultiplier;
    this.period = period;
    this.rollConvention = rollConvention;
  }

  public Integer getPeriodMultiplier() {
    return periodMultiplier;
  }

  public PeriodEnum getPeriod() {
    return period;
  }

  public RollConventionEnum getRollConvention() {
    return rollConvention;
  }

  @Override
  public String toString() {
    return "CalculationPeriodFrequency{" +
      "periodMultiplier=" + periodMultiplier +
      ", period=" + period +
      ", rollConvention=" + rollConvention +
      '}';
  }
}
