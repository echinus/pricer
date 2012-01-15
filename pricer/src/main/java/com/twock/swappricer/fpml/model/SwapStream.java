package com.twock.swappricer.fpml.model;

import com.twock.swappricer.fpml.model.enumeration.CompoundingMethodEnum;
import com.twock.swappricer.fpml.model.enumeration.DayCountFractionEnum;
import com.twock.swappricer.fpml.model.enumeration.PeriodEnum;

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
  private final PaymentDates paymentDates;
  private final ResetDates resetDates;
  private final Double notionalAmount;
  private final String notionalCurrency;
  private final DayCountFractionEnum dayCountFraction;
  private final CompoundingMethodEnum compoundingMethod;
  // fixed sides only:
  private final Double fixedRate;
  // floating sides only:
  private final String floatingRateIndex;
  private final Integer indexTenorPeriodMultiplier;
  private final PeriodEnum indexTenorPeriod;
  private final Double spread;
  private final Double initialRate;

  public SwapStream(AdjustableDate effectiveDate, AdjustableDate terminationDate, BusinessDayAdjustments calculationPeriodDatesAdjustments, CalculationPeriodFrequency calculationPeriodFrequency, DateWithDayCount firstRegularPeriodStartDate, DateWithDayCount lastRegularPeriodEndDate, PaymentDates paymentDates, ResetDates resetDates, Double notionalAmount, String notionalCurrency, DayCountFractionEnum dayCountFraction, CompoundingMethodEnum compoundingMethod, Double fixedRate, String floatingRateIndex, Integer indexTenorPeriodMultiplier, PeriodEnum indexTenorPeriod, Double spread, Double initialRate) {
    this.effectiveDate = effectiveDate;
    this.terminationDate = terminationDate;
    this.calculationPeriodDatesAdjustments = calculationPeriodDatesAdjustments;
    this.calculationPeriodFrequency = calculationPeriodFrequency;
    this.firstRegularPeriodStartDate = firstRegularPeriodStartDate;
    this.lastRegularPeriodEndDate = lastRegularPeriodEndDate;
    this.paymentDates = paymentDates;
    this.resetDates = resetDates;
    this.notionalAmount = notionalAmount;
    this.notionalCurrency = notionalCurrency;
    this.dayCountFraction = dayCountFraction;
    this.compoundingMethod = compoundingMethod;
    this.fixedRate = fixedRate;
    this.floatingRateIndex = floatingRateIndex;
    this.indexTenorPeriodMultiplier = indexTenorPeriodMultiplier;
    this.indexTenorPeriod = indexTenorPeriod;
    this.spread = spread;
    this.initialRate = initialRate;
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

  public PaymentDates getPaymentDates() {
    return paymentDates;
  }

  public ResetDates getResetDates() {
    return resetDates;
  }

  public Double getNotionalAmount() {
    return notionalAmount;
  }

  public String getNotionalCurrency() {
    return notionalCurrency;
  }

  public Double getFixedRate() {
    return fixedRate;
  }

  public DayCountFractionEnum getDayCountFraction() {
    return dayCountFraction;
  }

  public CompoundingMethodEnum getCompoundingMethod() {
    return compoundingMethod;
  }

  public String getFloatingRateIndex() {
    return floatingRateIndex;
  }

  public Integer getIndexTenorPeriodMultiplier() {
    return indexTenorPeriodMultiplier;
  }

  public PeriodEnum getIndexTenorPeriod() {
    return indexTenorPeriod;
  }

  public Double getSpread() {
    return spread;
  }

  public Double getInitialRate() {
    return initialRate;
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
      ", paymentDates=" + paymentDates +
      ", resetDates=" + resetDates +
      ", notionalAmount=" + notionalAmount +
      ", notionalCurrency='" + notionalCurrency + '\'' +
      ", dayCountFraction='" + dayCountFraction + '\'' +
      ", compoundingMethod=" + compoundingMethod +
      ", fixedRate=" + fixedRate +
      ", floatingRateIndex='" + floatingRateIndex + '\'' +
      ", indexTenorPeriodMultiplier=" + indexTenorPeriodMultiplier +
      ", indexTenorPeriod=" + indexTenorPeriod +
      ", spread=" + spread +
      ", initialRate=" + initialRate +
      '}';
  }
}
