package com.twock.swappricer.fpml.woodstox.factory;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;

import com.twock.swappricer.PricerException;
import com.twock.swappricer.fpml.woodstox.FpmlParser;
import com.twock.swappricer.fpml.woodstox.model.BusinessDayAdjustments;
import com.twock.swappricer.fpml.woodstox.model.Offset;
import com.twock.swappricer.fpml.woodstox.model.PaymentDates;
import com.twock.swappricer.fpml.woodstox.model.enumeration.PayRelativeToEnum;
import com.twock.swappricer.fpml.woodstox.model.enumeration.PeriodEnum;
import org.codehaus.stax2.XMLStreamReader2;

/**
 * @author Chris Pearson (chris@twock.com)
 */
public class PaymentDatesFactory {
  private final BusinessDayAdjustmentsFactory businessDayAdjustmentsFactory;
  private final OffsetFactory offsetFactory;

  public PaymentDatesFactory(BusinessDayAdjustmentsFactory businessDayAdjustmentsFactory, OffsetFactory offsetFactory) {
    this.businessDayAdjustmentsFactory = businessDayAdjustmentsFactory;
    this.offsetFactory = offsetFactory;
  }

  public PaymentDates readPaymentDates(XMLStreamReader2 streamReader) throws XMLStreamException {
    Integer paymentFrequencyPeriodMultiplier = null;
    PeriodEnum paymentFrequencyPeriod = null;
    PayRelativeToEnum payRelativeTo = null;
    Offset paymentDaysOffset = null;
    BusinessDayAdjustments paymentDatesAdjustments = null;
    int startingDepth = streamReader.getDepth();
    while(streamReader.hasNext()) {
      switch(streamReader.next()) {
        case XMLEvent.START_ELEMENT:
          String localName = streamReader.getLocalName();
          if("periodMultiplier".equals(localName)) {
            paymentFrequencyPeriodMultiplier = Integer.parseInt(FpmlParser.readText(streamReader));
          } else if("period".equals(localName)) {
            paymentFrequencyPeriod = PeriodEnum.valueOf(FpmlParser.readText(streamReader));
          } else if("payRelativeTo".equals(localName)) {
            payRelativeTo = PayRelativeToEnum.fromValue(FpmlParser.readText(streamReader));
          } else if("paymentDaysOffset".equals(localName)) {
            paymentDaysOffset = offsetFactory.readOffset(streamReader);
          } else if("paymentDatesAdjustments".equals(localName)) {
            paymentDatesAdjustments = businessDayAdjustmentsFactory.readBusinessDayAdjustments(streamReader);
          }
          break;
        case XMLEvent.END_ELEMENT:
          if(streamReader.getDepth() == startingDepth) {
            return new PaymentDates(paymentFrequencyPeriodMultiplier, paymentFrequencyPeriod, payRelativeTo, paymentDaysOffset, paymentDatesAdjustments);
          }
      }
    }
    throw new PricerException("No more events before element finished");
  }
}
