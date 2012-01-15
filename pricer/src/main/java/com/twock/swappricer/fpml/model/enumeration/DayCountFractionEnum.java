package com.twock.swappricer.fpml.model.enumeration;

/**
 * From <a href="http://www.fpml.org/coding-scheme/day-count-fraction-2-2.xml">http://www.fpml.org/coding-scheme/day-count-fraction-2-2.xml</a>.
 *
 * @author Chris Pearson (chris@twock.com)
 */
public enum DayCountFractionEnum {
  /**
   * Per 2006 ISDA Definitions, Section 4.16. Day Count Fraction, paragraph (a) or Annex to the 2000 ISDA Definitions
   * (June 2000 Version), Section 4.16. Day Count Fraction, paragraph (a).
   * <p/>
   * if “1/1” is specified, 1;
   */
  SINGLE("1/1"),

  /**
   * Per 2006 ISDA Definitions, Section 4.16. Day Count Fraction, paragraph (b) or Annex to the 2000 ISDA Definitions
   * (June 2000 Version), Section 4.16. Day Count Fraction, paragraph (b). Note that going from FpML 2.0 Recommendation
   * to the FpML 3.0 Trial Recommendation the code in FpML 2.0 'ACT/365.ISDA' became 'ACT/ACT.ISDA'.
   * <p/>
   * if “Actual/Actual”, “Actual/Actual (ISDA)”, “Act/Act” or “Act/Act (ISDA)” is specified,
   * the actual number of days in the Calculation Period or Compounding Period in respect of which payment
   * is being made divided by 365 (or, if any portion of that Calculation Period or Compounding Period falls
   * in a leap year, the sum of (i) the actual number of days in that portion of the Calculation Period or
   * Compounding Period falling in a leap year divided by 366 and (ii) the actual number of days in that
   * portion of the Calculation Period or Compounding Period falling in a non-leap year divided by 365);
   */
  ACT_ACT_ISDA("ACT/ACT.ISDA"),

  /**
   * Per 2006 ISDA Definitions, Section 4.16. Day Count Fraction, paragraph (c). This day count fraction code is
   * applicable for transactions booked under the 2006 ISDA Definitions. Transactions under the 2000 ISDA Definitions
   * should use the ACT/ACT.ISMA code instead.
   * <p/>
   * if “Actual/Actual (ICMA)” or “Act/Act (ICMA)” is specified, a fraction equal to
   * “number of days accrued/number of days in year”, as such terms are used in Rule 251 of the statutes, bylaws,
   * rules and recommendations of the International Capital Market Association (the “ICMA Rule
   * Book”), calculated in accordance with Rule 251 of the ICMA Rule Book as applied to non US dollar
   * denominated straight and convertible bonds issued after December 31, 1998, as though the interest
   * coupon on a bond were being calculated for a coupon period corresponding to the Calculation Period or
   * Compounding Period in respect of which payment is being made;
   */
  ACT_ACT_ICMA("ACT/ACT.ICMA"),

  /**
   * The Fixed/Floating Amount will be calculated in accordance with Rule 251 of the statutes, by-laws, rules and
   * recommendations of the International Securities Market Association, as published in April 1999, as applied to
   * straight and convertible bonds issued after December 31, 1998, as though the Fixed/Floating Amount were the
   * interest coupon on such a bond. This day count fraction code is applicable for transactions booked under the 2000
   * ISDA Definitions. Transactions under the 2006 ISDA Definitions should use the ACT/ACT.ICMA code instead.
   */
  ACT_ACT_ISMA("ACT/ACT.ISMA"),

  /**
   * The Fixed/Floating Amount will be calculated in accordance with the "BASE EXACT/EXACT" day count fraction, as
   * defined in the "Definitions Communes plusieurs Additifs Techniques" published by the Association Francaise des
   * Banques in September 1994
   */
//  ACT_ACT_AFB("ACT/ACT.AFB"),

  /**
   * Per 2006 ISDA Definitions, Section 4.16. Day Count Fraction, paragraph (d) or Annex to the 2000 ISDA Definitions
   * (June 2000 Version), Section 4.16. Day Count Fraction, paragraph (c).
   * <p/>
   * if “Actual/365 (Fixed)”, “Act/365 (Fixed)”, “A/365 (Fixed)” or “A/365F” is specified,
   * the actual number of days in the Calculation Period or Compounding Period in respect of which payment
   * is being made divided by 365;
   */
  ACT_365_FIXED("ACT/365.FIXED"),

  /**
   * Per 2006 ISDA Definitions, Section 4.16. Day Count Fraction, paragraph (e) or Annex to the 2000 ISDA Definitions
   * (June 2000 Version), Section 4.16. Day Count Fraction, paragraph (d).
   * <p/>
   * if “Actual/360”, “Act/360” or “A/360” is specified, the actual number of days in the
   * Calculation Period or Compounding Period in respect of which payment is being made divided by 360;
   */
  ACT_360("ACT/360"),

