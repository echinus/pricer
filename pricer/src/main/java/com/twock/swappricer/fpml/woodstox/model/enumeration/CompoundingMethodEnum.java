package com.twock.swappricer.fpml.woodstox.model.enumeration;

/**
 * <p>Java class for CompoundingMethodEnum.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;simpleType name="CompoundingMethodEnum">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}token">
 *     &lt;enumeration value="Flat"/>
 *     &lt;enumeration value="None"/>
 *     &lt;enumeration value="Straight"/>
 *     &lt;enumeration value="SpreadExclusive"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 */
public enum CompoundingMethodEnum {

  /**
   * Flat compounding. Compounding excludes the spread. Note that the first compounding period has it's interest calculated including any spread then subsequent periods compound this at a rate excluding the spread.
   */
  FLAT("Flat"),

  /**
   * No compounding is to be applied.
   */
  NONE("None"),

  /**
   * Straight compounding. Compounding includes the spread.
   */
  STRAIGHT("Straight"),

  /**
   * Spread Exclusive compounding.
   */
  SPREAD_EXCLUSIVE("SpreadExclusive");
  private final String value;

  CompoundingMethodEnum(String v) {
    value = v;
  }

  public String value() {
    return value;
  }

  public static CompoundingMethodEnum fromValue(String v) {
    for(CompoundingMethodEnum c : CompoundingMethodEnum.values()) {
      if(c.value.equals(v)) {
        return c;
      }
    }
    throw new IllegalArgumentException(v);
  }

}
