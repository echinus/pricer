package com.twock.swappricer;

import java.io.Reader;
import java.util.*;

import au.com.bytecode.opencsv.CSVReader;
import com.twock.swappricer.fpml.model.DateWithDayCount;
import org.apache.commons.io.IOUtils;

/**
 * @author Chris Pearson (chris@twock.com)
 */
public class HolidayCalendarContainer {
  public Map<String, HolidayCalendar> holidayCalendars;

  public HolidayCalendarContainer() {
  }

  public HolidayCalendarContainer(HolidayCalendarContainer cals, String... codes) {
    if(cals.holidayCalendars != null && codes != null) {
      TreeMap<String, HolidayCalendar> calendars = new TreeMap<String, HolidayCalendar>();
      for(String code : codes) {
        HolidayCalendar calendar = cals.getHolidayCalendars().get(code);
        if(calendar != null) {
          calendars.put(code, calendar);
        }
      }
      holidayCalendars = calendars;
    }
  }

  public void loadFromTsv(Reader input) {
    Map<String, HolidayCalendar> result = new TreeMap<String, HolidayCalendar>();
    try {
      CSVReader reader = new CSVReader(input, '\t');
      reader.readNext(); // dump header row
      String currentCalendar = null;
      List<DateWithDayCount> currentDates = new ArrayList<DateWithDayCount>();
      String[] line;
      while((line = reader.readNext()) != null) {
        if(line.length >= 3) {
          String thisCalendar = line[1]; // e.g. GBLO
          DateWithDayCount thisDate = new DateWithDayCount(Short.parseShort(line[2].substring(6, 10)), Short.parseShort(line[2].substring(3, 5)), Short.parseShort(line[2].substring(0, 2)));
          if(currentCalendar != null && !currentCalendar.equals(thisCalendar)) {
            result.put(currentCalendar, new HolidayCalendar(currentCalendar, currentDates));
            currentDates.clear();
          }
          currentCalendar = thisCalendar;
          currentDates.add(thisDate);
        }
      }
    } catch(Exception e) {
      throw new PricerException("Failed to read in tab separated holiday calendar file", e);
    } finally {
      IOUtils.closeQuietly(input);
    }
    this.holidayCalendars = result;
  }

  public Map<String, HolidayCalendar> getHolidayCalendars() {
    return holidayCalendars;
  }

  public void setHolidayCalendars(Map<String, HolidayCalendar> holidayCalendars) {
    this.holidayCalendars = holidayCalendars;
  }

  public boolean isWeekendOrPublicHoliday(DateWithDayCount date) {
    if(date.isWeekend()) {
      return true;
    }
    if(holidayCalendars != null) {
      for(HolidayCalendar holidayCalendar : holidayCalendars.values()) {
        if(holidayCalendar.isPublicHoliday(date)) {
          return true;
        }
      }
    }
    return false;
  }

  public boolean isPublicHoliday(DateWithDayCount date) {
    for(HolidayCalendar holidayCalendar : holidayCalendars.values()) {
      if(holidayCalendar.isPublicHoliday(date)) {
        return true;
      }
    }
    return false;
  }
}
