package com.twock.swappricer.test.fpml.woodstox;

import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.*;

import com.twock.swappricer.HolidayCalendar;
import com.twock.swappricer.HolidayCalendarContainer;
import com.twock.swappricer.PricerException;
import com.twock.swappricer.fpml.woodstox.FpmlParser;
import com.twock.swappricer.fpml.woodstox.SwapStreamDateCalculator;
import com.twock.swappricer.fpml.woodstox.model.CalculationPeriodFrequency;
import com.twock.swappricer.fpml.woodstox.model.DateWithDayCount;
import com.twock.swappricer.fpml.woodstox.model.SwapStream;
import com.twock.swappricer.fpml.woodstox.model.enumeration.BusinessDayConventionEnum;
import com.twock.swappricer.fpml.woodstox.model.enumeration.DayTypeEnum;
import com.twock.swappricer.fpml.woodstox.model.enumeration.PeriodEnum;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import static com.twock.swappricer.fpml.woodstox.model.enumeration.BusinessDayConventionEnum.*;
import static com.twock.swappricer.fpml.woodstox.model.enumeration.RollConventionEnum.*;

/**
 * @author Chris Pearson (chris@twock.com)
 */
public class SwapStreamDateCalculatorTest {
  private static SwapStreamDateCalculator calculator;
  private static FpmlParser fpmlParser;
  private static HolidayCalendarContainer container;
  private static HolidayCalendarContainer london;

  @BeforeClass
  public static void setUp() throws UnsupportedEncodingException {
    calculator = new SwapStreamDateCalculator();
    fpmlParser = FpmlParserTest.createFpmlParser();
    container = new HolidayCalendarContainer();
    container.loadFromTsv(new InputStreamReader(SwapStreamDateCalculatorTest.class.getResourceAsStream("/calendars.tsv"), "UTF8"));
    london = new HolidayCalendarContainer(container, Arrays.asList("GBLO"));
  }

  @Test
  public void checkImmDate() {
    DateWithDayCount JAN18_2012 = new DateWithDayCount(2012, 1, 18);
    Assert.assertTrue(calculator.matchesRollConvention(JAN18_2012, IMM));
    Assert.assertFalse(calculator.matchesRollConvention(JAN18_2012, DAY17));
    Assert.assertTrue(calculator.matchesRollConvention(JAN18_2012, DAY18));
    Assert.assertFalse(calculator.matchesRollConvention(JAN18_2012, DAY19));
    Assert.assertFalse(calculator.matchesRollConvention(JAN18_2012, EOM));
  }

  @Test
  public void checkImmSecondWeds() {
    DateWithDayCount JAN11_2012 = new DateWithDayCount(2012, 1, 11);
    Assert.assertFalse(calculator.matchesRollConvention(JAN11_2012, IMM));
    Assert.assertFalse(calculator.matchesRollConvention(JAN11_2012, DAY10));
    Assert.assertTrue(calculator.matchesRollConvention(JAN11_2012, DAY11));
    Assert.assertFalse(calculator.matchesRollConvention(JAN11_2012, DAY12));
    Assert.assertFalse(calculator.matchesRollConvention(JAN11_2012, EOM));
  }

  @Test
  public void checkImmFourthWeds() {
    DateWithDayCount JAN25_2012 = new DateWithDayCount(2012, 1, 25);
    Assert.assertFalse(calculator.matchesRollConvention(JAN25_2012, IMM));
    Assert.assertFalse(calculator.matchesRollConvention(JAN25_2012, DAY24));
    Assert.assertTrue(calculator.matchesRollConvention(JAN25_2012, DAY25));
    Assert.assertFalse(calculator.matchesRollConvention(JAN25_2012, DAY26));
    Assert.assertFalse(calculator.matchesRollConvention(JAN25_2012, EOM));
  }

