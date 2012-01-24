package com.twock.swappricer.test;

import java.io.BufferedInputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import com.twock.swappricer.CurveContainer;
import com.twock.swappricer.DateUtil;
import com.twock.swappricer.MissingMappingException;
import com.twock.swappricer.fpml.model.enumeration.PeriodEnum;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author Chris Pearson (chris@twock.com)
 */
public class CurveContainerTest {
  private static CurveContainer container;

  public static synchronized CurveContainer getCurveContainer() {
    if(container == null) {
      Reader mappingsCsv = new InputStreamReader(new BufferedInputStream(CurveContainerTest.class.getResourceAsStream("/static/curveMappings.csv")));
      Reader curveTsv = new InputStreamReader(new BufferedInputStream(CurveContainerTest.class.getResourceAsStream("/DMPAUC_EUR00100a - VM Yield Curve - Zero Rates Day 1.TXT")));
      Reader historicRates = new InputStreamReader(new BufferedInputStream(CurveContainerTest.class.getResourceAsStream("/20111104_0210 - DMPAUC00003 - Historic Index Rates.TXT")));
      container = new CurveContainer(mappingsCsv, curveTsv, historicRates);
    }
    return container;
  }

  @Test
  public void indexAndTenor() {
    Assert.assertEquals("EUR_EONIA_EOD", getCurveContainer().getDiscountCurve("EUR-EURIBOR-Reuters", 1, PeriodEnum.M, "EUR"));
    Assert.assertEquals("EUR_EURIBOR_1M_EOD", getCurveContainer().getForwardCurve("EUR-EURIBOR-Reuters", 1, PeriodEnum.M, "EUR"));
    Assert.assertEquals("EUR_EONIA_EOD", getCurveContainer().getDiscountCurve("EUR-EURIBOR-Reuters", 3, PeriodEnum.M, "EUR"));
    Assert.assertEquals("EUR_EURIBOR_3M_EOD", getCurveContainer().getForwardCurve("EUR-EURIBOR-Reuters", 3, PeriodEnum.M, "EUR"));
  }

  @Test
  public void fallbackIndexAndTenor() {
    Assert.assertEquals("EUR_EONIA_EOD", getCurveContainer().getDiscountCurve("EUR-EURIBOR-Reuters", 1, PeriodEnum.Y, "EUR"));
    Assert.assertEquals("EUR_EURIBOR_EOD", getCurveContainer().getForwardCurve("EUR-EURIBOR-Reuters", 1, PeriodEnum.Y, "EUR"));
  }

  @Test
  public void ois() {
    Assert.assertEquals("EUR_EONIA_EOD", getCurveContainer().getDiscountCurve("EUR-EONIA-OIS-COMPOUND", null, null, "EUR"));
    Assert.assertEquals("EUR_EONIA_EOD", getCurveContainer().getForwardCurve("EUR-EONIA-OIS-COMPOUND", null, null, "EUR"));
  }

  @Test
  public void unknownIndexButMappedCurrency() {
    Assert.assertEquals("EUR_EONIA_EOD", getCurveContainer().getDiscountCurve("xxx", 1, PeriodEnum.Y, "EUR"));
    Assert.assertEquals("EUR_EURIBOR_EOD", getCurveContainer().getForwardCurve("xxx", 1, PeriodEnum.Y, "EUR"));
    Assert.assertEquals("EUR_EONIA_EOD", getCurveContainer().getDiscountCurve("xxx", 1, null, "EUR"));
    Assert.assertEquals("EUR_EURIBOR_EOD", getCurveContainer().getForwardCurve("xxx", 1, null, "EUR"));
  }

  @Test
  public void noIndexButMappedCurrency() {
    Assert.assertEquals("EUR_EONIA_EOD", getCurveContainer().getDiscountCurve(null, null, null, "EUR"));
    Assert.assertEquals("EUR_EURIBOR_EOD", getCurveContainer().getForwardCurve(null, null, null, "EUR"));
  }

  @Test(expected = MissingMappingException.class)
  public void unmappedCurrencyDiscount() {
    getCurveContainer().getDiscountCurve("xxx", 1, PeriodEnum.Y, "xxx");
  }

  @Test(expected = MissingMappingException.class)
  public void unmappedCurrencyForward() {
    getCurveContainer().getForwardCurve("xxx", 1, PeriodEnum.Y, "xxx");
  }

  @Test
  public void getLoadedDiscountFactor() {
    Assert.assertEquals(0.986086097648214, getCurveContainer().getCurve("EUR_EURIBOR_6M_EOD").getDiscountFactor(DateUtil.dateToDayCount(new short[]{2012, 10, 8})), 0.000000001);
  }

  @Test
  public void getLoadedHistoricIndexRate() {
    Assert.assertEquals(1.26900, getCurveContainer().getHistoricIndexRates("EUR-EURIBOR-Reuters", 6, PeriodEnum.M).get(DateUtil.dateToDayCount(new short[]{2010, 10, 29})), 0.00001);
  }
}
