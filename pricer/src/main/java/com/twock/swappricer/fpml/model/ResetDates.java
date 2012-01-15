package com.twock.swappricer.fpml.model;

import com.twock.swappricer.fpml.model.enumeration.PeriodEnum;
import com.twock.swappricer.fpml.model.enumeration.ResetRelativeToEnum;

/**
 * @author Chris Pearson (chris@twock.com)
 */
public class ResetDates {
  private final ResetRelativeToEnum resetRelativeTo;
  private final RelativeDateOffset initialFixingDate;
  private final RelativeDateOffset fixingDates;
  private final Integer frequencyPeriodMultiplier;
  private final PeriodEnum frequencyPeriod;
  private final BusinessDayAdjustments resetDatesAdjustments;

  public ResetDates(ResetRelativeToEnum resetRelativeTo, RelativeDateOffset initialFixingDate, RelativeDateOffset fixingDates, Integer frequencyPeriodMultiplier, PeriodEnum frequencyPeriod, BusinessDayAdjustments resetDatesAdjustments) {
    this.resetRelativeTo = resetRelativeTo;
    this.initialFixingDate = initialFixingDate;
    this.fixingDates = fixingDates;
    this.frequencyPeriodMultiplier = frequencyPeriodMultiplier;
    this.frequencyPeriod = frequencyPeriod;
    this.resetDatesAdjustments = resetDatesAdjustments;
  }

  public ResetRelativeToEnum getResetRelativeTo() {
    return resetRelativeTo;
  }

  public RelativeDateOffset getInitialFixingDate() {
    return initialFixingDate;
  }

  public RelativeDateOffset getFixingDates() {
    return fixingDates;
  }

  public Integer getFrequencyPeriodMultiplier() {
    return frequencyPeriodMultiplier;
  }

  public PeriodEnum getFrequencyPeriod() {
    return frequencyPeriod;
  }

  public BusinessDayAdjustments getResetDatesAdjustments() {
    return resetDatesAdjustments;
  }

  @Override
  public String toString() {
    return "ResetDates{" +
      "resetRelativeTo=" + resetRelativeTo +
      ", initialFixingDate=" + initialFixingDate +
      ", fixingDates=" + fixingDates +
      ", frequencyPeriodMultiplier=" + frequencyPeriodMultiplier +
      ", frequencyPeriod=" + frequencyPeriod +
      ", resetDatesAdjustments=" + resetDatesAdjustments +
      '}';
  }
}
