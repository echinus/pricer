package com.twock.swappricer.fpml.woodstox.model;

import java.util.Arrays;

/**
 * @author Chris Pearson (chris@twock.com)
 */
public class SwapStream {
  private final AdjustableDate effectiveDate;
  private final AdjustableDate terminationDate;
  private final BusinessDayAdjustments calculationPeriodDatesAdjustments;
  private final CalculationPeriodFrequency calculationPeriodFrequency;
  private final DateWithDayCount firstRegularPeriodStartDate;
  private final DateWithDayCount lastRegularPeriodEndDate;

  public SwapStream(AdjustableDate effectiveDate, AdjustableDate terminationDate, BusinessDayAdjustments calculationPeriodDatesAdjustments, CalculationPeriodFrequency calculationPeriodFrequency, DateWithDayCount firstRegularPeriodStartDate, DateWithDayCount lastRegularPeriodEndDate) {
    this.effectiveDate = effectiveDate;
    this.terminationDate = terminationDate;
    this.calculationPeriodDatesAdjustments = calculationPeriodDatesAdjustments;
    this.calculationPeriodFrequency = calculationPeriodFrequency;
    this.firstRegularPeriodStartDate = firstRegularPeriodStartDate;
    this.lastRegularPeriodEndDate = lastRegularPeriodEndDate;
  }

  public AdjustableDate getEffectiveDate() {
    return effectiveDate;
  }

  public AdjustableDate getTerminationDate() {
    return terminationDate;
  }

  public BusinessDayAdjustments getCalculationPeriodDatesAdjustments() {
    return calculationPeriodDatesAdjustments;
  }

  public CalculationPeriodFrequency getCalculationPeriodFrequency() {
    return calculationPeriodFrequency;
  }

  public DateWithDayCount getFirstRegularPeriodStartDate() {
    return firstRegularPeriodStartDate;
  }

  public DateWithDayCount getLastRegularPeriodEndDate() {
    return lastRegularPeriodEndDate;
  }

  @Override
  public String toString() {
    return "SwapStream{" +
      "effectiveDate=" + effectiveDate +
      ", terminationDate=" + terminationDate +
      ", calculationPeriodDatesAdjustments=" + calculationPeriodDatesAdjustments +
      ", calculationPeriodFrequency=" + calculationPeriodFrequency +
      ", firstRegularPeriodStartDate=" + firstRegularPeriodStartDate +
      ", lastRegularPeriodEndDate=" + lastRegularPeriodEndDate +
      '}';
  }
}