  /**
   * Per 2006 ISDA Definitions, Section 4.16. Day Count Fraction, paragraph (f) or Annex to the 2000 ISDA Definitions
   * (June 2000 Version), Section 4.16. Day Count Fraction, paragraph (e).
   * <p/>
   * if “30/360”, “360/360” or “Bond Basis” is specified, the number of days in the
   * Calculation Period or Compounding Period in respect of which payment is being made divided by 360,
   * calculated on a formula basis as follows:<br/>
   * Day Count Fraction = ([360 * (Y2 - Y1)] + [30 * (M2 - M1)] + (D2 - D1)) / 360<br/>
   * where:<br/>
   * “Y1” is the year, expressed as a number, in which the first day of the Calculation Period
   * or Compounding Period falls;<br/>
   * “Y2” is the year, expressed as a number, in which the day immediately following the last
   * day included in the Calculation Period or Compounding Period falls;<br/>
   * “M1” is the calendar month, expressed as a number, in which the first day of the
   * Calculation Period or Compounding Period falls;<br/>
   * “M2” is the calendar month, expressed as number, in which the day immediately
   * following the last day included in the Calculation Period or Compounding Period falls;<br/>
   * “D1” is the first calendar day, expressed as a number, of the Calculation Period or
   * Compounding Period, unless such number would be 31, in which case D1 will be 30; and<br/>
   * “D2” is the calendar day, expressed as a number, immediately following the last day
   * included in the Calculation Period or Compounding Period, unless such number would be 31 and<br/>
   * D1 is greater than 29, in which case D2 will be 30;
   */
  THIRTY_360("30/360"),

  /**
   * Per 2006 ISDA Definitions, Section 4.16. Day Count Fraction, paragraph (g) or Annex to the 2000 ISDA Definitions
   * (June 2000 Version), Section 4.16. Day Count Fraction, paragraph (f). Note that the algorithm defined for this day
   * count fraction has changed between the 2000 ISDA Definitions and 2006 ISDA Definitions. See Introduction to the
   * 2006 ISDA Definitions for further information relating to this change.
   * <p/>
   * if “30E/360” or “Eurobond Basis” is specified, the number of days in the Calculation
   * Period or Compounding Period in respect of which payment is being made divided by 360, calculated on
   * a formula basis as follows:<br/>
   * Day Count Fraction = ([360 * (Y2 - Y1)] + [30 * (M2 - M1)] + (D2 - D1)) / 360<br/>
   * where:<br/>
   * “Y1” is the year, expressed as a number, in which the first day of the Calculation Period
   * or Compounding Period falls;<br/>
   * “Y2” is the year, expressed as a number, in which the day immediately following the last
   * day included in the Calculation Period or Compounding Period falls;<br/>
   * “M1” is the calendar month, expressed as a number, in which the first day of the
   * Calculation Period or Compounding Period falls;<br/>
   * “M2” is the calendar month, expressed as a number, in which the day immediately
   * following the last day included in the Calculation Period or Compounding Period falls;<br/>
   * “D1” is the first calendar day, expressed as a number, of the Calculation Period or
   * Compounding Period, unless such number would be 31, in which case D1 will be 30; and<br/>
   * “D2” is the calendar day, expressed as a number, immediately following the last day
   * included in the Calculation Period or Compounding Period, unless such number would be 31, in
   * which case D2 will be 30.
   */
  THIRTY_E_360("30E/360"),

  /**
   * Per 2006 ISDA Definitions, Section 4.16. Day Count Fraction, paragraph (h). Note the algorithm for this day count
   * fraction under the 2006 ISDA Definitions is designed to yield the same results in practice as the version of the
   * 30E/360 day count fraction defined in the 2000 ISDA Definitions. See Introduction to the 2006 ISDA Definitions for
   * further information relating to this change.
   * <p/>
   * if “30E/360 (ISDA)” is specified, the number of days in the Calculation Period or
   * Compounding Period in respect of which payment is being made divided by 360, calculated on a formula
   * basis as follows:<br/>
   * Day Count Fraction = ([360 * (Y2 - Y1)] + [30 * (M2 - M1)] + (D2 - D1)) / 360<br/>
   * where:<br/>
   * “Y1” is the year, expressed as a number, in which the first day of the Calculation Period
   * or Compounding Period falls;<br/>
   * “Y2” is the year, expressed as a number, in which the day immediately following the last
   * day included in the Calculation Period or Compounding Period falls;<br/>
   * “M1” is the calendar month, expressed as a number, in which the first day of the
   * Calculation Period or Compounding Period falls;<br/>
   * “M2” is the calendar month, expressed as a number, in which the day immediately
   * following the last day included in the Calculation Period or Compounding Period falls;<br/>
   * “D1” is the first calendar day, expressed as a number, of the Calculation Period or
   * Compounding Period, unless (i) that day is the last day of February or (ii) such number would be
   * 31, in which case D1 will be 30; and<br/>
   * “D2” is the calendar day, expressed as a number, immediately following the last day
   * included in the Calculation Period or Compounding Period, unless (i) that day is the last day of
   * February but not the Termination Date or (ii) such number would be 31, in which case D2 will be
   * 30.
   */
  THIRTY_E_360_ISDA("30E/360.ISDA"),

  /**
   * The number of Business Days in the Calculation Period or Compounding Period in respect of which payment is being
   * made divided by 252.
   */
//  BUS_252("BUS/252"),

  /**
   * Per 2006 ISDA Definitions, Section 4.16. Day Count Fraction, paragraph (i).
   */
//  ACT_365L("ACT/365L");
  ;

  private final String value;

  DayCountFractionEnum(String v) {
    value = v;
  }

  public String value() {
    return value;
  }

  public static DayCountFractionEnum fromValue(String v) {
    for(DayCountFractionEnum c : DayCountFractionEnum.values()) {
      if(c.value.equals(v)) {
        return c;
      }
    }
    throw new IllegalArgumentException(v);
  }
}
