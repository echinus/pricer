package com.twock.swappricer;

import java.util.Arrays;
import java.util.List;

import com.twock.swappricer.fpml.woodstox.model.DateWithDayCount;

/**
 * @author Chris Pearson (chris@twock.com)
 */
public class HolidayCalendar {
  private final String code;
  private final int[] holidayDayCounts;

  public HolidayCalendar(String code, List<DateWithDayCount> holidayDates) {
    this.code = code;
    this.holidayDayCounts = new int[holidayDates.size()];
    for(int i = 0; i < holidayDates.size(); i++) {
      holidayDayCounts[i] = holidayDates.get(i).getDayCount();
    }
  }

  public String getCode() {
    return code;
  }

  public int[] getHolidayDayCounts() {
    return holidayDayCounts;
  }

  public boolean isPublicHoliday(DateWithDayCount date) {
    return Arrays.binarySearch(holidayDayCounts, date.getDayCount()) >= 0;
  }
}
