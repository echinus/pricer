package com.twock.swappricer.test.fpml.woodstox;

import com.twock.swappricer.fpml.woodstox.FpmlParser;
import com.twock.swappricer.fpml.woodstox.factory.*;
import org.junit.Test;

/**
 * @author Chris Pearson (chris@twock.com)
 */
public class FpmlParserTest {
  @Test
  public void parseFpml() {
    BusinessDayAdjustmentsFactory businessDayAdjustmentsFactory = new BusinessDayAdjustmentsFactory();
    AdjustableDateFactory adjustableDateFactory = new AdjustableDateFactory(businessDayAdjustmentsFactory);
    CalculationPeriodFrequencyFactory calculationPeriodFrequencyFactory = new CalculationPeriodFrequencyFactory();
    SwapStreamFactory swapStreamFactory = new SwapStreamFactory(businessDayAdjustmentsFactory, adjustableDateFactory, calculationPeriodFrequencyFactory);
    FpmlParser fpmlParser = new FpmlParser(swapStreamFactory);

    fpmlParser.parse(getClass().getResourceAsStream("/LCH00000513426.xml"));
  }
}
