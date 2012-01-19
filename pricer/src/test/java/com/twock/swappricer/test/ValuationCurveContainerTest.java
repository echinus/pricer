package com.twock.swappricer.test;

import java.io.BufferedInputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import com.twock.swappricer.MissingMappingException;
import com.twock.swappricer.ValuationCurveContainer;
import com.twock.swappricer.fpml.model.enumeration.PeriodEnum;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Chris Pearson (chris@twock.com)
 */
public class ValuationCurveContainerTest {
  private static ValuationCurveContainer container;
  
  public static synchronized ValuationCurveContainer getValuationCurveContainer() {
    if (container == null) {
      Reader mappingsCsv = new InputStreamReader(new BufferedInputStream(ValuationCurveContainerTest.class.getResourceAsStream("/static/valuationCurves.csv")));
      Reader curveTsv = new InputStreamReader(new BufferedInputStream(ValuationCurveContainerTest.class.getResourceAsStream("/DMPAUC_EUR00100a - VM Yield Curve - Zero Rates Day 1.TXT")));
      container = new ValuationCurveContainer(mappingsCsv, curveTsv);
    }
    return container;
  }

  @Test
  public void indexAndTenor() {
    Assert.assertEquals("EUR_EONIA_EOD",  getValuationCurveContainer().getDiscountCurve("EUR-EURIBOR-Reuters", 1, PeriodEnum.M, "EUR"));
    Assert.assertEquals("EUR_EURIBOR_1M_EOD",  getValuationCurveContainer().getForwardCurve("EUR-EURIBOR-Reuters", 1, PeriodEnum.M, "EUR"));
    Assert.assertEquals("EUR_EONIA_EOD",  getValuationCurveContainer().getDiscountCurve("EUR-EURIBOR-Reuters", 3, PeriodEnum.M, "EUR"));
    Assert.assertEquals("EUR_EURIBOR_3M_EOD",  getValuationCurveContainer().getForwardCurve("EUR-EURIBOR-Reuters", 3, PeriodEnum.M, "EUR"));
  }

  @Test
  public void fallbackIndexAndTenor() {
    Assert.assertEquals("EUR_EONIA_EOD",  getValuationCurveContainer().getDiscountCurve("EUR-EURIBOR-Reuters", 1, PeriodEnum.Y, "EUR"));
    Assert.assertEquals("EUR_EURIBOR_EOD",  getValuationCurveContainer().getForwardCurve("EUR-EURIBOR-Reuters", 1, PeriodEnum.Y, "EUR"));
  }

  @Test
  public void ois() {
    Assert.assertEquals("EUR_EONIA_EOD",  getValuationCurveContainer().getDiscountCurve("EUR-EONIA-OIS-COMPOUND", null, null, "EUR"));
    Assert.assertEquals("EUR_EONIA_EOD",  getValuationCurveContainer().getForwardCurve("EUR-EONIA-OIS-COMPOUND", null, null, "EUR"));
  }

  @Test
  public void unknownIndexButMappedCurrency() {
    Assert.assertEquals("EUR_EONIA_EOD",  getValuationCurveContainer().getDiscountCurve("xxx", 1, PeriodEnum.Y, "EUR"));
    Assert.assertEquals("EUR_EURIBOR_EOD",  getValuationCurveContainer().getForwardCurve("xxx", 1, PeriodEnum.Y, "EUR"));
    Assert.assertEquals("EUR_EONIA_EOD",  getValuationCurveContainer().getDiscountCurve("xxx", 1, null, "EUR"));
    Assert.assertEquals("EUR_EURIBOR_EOD",  getValuationCurveContainer().getForwardCurve("xxx", 1, null, "EUR"));
  }

  @Test
  public void noIndexButMappedCurrency() {
    Assert.assertEquals("EUR_EONIA_EOD",  getValuationCurveContainer().getDiscountCurve(null, null, null, "EUR"));
    Assert.assertEquals("EUR_EURIBOR_EOD",  getValuationCurveContainer().getForwardCurve(null, null, null, "EUR"));
  }

  @Test(expected = MissingMappingException.class)
  public void unmappedCurrencyDiscount() {
     getValuationCurveContainer().getDiscountCurve("xxx", 1, PeriodEnum.Y, "xxx");
  }

  @Test(expected = MissingMappingException.class)
  public void unmappedCurrencyForward() {
     getValuationCurveContainer().getForwardCurve("xxx", 1, PeriodEnum.Y, "xxx");
  }
}
