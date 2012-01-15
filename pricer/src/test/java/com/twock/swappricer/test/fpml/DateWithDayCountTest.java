package com.twock.swappricer.test.fpml;

import com.twock.swappricer.fpml.model.DateWithDayCount;
import com.twock.swappricer.fpml.model.enumeration.RollConventionEnum;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author Chris Pearson (chris@twock.com)
 */
public class DateWithDayCountTest {
  @Test
  public void addMonthsPositive() {
    Assert.assertEquals(new DateWithDayCount(2012, 1, 11), new DateWithDayCount(2012, 1, 11).addMonths(0));
    Assert.assertEquals(new DateWithDayCount(2012, 2, 11), new DateWithDayCount(2012, 1, 11).addMonths(1));
    Assert.assertEquals(new DateWithDayCount(2012, 3, 11), new DateWithDayCount(2012, 1, 11).addMonths(2));
    Assert.assertEquals(new DateWithDayCount(2013, 1, 11), new DateWithDayCount(2012, 1, 11).addMonths(12));
  }

  @Test
  public void addMonthsNegative() {
    Assert.assertEquals(new DateWithDayCount(2012, 1, 11), new DateWithDayCount(2012, 1, 11).addMonths(-0));
    Assert.assertEquals(new DateWithDayCount(2011, 12, 11), new DateWithDayCount(2012, 1, 11).addMonths(-1));
    Assert.assertEquals(new DateWithDayCount(2011, 11, 11), new DateWithDayCount(2012, 1, 11).addMonths(-2));
    Assert.assertEquals(new DateWithDayCount(2011, 1, 11), new DateWithDayCount(2012, 1, 11).addMonths(-12));
  }

  @Test
  public void addMonthsEndOfMonth() {
    Assert.assertEquals(new DateWithDayCount(2012, 2, 29), new DateWithDayCount(2012, 1, 31).addMonths(1));
    Assert.assertEquals(new DateWithDayCount(2012, 3, 29), new DateWithDayCount(2012, 2, 29).addMonths(1));
    Assert.assertEquals(new DateWithDayCount(2012, 4, 30), new DateWithDayCount(2012, 3, 31).addMonths(1));
  }

  @Test
  public void addMonthsNegativeEndOfMonth() {
    Assert.assertEquals(new DateWithDayCount(2011, 12, 31), new DateWithDayCount(2012, 1, 31).addMonths(-1));
    Assert.assertEquals(new DateWithDayCount(2012, 1, 29), new DateWithDayCount(2012, 2, 29).addMonths(-1));
    Assert.assertEquals(new DateWithDayCount(2012, 2, 29), new DateWithDayCount(2012, 3, 31).addMonths(-1));
    Assert.assertEquals(new DateWithDayCount(2012, 3, 30), new DateWithDayCount(2012, 4, 30).addMonths(-1));
  }

  @Test
  public void addMonthsWithRollZero() {
    Assert.assertEquals(new DateWithDayCount(2012, 1, 25), new DateWithDayCount(2012, 1, 11).addMonths(0, RollConventionEnum.DAY25));
    Assert.assertEquals(new DateWithDayCount(2012, 1, 10), new DateWithDayCount(2012, 1, 11).addMonths(0, RollConventionEnum.DAY10));
    Assert.assertEquals(new DateWithDayCount(2012, 1, 11), new DateWithDayCount(2012, 1, 11).addMonths(0, RollConventionEnum.DAY11));
    Assert.assertEquals(new DateWithDayCount(2012, 1, 31), new DateWithDayCount(2012, 1, 11).addMonths(0, RollConventionEnum.EOM));
    Assert.assertEquals(new DateWithDayCount(2012, 1, 18), new DateWithDayCount(2012, 1, 11).addMonths(0, RollConventionEnum.IMM));
  }

  @Test
  public void addMonthsWithRollPositive() {
    Assert.assertEquals(new DateWithDayCount(2012, 2, 25), new DateWithDayCount(2012, 1, 11).addMonths(1, RollConventionEnum.DAY25));
    Assert.assertEquals(new DateWithDayCount(2012, 2, 10), new DateWithDayCount(2012, 1, 11).addMonths(1, RollConventionEnum.DAY10));
    Assert.assertEquals(new DateWithDayCount(2012, 2, 11), new DateWithDayCount(2012, 1, 11).addMonths(1, RollConventionEnum.DAY11));
    Assert.assertEquals(new DateWithDayCount(2012, 2, 29), new DateWithDayCount(2012, 1, 11).addMonths(1, RollConventionEnum.EOM));
    Assert.assertEquals(new DateWithDayCount(2012, 2, 15), new DateWithDayCount(2012, 1, 11).addMonths(1, RollConventionEnum.IMM));
    Assert.assertEquals(new DateWithDayCount(2012, 7, 25), new DateWithDayCount(2012, 1, 11).addMonths(6, RollConventionEnum.DAY25));
    Assert.assertEquals(new DateWithDayCount(2012, 7, 10), new DateWithDayCount(2012, 1, 11).addMonths(6, RollConventionEnum.DAY10));
    Assert.assertEquals(new DateWithDayCount(2012, 7, 11), new DateWithDayCount(2012, 1, 11).addMonths(6, RollConventionEnum.DAY11));
    Assert.assertEquals(new DateWithDayCount(2012, 7, 31), new DateWithDayCount(2012, 1, 11).addMonths(6, RollConventionEnum.EOM));
    Assert.assertEquals(new DateWithDayCount(2012, 7, 18), new DateWithDayCount(2012, 1, 11).addMonths(6, RollConventionEnum.IMM));
  }

  @Test
  public void addMonthsWithRollNegative() {
    Assert.assertEquals(new DateWithDayCount(2011, 12, 25), new DateWithDayCount(2012, 1, 11).addMonths(-1, RollConventionEnum.DAY25));
    Assert.assertEquals(new DateWithDayCount(2011, 12, 10), new DateWithDayCount(2012, 1, 11).addMonths(-1, RollConventionEnum.DAY10));
    Assert.assertEquals(new DateWithDayCount(2011, 12, 11), new DateWithDayCount(2012, 1, 11).addMonths(-1, RollConventionEnum.DAY11));
    Assert.assertEquals(new DateWithDayCount(2011, 12, 31), new DateWithDayCount(2012, 1, 11).addMonths(-1, RollConventionEnum.EOM));
    Assert.assertEquals(new DateWithDayCount(2011, 12, 21), new DateWithDayCount(2012, 1, 11).addMonths(-1, RollConventionEnum.IMM));
    Assert.assertEquals(new DateWithDayCount(2011, 7, 25), new DateWithDayCount(2012, 1, 11).addMonths(-6, RollConventionEnum.DAY25));
    Assert.assertEquals(new DateWithDayCount(2011, 7, 10), new DateWithDayCount(2012, 1, 11).addMonths(-6, RollConventionEnum.DAY10));
    Assert.assertEquals(new DateWithDayCount(2011, 7, 11), new DateWithDayCount(2012, 1, 11).addMonths(-6, RollConventionEnum.DAY11));
    Assert.assertEquals(new DateWithDayCount(2011, 7, 31), new DateWithDayCount(2012, 1, 11).addMonths(-6, RollConventionEnum.EOM));
    Assert.assertEquals(new DateWithDayCount(2011, 7, 20), new DateWithDayCount(2012, 1, 11).addMonths(-6, RollConventionEnum.IMM));
  }
}
