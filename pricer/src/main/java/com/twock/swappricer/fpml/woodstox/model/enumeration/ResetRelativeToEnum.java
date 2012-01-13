package com.twock.swappricer.fpml.woodstox.model.enumeration;

/**
 * <p>Java class for ResetRelativeToEnum.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;simpleType name="ResetRelativeToEnum">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}token">
 *     &lt;enumeration value="CalculationPeriodStartDate"/>
 *     &lt;enumeration value="CalculationPeriodEndDate"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 */
public enum ResetRelativeToEnum {

  /**
   * Resets will occur relative to the first day of each calculation period.
   */
  CALCULATION_PERIOD_START_DATE("CalculationPeriodStartDate"),

  /**
   * Resets will occur relative to the last day of each calculation period.
   */
  CALCULATION_PERIOD_END_DATE("CalculationPeriodEndDate");
  private final String value;

  ResetRelativeToEnum(String v) {
    value = v;
  }

  public String value() {
    return value;
  }

  public static ResetRelativeToEnum fromValue(String v) {
    for(ResetRelativeToEnum c : ResetRelativeToEnum.values()) {
      if(c.value.equals(v)) {
        return c;
      }
    }
    throw new IllegalArgumentException(v);
  }
}