  @Test
  public void checkMidMonth() {
    DateWithDayCount JAN19_2012 = new DateWithDayCount(2012, 1, 19);
    Assert.assertFalse(calculator.matchesRollConvention(JAN19_2012, IMM));
    Assert.assertFalse(calculator.matchesRollConvention(JAN19_2012, DAY17));
    Assert.assertFalse(calculator.matchesRollConvention(JAN19_2012, DAY18));
    Assert.assertTrue(calculator.matchesRollConvention(JAN19_2012, DAY19));
    Assert.assertFalse(calculator.matchesRollConvention(JAN19_2012, EOM));
  }

  @Test
  public void checkLeapYear() {
    DateWithDayCount FEB29_2012 = new DateWithDayCount(2012, 2, 29);
    Assert.assertFalse(calculator.matchesRollConvention(FEB29_2012, IMM));
    Assert.assertFalse(calculator.matchesRollConvention(FEB29_2012, DAY28));
    Assert.assertTrue(calculator.matchesRollConvention(FEB29_2012, DAY29));
    Assert.assertTrue(calculator.matchesRollConvention(FEB29_2012, DAY30));
    Assert.assertTrue(calculator.matchesRollConvention(FEB29_2012, EOM));
  }

  @Test
  public void checkLeapYear28Feb() {
    DateWithDayCount FEB28_2012 = new DateWithDayCount(2012, 2, 28);
    Assert.assertFalse(calculator.matchesRollConvention(FEB28_2012, IMM));
    Assert.assertTrue(calculator.matchesRollConvention(FEB28_2012, DAY28));
    Assert.assertFalse(calculator.matchesRollConvention(FEB28_2012, DAY29));
    Assert.assertFalse(calculator.matchesRollConvention(FEB28_2012, DAY30));
    Assert.assertFalse(calculator.matchesRollConvention(FEB28_2012, EOM));
  }

  @Test
  public void checkNonLeapYear() {
    DateWithDayCount FEB28_2011 = new DateWithDayCount(2011, 2, 28);
    Assert.assertFalse(calculator.matchesRollConvention(FEB28_2011, IMM));
    Assert.assertTrue(calculator.matchesRollConvention(FEB28_2011, DAY28));
    Assert.assertTrue(calculator.matchesRollConvention(FEB28_2011, DAY29));
    Assert.assertTrue(calculator.matchesRollConvention(FEB28_2011, DAY30));
    Assert.assertTrue(calculator.matchesRollConvention(FEB28_2011, EOM));
  }

  @Test(expected = PricerException.class)
  public void errorsOnFrn() {
    DateWithDayCount FEB29_2012 = new DateWithDayCount(2012, 2, 29);
    calculator.matchesRollConvention(FEB29_2012, FRN);
  }

  @Test
  public void testNoAdjustmentNeeded() {
    HolidayCalendarContainer calendars = new HolidayCalendarContainer();
    Assert.assertEquals(new DateWithDayCount(2012, 1, 11), calculator.adjustDate(new DateWithDayCount(2012, 1, 11), FOLLOWING, calendars));
    Assert.assertEquals(new DateWithDayCount(2012, 1, 11), calculator.adjustDate(new DateWithDayCount(2012, 1, 11), PRECEDING, calendars));
    Assert.assertEquals(new DateWithDayCount(2012, 1, 11), calculator.adjustDate(new DateWithDayCount(2012, 1, 11), NO_ADJUST, calendars));
    Assert.assertEquals(new DateWithDayCount(2012, 1, 11), calculator.adjustDate(new DateWithDayCount(2012, 1, 11), MODFOLLOWING, calendars));
    Assert.assertEquals(new DateWithDayCount(2012, 1, 11), calculator.adjustDate(new DateWithDayCount(2012, 1, 11), MODPRECEDING, calendars));
  }

