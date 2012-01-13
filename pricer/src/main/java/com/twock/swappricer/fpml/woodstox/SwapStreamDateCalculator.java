package com.twock.swappricer.fpml.woodstox;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.twock.swappricer.DateUtil;
import com.twock.swappricer.HolidayCalendarContainer;
import com.twock.swappricer.PricerException;
import com.twock.swappricer.fpml.woodstox.model.*;
import com.twock.swappricer.fpml.woodstox.model.enumeration.*;

/**
 * @author Chris Pearson (chris@twock.com)
 */
public class SwapStreamDateCalculator {
  public boolean hasInitialStub(SwapStream swapStream) {
    DateWithDayCount effectiveDate = swapStream.getEffectiveDate().getUnadjustedDate();
    if(!matchesRollConvention(effectiveDate, swapStream.getCalculationPeriodFrequency().getRollConvention())) {
      return true;
    }
    return false;
  }

  public boolean matchesRollConvention(DateWithDayCount date, RollConventionEnum rollConvention) {
    switch(rollConvention) {
      case EOM:
        short[] nextDate = DateUtil.dayCountToDate(date.getDayCount() + 1);
        return nextDate[1] != date.getDate()[1];
      case IMM:
        // third wednesday of the month
        if(date.getDayOfWeek() != Calendar.WEDNESDAY) {
          return false;
        }
        // 1 sunday, 2 mon, 3 tue, 4 wed,..., 7 sat
        // if dow=1, add 17     11-dow=3
        // if dow=2, add 16     11-dow=2
        // if dow=3, add 15     11-dow=1
        // if dow=4, add 14     11-dow=0
        // if dow=5, add 20     11-dow=6
        // if dow=6, add 19     11-dow=5
        // if dow=7, add 18     11-dow=4
        // so add 14+(11-dow)%7
        DateWithDayCount firstOfMonth = new DateWithDayCount(date.getYear(), date.getMonthOfYear(), 1);
        int dayOfWeek = firstOfMonth.getDayOfWeek();
        int immDayCount = firstOfMonth.getDayCount() + 14 + ((11 - dayOfWeek) % 7);
        return immDayCount == date.getDayCount();

      default:
        if(!rollConvention.value().matches("\\d+")) {
          throw new PricerException("Unhandled roll convention: " + rollConvention);
        }
        int roll = Integer.parseInt(rollConvention.value());
        if(roll == date.getDate()[2]) {
          return true;
        }
        // roll conventions greater than number of days in the month must match too
        if(roll >= 29) {
          nextDate = DateUtil.dayCountToDate(date.getDayCount() + 1);
          return nextDate[1] != date.getDate()[1];
        } else {
          return false;
        }
    }
  }

  public DateWithDayCount adjustDate(DateWithDayCount startDate, BusinessDayConventionEnum businessDayConvention, HolidayCalendarContainer calendars) {
    if(businessDayConvention == BusinessDayConventionEnum.NO_ADJUST) {
      return startDate;
    }
    DateWithDayCount currentDay = new DateWithDayCount(startDate);
    while(calendars.isWeekendOrPublicHoliday(currentDay)) {
      int dayOfWeek = currentDay.getDayOfWeek();
      switch(businessDayConvention) {
        case FOLLOWING:
          currentDay.addDays(dayOfWeek == Calendar.SATURDAY ? 2 : 1);
          break;
        case MODFOLLOWING:
          currentDay.addDays(1);
          if(currentDay.getMonthOfYear() != startDate.getMonthOfYear()) {
            // if we've strayed to the next month, rewind to the starting date, and go backwards instead
            return adjustDate(startDate, BusinessDayConventionEnum.PRECEDING, calendars);
          }
          break;
        case PRECEDING:
          currentDay.addDays(dayOfWeek == Calendar.SUNDAY ? -2 : -1);
          break;
        case MODPRECEDING:
          currentDay.addDays(-1);
          if(currentDay.getMonthOfYear() != startDate.getMonthOfYear()) {
            // if we've strayed to the previous month, fast forward to the starting date, and go forwards instead
            return adjustDate(startDate, BusinessDayConventionEnum.FOLLOWING, calendars);
          }
          break;
        case NEAREST:
          if(dayOfWeek == Calendar.SUNDAY || dayOfWeek == Calendar.MONDAY) {
            return adjustDate(startDate, BusinessDayConventionEnum.FOLLOWING, calendars);
          } else {
            return adjustDate(startDate, BusinessDayConventionEnum.PRECEDING, calendars);
          }
        default:
          throw new PricerException("Unhandled business day convention " + businessDayConvention);
      }
    }
    return currentDay;
  }

  public List<DateWithDayCount> calculateUnadjustedPeriodDates(DateWithDayCount effectiveDate, DateWithDayCount firstRegularPeriodStartDate, DateWithDayCount lastRegularPeriodEndDate, DateWithDayCount terminationDate, CalculationPeriodFrequency calculationPeriodFrequency) {
    List<DateWithDayCount> result = new ArrayList<DateWithDayCount>();
    result.add(effectiveDate);

    if(calculationPeriodFrequency.getPeriod() != PeriodEnum.T) {
      DateWithDayCount start = effectiveDate;
      if(firstRegularPeriodStartDate != null && firstRegularPeriodStartDate.compareTo(terminationDate) < 0) {
        result.add(firstRegularPeriodStartDate);
        start = firstRegularPeriodStartDate;
      }
      DateWithDayCount end = lastRegularPeriodEndDate == null ? terminationDate : lastRegularPeriodEndDate;

      DateWithDayCount current = new DateWithDayCount(start);
      while(current.compareTo(end) < 0) {
        switch(calculationPeriodFrequency.getPeriod()) {
          case D:
            current.addDays(calculationPeriodFrequency.getPeriodMultiplier());
            break;
          case W:
            current.addDays(7 * calculationPeriodFrequency.getPeriodMultiplier());
            break;
          case M:
            current.addMonths(calculationPeriodFrequency.getPeriodMultiplier());
            break;
          case Y:
            current.addMonths(calculationPeriodFrequency.getPeriodMultiplier() * 12);
            break;
        }
        if(current.compareTo(end) < 0) {
          result.add(new DateWithDayCount(current));
        }
      }

      if(lastRegularPeriodEndDate != null && lastRegularPeriodEndDate.compareTo(effectiveDate) > 0) {
        result.add(lastRegularPeriodEndDate);
      }
    }
    result.add(terminationDate);
    return result;
  }

  public DateWithDayCount shift(DateWithDayCount date, int periodMultiplier, PeriodEnum period, DayTypeEnum dayType) {
    if(period != PeriodEnum.D) {
      throw new PricerException("Unhandled period " + period + ", expected D");
    }
    switch(dayType) {
      case BUSINESS:
      case CALENDAR:
      default:
        throw new PricerException("Unhandled dayType " + dayType + ", expected BUSINESS or CALENDAR");
    }
  }
}
