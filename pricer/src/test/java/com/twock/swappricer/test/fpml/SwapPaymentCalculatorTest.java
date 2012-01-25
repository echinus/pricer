package com.twock.swappricer.test.fpml;

import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.SortedMap;

import com.twock.swappricer.CurveContainer;
import com.twock.swappricer.HolidayCalendarContainer;
import com.twock.swappricer.ValuationCurve;
import com.twock.swappricer.fpml.FpmlParser;
import com.twock.swappricer.fpml.SwapPaymentCalculator;
import com.twock.swappricer.fpml.SwapStreamDateCalculator;
import com.twock.swappricer.fpml.model.DateWithDayCount;
import com.twock.swappricer.fpml.model.SwapStream;
import com.twock.swappricer.test.CurveContainerTest;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Chris Pearson (chris@twock.com)
 */
public class SwapPaymentCalculatorTest {
  private static final double DCF_DELTA = 0.00001;
  private static HolidayCalendarContainer allCalendars;
  private static List<SwapStream> streams;
  private static SwapPaymentCalculator swapPaymentCalculator;
  private static SwapStreamDateCalculator swapStreamDateCalculator;
  private static CurveContainer curveContainer;

  @BeforeClass
  public static void setUp() throws UnsupportedEncodingException {
    FpmlParser fpmlParser = FpmlParserTest.createFpmlParser();
    streams = fpmlParser.parse(FpmlParserTest.class.getResourceAsStream("/LCH00000513426.xml"));
    allCalendars = new HolidayCalendarContainer();
    allCalendars.loadFromTsv(new InputStreamReader(SwapPaymentCalculatorTest.class.getResourceAsStream("/calendars.tsv"), "UTF8"));
    curveContainer = CurveContainerTest.getCurveContainer();
    swapPaymentCalculator = new SwapPaymentCalculator(curveContainer);
    swapStreamDateCalculator = new SwapStreamDateCalculator();
  }

  @Test
  public void testFixedSideValuation() {
    SwapStream fixedStream = streams.get(0);
    List<DateWithDayCount> periodDates = swapStreamDateCalculator.calculateAdjustedPeriodDates(fixedStream, allCalendars);
    List<DateWithDayCount> paymentDates = swapStreamDateCalculator.calculatePaymentDates(periodDates, fixedStream.getPaymentDates(), allCalendars);
    double[] dayCountFractions = swapStreamDateCalculator.getDayCountFractions(periodDates, fixedStream.getDayCountFraction(), fixedStream.getCalculationPeriodFrequency(), null, null);
    Assert.assertEquals(167468.93, swapPaymentCalculator.valueFixedSide(fixedStream.getNotionalAmount(), fixedStream.getFixedRate(), dayCountFractions, paymentDates, fixedStream.getNotionalCurrency()), 0.01);
  }

  @Test
  public void testFixedSidePricing() {
    SwapStream fixedStream = streams.get(0);
    List<DateWithDayCount> periodDates = swapStreamDateCalculator.calculateAdjustedPeriodDates(fixedStream, allCalendars);
    List<DateWithDayCount> paymentDates = swapStreamDateCalculator.calculatePaymentDates(periodDates, fixedStream.getPaymentDates(), allCalendars);
    double[] dayCountFractions = swapStreamDateCalculator.getDayCountFractions(periodDates, fixedStream.getDayCountFraction(), fixedStream.getCalculationPeriodFrequency(), null, null);
    String discountCurve = curveContainer.getDiscountCurve(null, null, null, fixedStream.getNotionalCurrency());
    ValuationCurve curve = curveContainer.getCurve(discountCurve);

    double[] fixedPaymentAmounts = swapPaymentCalculator.calculateFixedPaymentAmounts(fixedStream.getNotionalAmount(), fixedStream.getFixedRate(), dayCountFractions, 0, dayCountFractions.length);
    double[] expectedLastAmounts = {25000, 25000, 25000, 25138.89, 24861.11, 25000, 25000};
    for(int i = 0; i < expectedLastAmounts.length; i++) {
      Assert.assertEquals(expectedLastAmounts[i], fixedPaymentAmounts[fixedPaymentAmounts.length - expectedLastAmounts.length + i], 0.01);
    }

    double[] fixedDiscountedAmounts = swapPaymentCalculator.calculateDiscountedPaymentAmounts(fixedPaymentAmounts, paymentDates, curve);
    double[] expectedDiscountedAmounts = {24916.00, 24763.71, 24513.38, 24276.06, 23519.49, 23061.93, 22418.36};
    for(int i = 0; i < expectedDiscountedAmounts.length; i++) {
      Assert.assertEquals(expectedDiscountedAmounts[i], fixedDiscountedAmounts[fixedDiscountedAmounts.length - expectedDiscountedAmounts.length + i], 0.01);
    }
  }

  @Test
  public void testFloatingSideIndexRates() {
    SwapStream floatingStream = streams.get(1);
    List<DateWithDayCount> periodDates = swapStreamDateCalculator.calculateAdjustedPeriodDates(floatingStream, allCalendars);
    List<DateWithDayCount> paymentDates = swapStreamDateCalculator.calculatePaymentDates(periodDates, floatingStream.getPaymentDates(), allCalendars);
    List<DateWithDayCount> resetDates = swapStreamDateCalculator.calculateFixingDates(periodDates, floatingStream.getResetDates(), allCalendars);
    SortedMap<Integer, Double> historicIndexRates = curveContainer.getHistoricIndexRates(floatingStream.getFloatingRateIndex(), floatingStream.getIndexTenorPeriodMultiplier(), floatingStream.getIndexTenorPeriod());
    // todo figure out currency cut off lag for PV, and don't bother calculating fixings for periods that pay before that date
    // todo today's date is the cut off
    // todo get historic rates for any periods that pay > valuation date and have available historic fixings
    double[] fixings = new double[resetDates.size()];
    for(int i = 0; i < fixings.length; i++) {
      Double fixing = historicIndexRates.get(resetDates.get(i).getDayCount());
      if(fixing == null) {
        break;
      }
      fixings[i] = fixing;
    }
    // todo estimate forward rates for any periods
  }
}
