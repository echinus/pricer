package com.twock.swappricer.fpml.woodstox.model;

/**
 * <p>Java class for BusinessDayConventionEnum.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;simpleType name="BusinessDayConventionEnum">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}token">
 *     &lt;enumeration value="FOLLOWING"/>
 *     &lt;enumeration value="FRN"/>
 *     &lt;enumeration value="MODFOLLOWING"/>
 *     &lt;enumeration value="PRECEDING"/>
 *     &lt;enumeration value="MODPRECEDING"/>
 *     &lt;enumeration value="NEAREST"/>
 *     &lt;enumeration value="NONE"/>
 *     &lt;enumeration value="NotApplicable"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 */
public enum BusinessDayConventionEnum {

  /**
   * The non-business date will be adjusted to the first following day that is a business day
   */
  FOLLOWING("FOLLOWING"),

  /**
   * Per 2000 ISDA Definitions, Section 4.11. FRN Convention; Eurodollar Convention.
   */
  FRN_ADJUST("FRN"),

  /**
   * The non-business date will be adjusted to the first following day that is a business day unless that day falls in the next calendar month, in which case that date will be the first preceding day that is a business day.
   */
  MODFOLLOWING("MODFOLLOWING"),

  /**
   * The non-business day will be adjusted to the first preceding day that is a business day.
   */
  PRECEDING("PRECEDING"),

  /**
   * The non-business date will be adjusted to the first preceding day that is a business day unless that day falls in the previous calendar month, in which case that date will be the first following day that us a business day.
   */
  MODPRECEDING("MODPRECEDING"),

  /**
   * The non-business date will be adjusted to the nearest day that is a business day - i.e. if the non-business day falls on any day other than a Sunday or a Monday, it will be the first preceding day that is a business day, and will be the first following business day if it falls on a Sunday or a Monday.
   */
  NEAREST("NEAREST"),

  /**
   * The date will not be adjusted if it falls on a day that is not a business day.
   */
  NO_ADJUST("NONE"),

  /**
   * The date adjustments conventions are defined elsewhere, so it is not required to specify them here.
   */
  NOT_APPLICABLE("NotApplicable");
  private final String value;

  BusinessDayConventionEnum(String v) {
    value = v;
  }

  public String value() {
    return value;
  }

  public static BusinessDayConventionEnum fromValue(String v) {
    for(BusinessDayConventionEnum c : BusinessDayConventionEnum.values()) {
      if(c.value.equals(v)) {
        return c;
      }
    }
    throw new IllegalArgumentException(v);
  }

}
