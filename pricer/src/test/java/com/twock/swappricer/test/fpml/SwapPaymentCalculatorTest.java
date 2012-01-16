package com.twock.swappricer.test.fpml;

import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;

import com.twock.swappricer.HolidayCalendarContainer;
import com.twock.swappricer.fpml.FpmlParser;
import com.twock.swappricer.fpml.SwapPaymentCalculator;
import com.twock.swappricer.fpml.SwapStreamDateCalculator;
import com.twock.swappricer.fpml.model.DateWithDayCount;
import com.twock.swappricer.fpml.model.SwapStream;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Chris Pearson (chris@twock.com)
 */
public class SwapPaymentCalculatorTest {
  private static HolidayCalendarContainer allCalendars;
  private static List<SwapStream> streams;
  private static SwapPaymentCalculator swapPaymentCalculator;
  private static SwapStreamDateCalculator swapStreamDateCalculator;

  @BeforeClass
  public static void setUp() throws UnsupportedEncodingException {
    FpmlParser fpmlParser = FpmlParserTest.createFpmlParser();
    streams = fpmlParser.parse(FpmlParserTest.class.getResourceAsStream("/LCH00000513426.xml"));
    allCalendars = new HolidayCalendarContainer();
    allCalendars.loadFromTsv(new InputStreamReader(SwapStreamDateCalculatorTest.class.getResourceAsStream("/calendars.tsv"), "UTF8"));
    swapPaymentCalculator = new SwapPaymentCalculator();
    swapStreamDateCalculator = new SwapStreamDateCalculator();
  }

  @Test
  public void testFixedSideValuation() {
    SwapStream fixedStream = streams.get(0);
    List<DateWithDayCount> periodDates = swapStreamDateCalculator.calculateAdjustedPeriodDates(fixedStream, allCalendars);
    List<DateWithDayCount> paymentDates = swapStreamDateCalculator.calculatePaymentDates(periodDates, fixedStream.getPaymentDates(), allCalendars);
    double[] dayCountFractions = swapStreamDateCalculator.getDayCountFractions(periodDates, fixedStream.getDayCountFraction(), fixedStream.getCalculationPeriodFrequency(), null, null);
    swapPaymentCalculator.valueFixedSide(fixedStream.getNotionalAmount(), fixedStream.getFixedRate(), dayCountFractions, paymentDates);
  }
}
