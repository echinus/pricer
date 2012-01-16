package com.twock.swappricer.fpml;

import java.util.List;

import com.twock.swappricer.fpml.model.DateWithDayCount;
import org.apache.log4j.Logger;

/**
 * @author Chris Pearson (chris@twock.com)
 */
public class SwapPaymentCalculator {
  private static final Logger log = Logger.getLogger(SwapPaymentCalculator.class);

  public double valueFixedSide(double notional, double fixedRate, double[] dayCount, List<DateWithDayCount> paymentDates) {
    for(int i = 0; i < dayCount.length; i++) {
      log.debug("Payment on " + paymentDates.get(i) + " for " + notional * fixedRate * dayCount[i]);
    }
    return 0.0;
  }
}