  @Test
  public void testWeekendModFol() {
    HolidayCalendarContainer calendars = new HolidayCalendarContainer();
    Assert.assertEquals(new DateWithDayCount(2012, 4, 2), calculator.adjustDate(new DateWithDayCount(2012, 3, 31), FOLLOWING, calendars));
    Assert.assertEquals(new DateWithDayCount(2012, 3, 30), calculator.adjustDate(new DateWithDayCount(2012, 3, 31), PRECEDING, calendars));
    Assert.assertEquals(new DateWithDayCount(2012, 3, 31), calculator.adjustDate(new DateWithDayCount(2012, 3, 31), NO_ADJUST, calendars));
    Assert.assertEquals(new DateWithDayCount(2012, 3, 30), calculator.adjustDate(new DateWithDayCount(2012, 3, 31), MODFOLLOWING, calendars));
    Assert.assertEquals(new DateWithDayCount(2012, 3, 30), calculator.adjustDate(new DateWithDayCount(2012, 3, 31), MODPRECEDING, calendars));
  }

  @Test
  public void testWeekendModPrec() {
    HolidayCalendarContainer calendars = new HolidayCalendarContainer();
    Assert.assertEquals(new DateWithDayCount(2012, 1, 2), calculator.adjustDate(new DateWithDayCount(2012, 1, 1), FOLLOWING, calendars));
    Assert.assertEquals(new DateWithDayCount(2011, 12, 30), calculator.adjustDate(new DateWithDayCount(2012, 1, 1), PRECEDING, calendars));
    Assert.assertEquals(new DateWithDayCount(2012, 1, 1), calculator.adjustDate(new DateWithDayCount(2012, 1, 1), NO_ADJUST, calendars));
    Assert.assertEquals(new DateWithDayCount(2012, 1, 2), calculator.adjustDate(new DateWithDayCount(2012, 1, 1), MODFOLLOWING, calendars));
    Assert.assertEquals(new DateWithDayCount(2012, 1, 2), calculator.adjustDate(new DateWithDayCount(2012, 1, 1), MODPRECEDING, calendars));
  }

  @Test
  public void testAdjustmentForHolidayInOnlyNeeded() {
    HolidayCalendarContainer calendars = new HolidayCalendarContainer();
    TreeMap<String, HolidayCalendar> calendarMap = new TreeMap<String, HolidayCalendar>();
    calendarMap.put("GBLO", new HolidayCalendar("GBLO", Arrays.asList(new DateWithDayCount(2012, 1, 11))));
    calendars.setHolidayCalendars(calendarMap);

    Assert.assertEquals(new DateWithDayCount(2012, 1, 12), calculator.adjustDate(new DateWithDayCount(2012, 1, 11), FOLLOWING, calendars));
    Assert.assertEquals(new DateWithDayCount(2012, 1, 10), calculator.adjustDate(new DateWithDayCount(2012, 1, 11), PRECEDING, calendars));
    Assert.assertEquals(new DateWithDayCount(2012, 1, 11), calculator.adjustDate(new DateWithDayCount(2012, 1, 11), NO_ADJUST, calendars));
    Assert.assertEquals(new DateWithDayCount(2012, 1, 12), calculator.adjustDate(new DateWithDayCount(2012, 1, 11), MODFOLLOWING, calendars));
    Assert.assertEquals(new DateWithDayCount(2012, 1, 10), calculator.adjustDate(new DateWithDayCount(2012, 1, 11), MODPRECEDING, calendars));
  }

  @Test
  public void testAdjustmentForMondayHolidayInOnlyNeeded() {
    HolidayCalendarContainer calendars = new HolidayCalendarContainer();
    TreeMap<String, HolidayCalendar> calendarMap = new TreeMap<String, HolidayCalendar>();
    calendarMap.put("GBLO", new HolidayCalendar("GBLO", Arrays.asList(new DateWithDayCount(2012, 1, 9))));
    calendars.setHolidayCalendars(calendarMap);

    Assert.assertEquals(new DateWithDayCount(2012, 1, 10), calculator.adjustDate(new DateWithDayCount(2012, 1, 9), FOLLOWING, calendars));
    Assert.assertEquals(new DateWithDayCount(2012, 1, 6), calculator.adjustDate(new DateWithDayCount(2012, 1, 9), PRECEDING, calendars));
    Assert.assertEquals(new DateWithDayCount(2012, 1, 9), calculator.adjustDate(new DateWithDayCount(2012, 1, 9), NO_ADJUST, calendars));
    Assert.assertEquals(new DateWithDayCount(2012, 1, 10), calculator.adjustDate(new DateWithDayCount(2012, 1, 9), MODFOLLOWING, calendars));
    Assert.assertEquals(new DateWithDayCount(2012, 1, 6), calculator.adjustDate(new DateWithDayCount(2012, 1, 9), MODPRECEDING, calendars));
  }

