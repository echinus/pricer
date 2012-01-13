package com.twock.swappricer.fpml.woodstox.model.enumeration;

/**
 * <p>Java class for PayRelativeToEnum.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;simpleType name="PayRelativeToEnum">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}token">
 *     &lt;enumeration value="CalculationPeriodStartDate"/>
 *     &lt;enumeration value="CalculationPeriodEndDate"/>
 *     &lt;enumeration value="LastPricingDate"/>
 *     &lt;enumeration value="ResetDate"/>
 *     &lt;enumeration value="ValuationDate"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 */
public enum PayRelativeToEnum {

  /**
   * Payments will occur relative to the first day of each calculation period.
   */
  CALCULATION_PERIOD_START_DATE("CalculationPeriodStartDate"),

  /**
   * Payments will occur relative to the last day of each calculation period.
   */
  CALCULATION_PERIOD_END_DATE("CalculationPeriodEndDate"),

  /**
   * Payments will occur relative to the last Pricing Date of each Calculation Period.
   */
  LAST_PRICING_DATE("LastPricingDate"),

  /**
   * Payments will occur relative to the reset date.
   */
  RESET_DATE("ResetDate"),

  /**
   * Payments will occur relative to the valuation date.
   */
  VALUATION_DATE("ValuationDate");
  private final String value;

  PayRelativeToEnum(String v) {
    value = v;
  }

  public String value() {
    return value;
  }

  public static PayRelativeToEnum fromValue(String v) {
    for(PayRelativeToEnum c : PayRelativeToEnum.values()) {
      if(c.value.equals(v)) {
        return c;
      }
    }
    throw new IllegalArgumentException(v);
  }
}
