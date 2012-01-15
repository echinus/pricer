package com.twock.swappricer.fpml.model.enumeration;

/**
 * <p>Java class for DayTypeEnum.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;simpleType name="DayTypeEnum">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}token">
 *     &lt;enumeration value="Business"/>
 *     &lt;enumeration value="Calendar"/>
 *     &lt;enumeration value="CommodityBusiness"/>
 *     &lt;enumeration value="CurrencyBusiness"/>
 *     &lt;enumeration value="ExchangeBusiness"/>
 *     &lt;enumeration value="ScheduledTradingDay"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 */
public enum DayTypeEnum {

  /**
   * When calculating the number of days between two dates the count includes only business days.
   */
  BUSINESS("Business"),

  /**
   * When calculating the number of days between two dates the count includes all calendar days.
   */
  CALENDAR("Calendar"),

  /**
   * When calculating the number of days between two dates the count includes only commodity business days.
   */
//  COMMODITY_BUSINESS("CommodityBusiness"),

  /**
   * When calculating the number of days between two dates the count includes only currency business days.
   */
//  CURRENCY_BUSINESS("CurrencyBusiness"),

  /**
   * When calculating the number of days between two dates the count includes only stock exchange business days.
   */
//  EXCHANGE_BUSINESS("ExchangeBusiness"),

  /**
   * When calculating the number of days between two dates the count includes only scheduled trading days.
   */
//  SCHEDULED_TRADING_DAY("ScheduledTradingDay")
  ;
  private final String value;

  DayTypeEnum(String v) {
    value = v;
  }

  public String value() {
    return value;
  }

  public static DayTypeEnum fromValue(String v) {
    for(DayTypeEnum c : DayTypeEnum.values()) {
      if(c.value.equals(v)) {
        return c;
      }
    }
    throw new IllegalArgumentException(v);
  }

}
