package com.twock.swappricer.fpml;

import java.util.List;

import com.twock.swappricer.ValuationCurveContainer;
import com.twock.swappricer.fpml.model.DateWithDayCount;
import org.apache.log4j.Logger;

/**
 * @author Chris Pearson (chris@twock.com)
 */
public class SwapPaymentCalculator {
  private static final Logger log = Logger.getLogger(SwapPaymentCalculator.class);
  private final ValuationCurveContainer valuationCurveContainer;

  public SwapPaymentCalculator(ValuationCurveContainer valuationCurveContainer) {
    this.valuationCurveContainer = valuationCurveContainer;
  }

  public double valueFixedSide(double notional, double fixedRate, double[] dayCount, List<DateWithDayCount> paymentDates, String currency) {
    String discountCurve = valuationCurveContainer.getDiscountCurve(null, null, null, currency);
    for(int i = 0; i < dayCount.length; i++) {
      double discountFactor = valuationCurveContainer.getDiscountFactor(discountCurve, paymentDates.get(i).getDayCount());
      log.debug("Payment on " + paymentDates.get(i) + " for " + notional * fixedRate * dayCount[i] + " with df=" + discountFactor);
    }
    return 0.0;
  }
}
