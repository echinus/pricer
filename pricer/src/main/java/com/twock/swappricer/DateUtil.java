package com.twock.swappricer;

import java.util.Calendar;

import com.twock.swappricer.fpml.woodstox.model.DateWithDayCount;

/**
 * @author Chris Pearson (chris@twock.com)
 */
public class DateUtil {
  /**
   * Get the number of days since 1st March 0000.
   *
   * @param date [year, month, day]
   * @return number of days since 01/03/0000
   */
  public static int dateToDayCount(short[] date) {
    int m = (date[1] + 9) % 12;
    int y = date[0] - m / 10;
    return 365 * y + y / 4 - y / 100 + y / 400 + (m * 306 + 5) / 10 + (date[2] - 1);
  }

  /**
   * Convert the number of days since 01/03/0000 into an array of [year, month, day].
   *
   * @param dayCount number of days since 01/03/0000
   * @return an array [year, month, day]
   */
  public static short[] dayCountToDate(int dayCount) {
    int y = (int)((10000L * dayCount + 14780) / 3652425);
    int ddd = dayCount - (y * 365 + y / 4 - y / 100 + y / 400);
    if(ddd < 0) {
      y--;
      ddd = dayCount - (y * 365 + y / 4 - y / 100 + y / 400);
    }
    int mi = (52 + 100 * ddd) / 3060;
    int year = y + (mi + 2) / 12;
    int month = (mi + 2) % 12 + 1;
    int day = ddd - (mi * 306 + 5) / 10 + 1;
    return new short[]{(short)year, (short)month, (short)day};
  }
}