  @Test
  public void testAdjustmentForMondayHolidayInSecondNeeded() {
    HolidayCalendarContainer calendars = new HolidayCalendarContainer();
    TreeMap<String, HolidayCalendar> calendarMap = new TreeMap<String, HolidayCalendar>();
    calendarMap.put("GBLO", new HolidayCalendar("GBLO", Collections.<DateWithDayCount>emptyList()));
    calendarMap.put("USNY", new HolidayCalendar("USNY", Arrays.asList(new DateWithDayCount(2012, 1, 9))));
    calendars.setHolidayCalendars(calendarMap);

    Assert.assertEquals(new DateWithDayCount(2012, 1, 10), calculator.adjustDate(new DateWithDayCount(2012, 1, 9), FOLLOWING, calendars));
    Assert.assertEquals(new DateWithDayCount(2012, 1, 6), calculator.adjustDate(new DateWithDayCount(2012, 1, 9), PRECEDING, calendars));
    Assert.assertEquals(new DateWithDayCount(2012, 1, 9), calculator.adjustDate(new DateWithDayCount(2012, 1, 9), NO_ADJUST, calendars));
    Assert.assertEquals(new DateWithDayCount(2012, 1, 10), calculator.adjustDate(new DateWithDayCount(2012, 1, 9), MODFOLLOWING, calendars));
    Assert.assertEquals(new DateWithDayCount(2012, 1, 6), calculator.adjustDate(new DateWithDayCount(2012, 1, 9), MODPRECEDING, calendars));
  }

  @Test
  public void singleExactPeriod() {
    Assert.assertEquals(Arrays.asList(
      new DateWithDayCount(2012, 1, 11),
      new DateWithDayCount(2012, 7, 11)
    ), calculator.calculateUnadjustedPeriodDates(new DateWithDayCount(2012, 1, 11), null, null, new DateWithDayCount(2012, 7, 11), new CalculationPeriodFrequency(6, PeriodEnum.M, DAY11)));
  }

  @Test
  public void singleShortPeriod() {
    Assert.assertEquals(Arrays.asList(
      new DateWithDayCount(2012, 1, 11),
      new DateWithDayCount(2012, 7, 11)
    ), calculator.calculateUnadjustedPeriodDates(new DateWithDayCount(2012, 1, 11), null, null, new DateWithDayCount(2012, 7, 11), new CalculationPeriodFrequency(1, PeriodEnum.Y, DAY11)));
  }

  @Test
  public void shortInitialStubPeriod() {
    Assert.assertEquals(Arrays.asList(
      new DateWithDayCount(2012, 1, 11),
      new DateWithDayCount(2012, 1, 17),
      new DateWithDayCount(2012, 7, 17)
    ), calculator.calculateUnadjustedPeriodDates(new DateWithDayCount(2012, 1, 11), new DateWithDayCount(2012, 1, 17), null, new DateWithDayCount(2012, 7, 17), new CalculationPeriodFrequency(6, PeriodEnum.M, DAY17)));
    Assert.assertEquals(Arrays.asList(
      new DateWithDayCount(2012, 1, 11),
      new DateWithDayCount(2012, 2, 1),
      new DateWithDayCount(2012, 8, 1)
    ), calculator.calculateUnadjustedPeriodDates(new DateWithDayCount(2012, 1, 11), new DateWithDayCount(2012, 2, 1), null, new DateWithDayCount(2012, 8, 1), new CalculationPeriodFrequency(6, PeriodEnum.M, DAY17)));
  }

