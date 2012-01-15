package com.twock.swappricer.fpml.model.enumeration;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

/**
 * <a href="http://www.fpml.org/spec/fpml-5-2-5-rec-1/html/confirmation/schemaDocumentation/schemas/fpml-shared-5-2_xsd/complexTypes/CalculationPeriodFrequency/rollConvention.html">http://www.fpml.org/spec/fpml-5-2-5-rec-1/html/confirmation/schemaDocumentation/schemas/fpml-shared-5-2_xsd/complexTypes/CalculationPeriodFrequency/rollConvention.html</a>
 *
 * @author Chris Pearson (chris@twock.com)
 */
public enum RollConventionEnum {
  EOM("EOM"),
  //  FRN("FRN"),
  IMM("IMM"),
  //  IMMCAD("IMMCAD"),
//  IMMAUD("IMMAUD"),
//  IMMNZD("IMMNZD"),
//  SFE("SFE"),
  NONE("NONE"),
  //  TBILL("TBILL"),
  DAY1("1"),
  DAY2("2"),
  DAY3("3"),
  DAY4("4"),
  DAY5("5"),
  DAY6("6"),
  DAY7("7"),
  DAY8("8"),
  DAY9("9"),
  DAY10("10"),
  DAY11("11"),
  DAY12("12"),
  DAY13("13"),
  DAY14("14"),
  DAY15("15"),
  DAY16("16"),
  DAY17("17"),
  DAY18("18"),
  DAY19("19"),
  DAY20("20"),
  DAY21("21"),
  DAY22("22"),
  DAY23("23"),
  DAY24("24"),
  DAY25("25"),
  DAY26("26"),
  DAY27("27"),
  DAY28("28"),
  DAY29("29"),
  DAY30("30"),
//  MON("MON"),
//  TUE("TUE"),
//  WED("WED"),
//  THU("THU"),
//  FRI("FRI"),
//  SAT("SAT"),
//  SUN("SUN");
  ;
  private final String value;
  public static final Set<RollConventionEnum> DAY_ROLLS = Collections.unmodifiableSet(EnumSet.range(DAY1, DAY30));

  RollConventionEnum(String v) {
    value = v;
  }

  public String value() {
    return value;
  }

  public static RollConventionEnum fromValue(String v) {
    for(RollConventionEnum c : RollConventionEnum.values()) {
      if(c.value.equals(v)) {
        return c;
      }
    }
    throw new IllegalArgumentException(v);
  }
}
