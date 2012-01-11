package com.twock.swappricer.test;

import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import com.twock.swappricer.HolidayCalendarContainer;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author Chris Pearson (chris@twock.com)
 */
public class HolidayCalendarContainerTest {
  @Test
  public void testRead() throws UnsupportedEncodingException {
    HolidayCalendarContainer container = new HolidayCalendarContainer();
    container.loadFromTsv(new InputStreamReader(getClass().getResourceAsStream("/calendars.tsv"), "UTF8"));
    Assert.assertEquals(container.getHolidayCalendars().keySet().toString(), 36, container.getHolidayCalendars().size());
  }
}
