package com.twock.swappricer.test;

import java.util.*;

import com.twock.swappricer.HolidayCalendar;
import com.twock.swappricer.fpml.woodstox.model.DateWithDayCount;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author Chris Pearson (chris@twock.com)
 */
public class HolidayCalendarTest {
  private static final int RANDOM_COUNT = 500;

  @Test
  public void testConstructorSorting() {
    List<DateWithDayCount> dates = new ArrayList<DateWithDayCount>(RANDOM_COUNT);
    int[] dayCounts = new int[RANDOM_COUNT];

    long[] randomTimes = new long[RANDOM_COUNT];
    long startTime = getMillis(1900, 1, 1);
    long endTime = getMillis(2099, 12, 31);
    Calendar calendar = Calendar.getInstance();
    for(int i = 0; i < RANDOM_COUNT; i++) {
      randomTimes[i] = Math.round(startTime + Math.random() * (double)(endTime - startTime));
      calendar.setTimeInMillis(randomTimes[i]);
      dates.add(new DateWithDayCount(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH)));
    }

    HolidayCalendar c1 = new HolidayCalendar("", dates);

    Arrays.sort(randomTimes);
    for(int i = 0; i < RANDOM_COUNT; i++) {
      calendar.setTimeInMillis(randomTimes[i]);
      dayCounts[i] = dates.get(i).getDayCount();
    }
    Assert.assertArrayEquals(dayCounts, c1.getHolidayDayCounts());
  }

  private static long getMillis(int year, int month, int day) {
    Calendar calendar = Calendar.getInstance();
    calendar.set(year, month - 1, day, 0, 0);
    calendar.set(Calendar.MILLISECOND, 0);
    return calendar.getTimeInMillis();
  }

  @Test
  public void checkHolidays() {
    DateWithDayCount SUN_1JAN = new DateWithDayCount(2012, 1, 1);
    DateWithDayCount MON_2JAN = new DateWithDayCount(2012, 1, 2);
    DateWithDayCount TUE_3JAN = new DateWithDayCount(2012, 1, 3);
    HolidayCalendar calendar = new HolidayCalendar(null, Arrays.asList(MON_2JAN));

    Assert.assertFalse(calendar.isPublicHoliday(SUN_1JAN));
    Assert.assertTrue(calendar.isPublicHoliday(MON_2JAN));
    Assert.assertFalse(calendar.isPublicHoliday(TUE_3JAN));
  }

  @Test
  public void checkCodePersisted() {
    HolidayCalendar calendar = new HolidayCalendar("MyCode", Collections.<DateWithDayCount>emptyList());
    Assert.assertEquals("MyCode", calendar.getCode());
    calendar = new HolidayCalendar(null, Collections.<DateWithDayCount>emptyList());
    Assert.assertNull(calendar.getCode());
  }
}
