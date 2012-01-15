package com.twock.swappricer.fpml.model;

import java.util.Arrays;
import java.util.Calendar;

import com.twock.swappricer.DateUtil;
import com.twock.swappricer.PricerException;
import com.twock.swappricer.fpml.model.enumeration.RollConventionEnum;

public class DateWithDayCount implements Comparable<DateWithDayCount> {
  private short[] date;
  private Integer dayCount;

  public DateWithDayCount(DateWithDayCount toCopy) {
    this.date = toCopy.date;
    this.dayCount = toCopy.dayCount;
  }

  public DateWithDayCount(short[] date) {
    this.date = date;
  }

  public DateWithDayCount(int dayCount) {
    this.dayCount = dayCount;
  }

  public DateWithDayCount(short year, short month, short day) {
    this(new short[]{year, month, day});
  }

  public DateWithDayCount(int year, int month, int day) {
    this(new short[]{(short)year, (short)month, (short)day});
  }

  public short[] getDate() {
    if(date == null) {
      date = DateUtil.dayCountToDate(dayCount);
    }
    return date;
  }

  public void setDate(short[] date) {
    this.date = date;
    this.dayCount = null;
  }

  public int getDayCount() {
    if(dayCount == null) {
      dayCount = DateUtil.dateToDayCount(date);
    }
    return dayCount;
  }

  public void setDayCount(int dayCount) {
    this.date = null;
    this.dayCount = dayCount;
  }

  public boolean isWeekend() {
    int dayOfWeek = getDayOfWeek();
    return dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY;
  }

  public int getDayOfWeek() {
    return (getDayCount() + 3) % 7 + 1;
  }

  public short getYear() {
    return getDate()[0];
  }

  public short getMonthOfYear() {
    return getDate()[1];
  }

  public short getDayOfMonth() {
    return getDate()[2];
  }

  public DateWithDayCount addDays(int daysToAdd) {
    setDayCount(getDayCount() + daysToAdd);
    return this;
  }

  /**
   * Adds the given number of months to the date, keeping where possible the same day of the month.  Where this is not
   * possible (e.g. 31st January + 1 month, there's no 31st February), the last day of the new month will be used.
   *
   * @param monthsToAdd number of months to add to this date, can be negative
   * @return convenience value for method chaining - returns this date instance
   */
  public DateWithDayCount addMonths(int monthsToAdd) {
    short[] dateNow = getDate();
    int newYearMonth = dateNow[0] * 12 + dateNow[1] - 1 + monthsToAdd;
    short[] firstDayOfMonthAfter = {(short)((newYearMonth + 1) / 12), (short)((newYearMonth + 1) % 12 + 1), 1};
    short[] dateAfterAdd = {(short)(newYearMonth / 12), (short)(newYearMonth % 12 + 1), dateNow[2]};
    setDayCount(Math.min(DateUtil.dateToDayCount(firstDayOfMonthAfter) - 1, DateUtil.dateToDayCount(dateAfterAdd)));
    return this;
  }

  /**
   * Adds the given number of months to the date, applying the given roll convention.  Where this is not possible (e.g.
   * 31st January + 1 month with roll convention DAY31, there's no 31st February), the last day of the new month will be
   * used.
   *
   * @param monthsToAdd number of months to add to this date, can be negative
   * @param rollConvention the roll convention to apply after the add
   * @return convenience value for method chaining - returns this date instance
   */
  public DateWithDayCount addMonths(int monthsToAdd, RollConventionEnum rollConvention) {
    short[] dateNow = getDate();
    int newYearMonth = dateNow[0] * 12 + dateNow[1] - 1 + monthsToAdd;
    short[] firstDayOfMonthAfter = {(short)((newYearMonth + 1) / 12), (short)((newYearMonth + 1) % 12 + 1), 1};
    if(rollConvention == RollConventionEnum.EOM) {
      setDayCount(DateUtil.dateToDayCount(firstDayOfMonthAfter) - 1);
    } else {
      short[] dateAfterAdd;
      if(rollConvention == RollConventionEnum.IMM) {
        // adjust to third wednesday
        dateAfterAdd = new short[]{(short)(newYearMonth / 12), (short)(newYearMonth % 12 + 1), 1};
        setDate(dateAfterAdd);
        int dayOfWeek = getDayOfWeek();
        int immDayCount = getDayCount() + 14 + ((11 - dayOfWeek) % 7);
        setDayCount(immDayCount);
      } else if(RollConventionEnum.DAY_ROLLS.contains(rollConvention)) {
        dateAfterAdd = new short[]{(short)(newYearMonth / 12), (short)(newYearMonth % 12 + 1), Short.parseShort(rollConvention.value())};
        setDayCount(Math.min(DateUtil.dateToDayCount(firstDayOfMonthAfter) - 1, DateUtil.dateToDayCount(dateAfterAdd)));
      } else {
        throw new PricerException("Unhandled roll convention " + rollConvention);
      }
    }
    return this;
  }

  @Override
  public int compareTo(DateWithDayCount o) {
    return getDayCount() - o.getDayCount();
  }

  @Override
  public boolean equals(Object o) {
    if(this == o) {
      return true;
    }
    if(o == null || getClass() != o.getClass()) {
      return false;
    }

    DateWithDayCount that = (DateWithDayCount)o;
    return getDayCount() == that.getDayCount();
  }

  @Override
  public int hashCode() {
    return getDayCount();
  }

  @Override
  public String toString() {
    return "DateWithDayCount" + Arrays.toString(getDate());
  }
}