package com.twock.swappricer.test;

import java.util.Calendar;

import com.twock.swappricer.DateUtil;
import com.twock.swappricer.fpml.model.DateWithDayCount;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author Chris Pearson (chris@twock.com)
 */
public class DateUtilTest {
  @Test
  public void testDateToDayCount() {
    Assert.assertEquals(693901, DateUtil.dateToDayCount(new short[]{1900, 1, 1}));
    Assert.assertEquals(735173, DateUtil.dateToDayCount(new short[]{2012, 12, 31}));
    Assert.assertEquals(807917, DateUtil.dateToDayCount(new short[]{2212, 3, 2}));
  }

  @Test
  public void testDayCountToDate() {
    Assert.assertArrayEquals(new short[]{1900, 1, 1}, DateUtil.dayCountToDate(693901));
    Assert.assertArrayEquals(new short[]{2012, 12, 31}, DateUtil.dayCountToDate(735173));
    Assert.assertArrayEquals(new short[]{2212, 3, 2}, DateUtil.dayCountToDate(807917));
  }

  @Test
  public void dayOfTheWeekTest() {
    Assert.assertEquals(Calendar.SATURDAY, new DateWithDayCount(2012, 1, 7).getDayOfWeek());
    Assert.assertEquals(Calendar.SUNDAY, new DateWithDayCount(2012, 1, 8).getDayOfWeek());
    Assert.assertEquals(Calendar.MONDAY, new DateWithDayCount(2012, 1, 9).getDayOfWeek());
    Assert.assertEquals(Calendar.TUESDAY, new DateWithDayCount(2012, 1, 17).getDayOfWeek());
    Assert.assertEquals(Calendar.WEDNESDAY, new DateWithDayCount(2012, 1, 18).getDayOfWeek());
    Assert.assertEquals(Calendar.THURSDAY, new DateWithDayCount(2012, 1, 19).getDayOfWeek());
    Assert.assertEquals(Calendar.FRIDAY, new DateWithDayCount(2012, 1, 20).getDayOfWeek());
  }

  @Test
  public void checkWeekends() {
    Assert.assertTrue(new DateWithDayCount(2012, 1, 1).isWeekend());
    Assert.assertFalse(new DateWithDayCount(2012, 1, 2).isWeekend());
  }
}