  @Test
  public void shortFinalStubPeriod() {
    Assert.assertEquals(Arrays.asList(
      new DateWithDayCount(2012, 1, 11),
      new DateWithDayCount(2012, 7, 11),
      new DateWithDayCount(2012, 7, 17)
    ), calculator.calculateUnadjustedPeriodDates(new DateWithDayCount(2012, 1, 11), null, new DateWithDayCount(2012, 7, 11), new DateWithDayCount(2012, 7, 17), new CalculationPeriodFrequency(6, PeriodEnum.M, DAY17)));
    Assert.assertEquals(Arrays.asList(
      new DateWithDayCount(2012, 1, 11),
      new DateWithDayCount(2012, 7, 11),
      new DateWithDayCount(2012, 8, 1)
    ), calculator.calculateUnadjustedPeriodDates(new DateWithDayCount(2012, 1, 11), null, new DateWithDayCount(2012, 7, 11), new DateWithDayCount(2012, 8, 1), new CalculationPeriodFrequency(6, PeriodEnum.M, DAY17)));
  }

  @Test
  public void testNormalPeriod() {
    Assert.assertEquals(Arrays.asList(
      new DateWithDayCount(2012, 1, 11),
      new DateWithDayCount(2012, 2, 11),
      new DateWithDayCount(2012, 3, 11),
      new DateWithDayCount(2012, 4, 11),
      new DateWithDayCount(2012, 5, 11),
      new DateWithDayCount(2012, 6, 11),
      new DateWithDayCount(2012, 7, 11),
      new DateWithDayCount(2012, 8, 11)
    ), calculator.calculateUnadjustedPeriodDates(new DateWithDayCount(2012, 1, 11), null, null, new DateWithDayCount(2012, 8, 11), new CalculationPeriodFrequency(1, PeriodEnum.M, DAY11)));
  }

  @Test
  public void singleTerm() {
    Assert.assertEquals(Arrays.asList(
      new DateWithDayCount(2012, 1, 11),
      new DateWithDayCount(2012, 8, 11)
    ), calculator.calculateUnadjustedPeriodDates(new DateWithDayCount(2012, 1, 11), null, null, new DateWithDayCount(2012, 8, 11), new CalculationPeriodFrequency(1, PeriodEnum.T, DAY11)));
    Assert.assertEquals(Arrays.asList(
      new DateWithDayCount(2012, 1, 11),
      new DateWithDayCount(2014, 8, 11)
    ), calculator.calculateUnadjustedPeriodDates(new DateWithDayCount(2012, 1, 11), null, null, new DateWithDayCount(2014, 8, 11), new CalculationPeriodFrequency(1, PeriodEnum.T, DAY11)));
  }

