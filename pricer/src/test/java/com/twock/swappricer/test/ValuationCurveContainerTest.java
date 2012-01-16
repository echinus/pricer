package com.twock.swappricer.test;

import java.io.BufferedInputStream;
import java.io.InputStreamReader;

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

  @BeforeClass
  public static void loadCurves() {
    container = new ValuationCurveContainer(new InputStreamReader(new BufferedInputStream(ValuationCurveContainerTest.class.getResourceAsStream("/static/valuationCurves.csv"))));
  }

  @Test
  public void indexAndTenor() {
    Assert.assertEquals("EUR_EONIA_EOD", container.getDiscountCurve("EUR-EURIBOR-Reuters", 1, PeriodEnum.M, "EUR"));
    Assert.assertEquals("EUR_EURIBOR_1M_EOD", container.getForwardCurve("EUR-EURIBOR-Reuters", 1, PeriodEnum.M, "EUR"));
    Assert.assertEquals("EUR_EONIA_EOD", container.getDiscountCurve("EUR-EURIBOR-Reuters", 3, PeriodEnum.M, "EUR"));
    Assert.assertEquals("EUR_EURIBOR_3M_EOD", container.getForwardCurve("EUR-EURIBOR-Reuters", 3, PeriodEnum.M, "EUR"));
  }

  @Test
  public void fallbackIndexAndTenor() {
    Assert.assertEquals("EUR_EONIA_EOD", container.getDiscountCurve("EUR-EURIBOR-Reuters", 1, PeriodEnum.Y, "EUR"));
    Assert.assertEquals("EUR_EURIBOR_EOD", container.getForwardCurve("EUR-EURIBOR-Reuters", 1, PeriodEnum.Y, "EUR"));
  }

  @Test
  public void ois() {
    Assert.assertEquals("EUR_EONIA_EOD", container.getDiscountCurve("EUR-EONIA-OIS-COMPOUND", null, null, "EUR"));
    Assert.assertEquals("EUR_EONIA_EOD", container.getForwardCurve("EUR-EONIA-OIS-COMPOUND", null, null, "EUR"));
  }

  @Test
  public void unknownIndexButMappedCurrency() {
    Assert.assertEquals("EUR_EONIA_EOD", container.getDiscountCurve("xxx", 1, PeriodEnum.Y, "EUR"));
    Assert.assertEquals("EUR_EURIBOR_EOD", container.getForwardCurve("xxx", 1, PeriodEnum.Y, "EUR"));
    Assert.assertEquals("EUR_EONIA_EOD", container.getDiscountCurve("xxx", 1, null, "EUR"));
    Assert.assertEquals("EUR_EURIBOR_EOD", container.getForwardCurve("xxx", 1, null, "EUR"));
  }

  @Test(expected = MissingMappingException.class)
  public void unmappedCurrencyDiscount() {
    container.getDiscountCurve("xxx", 1, PeriodEnum.Y, "xxx");
  }

  @Test(expected = MissingMappingException.class)
  public void unmappedCurrencyForward() {
    container.getForwardCurve("xxx", 1, PeriodEnum.Y, "xxx");
  }
}
