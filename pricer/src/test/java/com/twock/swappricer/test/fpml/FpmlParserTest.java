package com.twock.swappricer.test.fpml;

import java.util.List;

import com.twock.swappricer.fpml.FpmlParser;
import com.twock.swappricer.fpml.factory.*;
import com.twock.swappricer.fpml.model.SwapStream;
import com.twock.swappricer.fpml.model.enumeration.BusinessDayConventionEnum;
import com.twock.swappricer.fpml.model.enumeration.PeriodEnum;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Chris Pearson (chris@twock.com)
 */
public class FpmlParserTest {
  private static final Logger log = Logger.getLogger(FpmlParserTest.class);
  private static List<SwapStream> streams;

  @BeforeClass
  public static void parseFpml() {
    FpmlParser fpmlParser = createFpmlParser();
    streams = fpmlParser.parse(FpmlParserTest.class.getResourceAsStream("/LCH00000513426.xml"));
    log.info("Stream 1: " + streams.get(0));
    log.info("Stream 2: " + streams.get(1));
  }

  public static FpmlParser createFpmlParser() {
    BusinessDayAdjustmentsFactory businessDayAdjustmentsFactory = new BusinessDayAdjustmentsFactory();
    AdjustableDateFactory adjustableDateFactory = new AdjustableDateFactory(businessDayAdjustmentsFactory);
    CalculationPeriodFrequencyFactory calculationPeriodFrequencyFactory = new CalculationPeriodFrequencyFactory();
    OffsetFactory offsetFactory = new OffsetFactory();
    RelativeDateOffsetFactory relativeDateOffsetFactory = new RelativeDateOffsetFactory(businessDayAdjustmentsFactory);
    PaymentDatesFactory paymentDatesFactory = new PaymentDatesFactory(businessDayAdjustmentsFactory, offsetFactory);
    ResetDatesFactory resetDatesFactory = new ResetDatesFactory(businessDayAdjustmentsFactory, relativeDateOffsetFactory);
    SwapStreamFactory swapStreamFactory = new SwapStreamFactory(businessDayAdjustmentsFactory, adjustableDateFactory, calculationPeriodFrequencyFactory, paymentDatesFactory, resetDatesFactory);
    return new FpmlParser(swapStreamFactory);
  }

  @Test
  public void paymentFrequency() {
    Assert.assertEquals(Integer.valueOf(1), streams.get(0).getPaymentDates().getPaymentFrequencyPeriodMultiplier());
    Assert.assertEquals(Integer.valueOf(1), streams.get(1).getPaymentDates().getPaymentFrequencyPeriodMultiplier());
    Assert.assertEquals(PeriodEnum.Y, streams.get(0).getPaymentDates().getPaymentFrequencyPeriod());
    Assert.assertEquals(PeriodEnum.T, streams.get(1).getPaymentDates().getPaymentFrequencyPeriod());
  }

  @Test
  public void paymentBusinessDayConvention() {
    Assert.assertEquals(BusinessDayConventionEnum.MODFOLLOWING, streams.get(0).getPaymentDates().getPaymentDatesAdjustments().getBusinessDayConvention());
    Assert.assertEquals(BusinessDayConventionEnum.MODFOLLOWING, streams.get(1).getPaymentDates().getPaymentDatesAdjustments().getBusinessDayConvention());
  }

  @Test
  public void paymentBusinessCenters() {
    Assert.assertArrayEquals(new String[]{"EUTA"}, streams.get(0).getPaymentDates().getPaymentDatesAdjustments().getBusinessCenters());
    Assert.assertArrayEquals(new String[]{"EUTA"}, streams.get(1).getPaymentDates().getPaymentDatesAdjustments().getBusinessCenters());
  }

  @Test
  public void fixingBusinessCenters() {
    Assert.assertArrayEquals(new String[]{"EUTA"}, streams.get(1).getResetDates().getFixingDates().getBusinessCenters());
  }
}