  @Test
  public void calculateFpmlPeriodDates() {
    List<SwapStream> streams = fpmlParser.parse(getClass().getResourceAsStream("/LCH00000513426.xml"));
    SwapStream s1 = streams.get(0);
    SwapStream s2 = streams.get(1);
    Assert.assertEquals(Arrays.asList(
      new DateWithDayCount(2008, 6, 13),
      new DateWithDayCount(2009, 6, 13),
      new DateWithDayCount(2010, 6, 13),
      new DateWithDayCount(2011, 6, 13),
      new DateWithDayCount(2012, 6, 13),
      new DateWithDayCount(2013, 6, 13),
      new DateWithDayCount(2014, 6, 13),
      new DateWithDayCount(2015, 6, 13),
      new DateWithDayCount(2016, 6, 13),
      new DateWithDayCount(2017, 6, 13),
      new DateWithDayCount(2018, 6, 13)
    ), calculator.calculateUnadjustedPeriodDates(s1.getEffectiveDate().getUnadjustedDate(), s1.getFirstRegularPeriodStartDate(), s1.getLastRegularPeriodEndDate(), s1.getTerminationDate().getUnadjustedDate(), s1.getCalculationPeriodFrequency()));
    Assert.assertEquals(Arrays.asList(
      new DateWithDayCount(2008, 6, 13),
      new DateWithDayCount(2008, 12, 13),
      new DateWithDayCount(2009, 6, 13),
      new DateWithDayCount(2009, 12, 13),
      new DateWithDayCount(2010, 6, 13),
      new DateWithDayCount(2010, 12, 13),
      new DateWithDayCount(2011, 6, 13),
      new DateWithDayCount(2011, 12, 13),
      new DateWithDayCount(2012, 6, 13),
      new DateWithDayCount(2012, 12, 13),
      new DateWithDayCount(2013, 6, 13),
      new DateWithDayCount(2013, 12, 13),
      new DateWithDayCount(2014, 6, 13),
      new DateWithDayCount(2014, 12, 13),
      new DateWithDayCount(2015, 6, 13),
      new DateWithDayCount(2015, 12, 13),
      new DateWithDayCount(2016, 6, 13),
      new DateWithDayCount(2016, 12, 13),
      new DateWithDayCount(2017, 6, 13),
      new DateWithDayCount(2017, 12, 13),
      new DateWithDayCount(2018, 6, 13)
    ), calculator.calculateUnadjustedPeriodDates(s2.getEffectiveDate().getUnadjustedDate(), s2.getFirstRegularPeriodStartDate(), s2.getLastRegularPeriodEndDate(), s2.getTerminationDate().getUnadjustedDate(), s2.getCalculationPeriodFrequency()));
  }

  @Test
  public void shiftNoBusinessDay() {
    Assert.assertEquals(new DateWithDayCount(2012, 1, 13), calculator.shift(new DateWithDayCount(2012, 1, 13), 0, PeriodEnum.D, DayTypeEnum.BUSINESS, BusinessDayConventionEnum.FOLLOWING, london));
    Assert.assertEquals(new DateWithDayCount(2012, 1, 13), calculator.shift(new DateWithDayCount(2012, 1, 13), 0, PeriodEnum.D, DayTypeEnum.BUSINESS, BusinessDayConventionEnum.PRECEDING, london));
    Assert.assertEquals(new DateWithDayCount(2012, 1, 13), calculator.shift(new DateWithDayCount(2012, 1, 13), 0, PeriodEnum.D, DayTypeEnum.BUSINESS, BusinessDayConventionEnum.MODFOLLOWING, london));
    Assert.assertEquals(new DateWithDayCount(2012, 1, 13), calculator.shift(new DateWithDayCount(2012, 1, 13), 0, PeriodEnum.D, DayTypeEnum.BUSINESS, BusinessDayConventionEnum.MODPRECEDING, london));
  }

  @Test
  public void shiftNoCalendarDay() {
    Assert.assertEquals(new DateWithDayCount(2012, 1, 13), calculator.shift(new DateWithDayCount(2012, 1, 13), 0, PeriodEnum.D, DayTypeEnum.CALENDAR, BusinessDayConventionEnum.FOLLOWING, london));
    Assert.assertEquals(new DateWithDayCount(2012, 1, 13), calculator.shift(new DateWithDayCount(2012, 1, 13), 0, PeriodEnum.D, DayTypeEnum.CALENDAR, BusinessDayConventionEnum.PRECEDING, london));
    Assert.assertEquals(new DateWithDayCount(2012, 1, 13), calculator.shift(new DateWithDayCount(2012, 1, 13), 0, PeriodEnum.D, DayTypeEnum.CALENDAR, BusinessDayConventionEnum.MODFOLLOWING, london));
    Assert.assertEquals(new DateWithDayCount(2012, 1, 13), calculator.shift(new DateWithDayCount(2012, 1, 13), 0, PeriodEnum.D, DayTypeEnum.CALENDAR, BusinessDayConventionEnum.MODPRECEDING, london));
  }

  @Test
  public void shiftPositiveBusinessDay() {
  }

  @Test
  public void shiftPositiveCalendarDay() {
  }

  @Test
  public void shiftNegativeBusinessDay() {
  }

  @Test
  public void shiftNegativeCalendarDay() {
  }
}
