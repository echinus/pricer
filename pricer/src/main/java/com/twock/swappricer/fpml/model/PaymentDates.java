package com.twock.swappricer.fpml.model;

import com.twock.swappricer.fpml.model.enumeration.PayRelativeToEnum;
import com.twock.swappricer.fpml.model.enumeration.PeriodEnum;

/**
 * @author Chris Pearson (chris@twock.com)
 */
public class PaymentDates {
  private final Integer paymentFrequencyPeriodMultiplier;
  private final PeriodEnum paymentFrequencyPeriod;
  private final PayRelativeToEnum payRelativeTo;
  private final Offset paymentDaysOffset;
  private final BusinessDayAdjustments paymentDatesAdjustments;

  public PaymentDates(Integer paymentFrequencyPeriodMultiplier, PeriodEnum paymentFrequencyPeriod, PayRelativeToEnum payRelativeTo, Offset paymentDaysOffset, BusinessDayAdjustments paymentDatesAdjustments) {
    this.paymentFrequencyPeriodMultiplier = paymentFrequencyPeriodMultiplier;
    this.paymentFrequencyPeriod = paymentFrequencyPeriod;
    this.payRelativeTo = payRelativeTo;
    this.paymentDaysOffset = paymentDaysOffset;
    this.paymentDatesAdjustments = paymentDatesAdjustments;
  }

  public Integer getPaymentFrequencyPeriodMultiplier() {
    return paymentFrequencyPeriodMultiplier;
  }

  public PeriodEnum getPaymentFrequencyPeriod() {
    return paymentFrequencyPeriod;
  }

  public PayRelativeToEnum getPayRelativeTo() {
    return payRelativeTo;
  }

  public Offset getPaymentDaysOffset() {
    return paymentDaysOffset;
  }

  public BusinessDayAdjustments getPaymentDatesAdjustments() {
    return paymentDatesAdjustments;
  }

  @Override
  public String toString() {
    return "PaymentDates{" +
      "paymentFrequencyPeriodMultiplier=" + paymentFrequencyPeriodMultiplier +
      ", paymentFrequencyPeriod=" + paymentFrequencyPeriod +
      ", payRelativeTo=" + payRelativeTo +
      ", paymentDaysOffset=" + paymentDaysOffset +
      ", paymentDatesAdjustments=" + paymentDatesAdjustments +
      '}';
  }
}
