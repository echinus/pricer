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

  /**
   * When given a date, obtain the discount factor by linearly interpolating between tenors.
   *
   * @param dayCount numeric date from DateUtil
   * @return the discount factor for the given date from the given curve
   */
  public double getDiscountFactor(int dayCount) {
    int position = Arrays.binarySearch(maturityDates, dayCount);
    if(position >= 0) {
      if(position == 0) {
        throw new PricerException("Extrapolation earlier than first date " + Arrays.toString(DateUtil.dayCountToDate(maturityDates[0])) + " not currently supported for curve " + curveName + " and date " + Arrays.toString(DateUtil.dayCountToDate(dayCount)));
      } else if(position == maturityDates.length) {
        throw new PricerException("Extrapolation not currently supported for curve " + curveName + " and date " + Arrays.toString(DateUtil.dayCountToDate(dayCount)));
      }
      return discountFactors[position];
    } else {
      position = -(position + 1);
      if(position == 0) {
        throw new PricerException("Extrapolation earlier than first date " + Arrays.toString(DateUtil.dayCountToDate(maturityDates[0])) + " not currently supported for curve " + curveName + " and date " + Arrays.toString(DateUtil.dayCountToDate(dayCount)));
      } else if(position == maturityDates.length) {
        throw new PricerException("Extrapolation not currently supported for curve " + curveName + " and date " + Arrays.toString(DateUtil.dayCountToDate(dayCount)));
      }
      int earlierDate = maturityDates[position - 1];
      int laterDate = maturityDates[position];
      double earlierRate = zeroRates[position - 1];
      double laterRate = zeroRates[position];
      // df = EXP(zero rate * -(flow date – valuation date)/365)
      // R = R1 + (D – D1)*(R2 – R1)/(D2 – D1)
      // R = interpolated zero rate
      // R1= zero rate for closest curve pillar with earlier date
      // R2 = zero rate for closest curve pillar with later date
      // D = value date for forward flow
      // D1 = value date for closest curve pillar with earlier date
      // D2 = value date for closest curve pillar with later date
      double interpolatedZeroRate = earlierRate + (dayCount - earlierDate) * (laterRate - earlierRate) / (laterDate - earlierDate);
      return Math.exp(interpolatedZeroRate * -(dayCount - curveDate.getDayCount()) / 365.0);
    }
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
