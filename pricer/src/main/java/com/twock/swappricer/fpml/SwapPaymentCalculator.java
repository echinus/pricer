package com.twock.swappricer.fpml;

import java.util.Collections;
import java.util.List;

import com.twock.swappricer.ValuationCurve;
import com.twock.swappricer.ValuationCurveContainer;
import com.twock.swappricer.fpml.model.DateWithDayCount;

/**
 * @author Chris Pearson (chris@twock.com)
 */
public class SwapPaymentCalculator {
  private final ValuationCurveContainer valuationCurveContainer;

  public SwapPaymentCalculator(ValuationCurveContainer valuationCurveContainer) {
    this.valuationCurveContainer = valuationCurveContainer;
  }

  public double valueFixedSide(double notional, double fixedRate, double[] dayCount, List<DateWithDayCount> paymentDates, String currency) {
    String discountCurve = valuationCurveContainer.getDiscountCurve(null, null, null, currency);
    ValuationCurve curve = valuationCurveContainer.getCurve(discountCurve);
    double[] fixedPaymentAmounts = calculateFixedPaymentAmounts(notional, fixedRate, dayCount, 0, dayCount.length);
    double[] fixedDiscountedAmounts = calculateDiscountedPaymentAmounts(fixedPaymentAmounts, paymentDates, curve);
    double result = 0;
    for(double amount : fixedDiscountedAmounts) {
      result += amount;
    }
    return result;
  }

  public double[] calculateFixedPaymentAmounts(double notional, double fixedRate, double[] dayCount, int startIndex, int endIndex) {
    double[] result = new double[endIndex - startIndex];
    for(int i = startIndex; i < endIndex; i++) {
      result[i] = notional * fixedRate * dayCount[i];
    }
    return result;
  }

  /**
   * Dump all payments that pay before the first maturity date on the given curve, and discount the provided payments
   * for the rest. Expects paymentAmounts.length == paymentDates.size() and returns an array of size &lt;=
   * paymentDates.size() containing only those payments that are within the given curve.
   *
   * @param paymentAmounts array of past and future payment amounts
   * @param paymentDates payment dates for the given payment amounts
   * @param curve the curve to use for discounting
   * @return the discounted future cash flows
   */
  public double[] calculateDiscountedPaymentAmounts(double[] paymentAmounts, List<DateWithDayCount> paymentDates, ValuationCurve curve) {
    DateWithDayCount temp = new DateWithDayCount(curve.getMaturityDates()[0]);
    int position = Collections.binarySearch(paymentDates, temp);
    if(position < 0) {
      position = -(position + 1);
    }
    int firstPaymentToLookAt = position;
    double[] result = new double[paymentAmounts.length - firstPaymentToLookAt];
    for(int i = 0; i < result.length; i++) {
      int index = i + firstPaymentToLookAt;
      result[i] = curve.getDiscountFactor(paymentDates.get(index).getDayCount()) * paymentAmounts[index];
    }
    return result;
  }
}
