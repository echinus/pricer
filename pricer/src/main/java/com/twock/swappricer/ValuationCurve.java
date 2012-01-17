package com.twock.swappricer;

import java.util.Arrays;

import com.twock.swappricer.fpml.model.DateWithDayCount;

/**
 * @author Chris Pearson (chris@twock.com)
 */
public class ValuationCurve {
  private final String curveName;
  private final DateWithDayCount curveDate;
  private final int[] maturityDates;
  private final double[] zeroRates;
  private final double[] discountFactors;

  public ValuationCurve(String curveName, DateWithDayCount curveDate, int[] maturityDates, double[] zeroRates, double[] discountFactors) {
    this.curveName = curveName;
    this.curveDate = curveDate;
    this.maturityDates = maturityDates;
    this.zeroRates = zeroRates;
    this.discountFactors = discountFactors;
  }

  public String getCurveName() {
    return curveName;
  }

  public DateWithDayCount getCurveDate() {
    return curveDate;
  }

  public int[] getMaturityDates() {
    return maturityDates;
  }

  public double[] getZeroRates() {
    return zeroRates;
  }

  public double[] getDiscountFactors() {
    return discountFactors;
  }

  @Override
  public String toString() {
    return "ValuationCurve{" +
      "curveName='" + curveName + '\'' +
      ", curveDate=" + curveDate +
      ", maturityDates=" + Arrays.toString(maturityDates) +
      ", zeroRates=" + Arrays.toString(zeroRates) +
      ", discountFactors=" + Arrays.toString(discountFactors) +
      '}';
  }
}
