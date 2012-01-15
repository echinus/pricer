package com.twock.swappricer.fpml.factory;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;

import com.twock.swappricer.PricerException;
import com.twock.swappricer.fpml.FpmlParser;
import com.twock.swappricer.fpml.model.*;
import com.twock.swappricer.fpml.model.enumeration.CompoundingMethodEnum;
import com.twock.swappricer.fpml.model.enumeration.DayCountFractionEnum;
import com.twock.swappricer.fpml.model.enumeration.PeriodEnum;
import org.codehaus.stax2.XMLStreamReader2;

/**
 * @author Chris Pearson (chris@twock.com)
 */
public class SwapStreamFactory {
  private final BusinessDayAdjustmentsFactory businessDayAdjustmentsFactory;
  private final AdjustableDateFactory adjustableDateFactory;
  private final CalculationPeriodFrequencyFactory calculationPeriodFrequencyFactory;
  private final PaymentDatesFactory paymentDatesFactory;
  private final ResetDatesFactory resetDatesFactory;

  public SwapStreamFactory(BusinessDayAdjustmentsFactory businessDayAdjustmentsFactory, AdjustableDateFactory adjustableDateFactory, CalculationPeriodFrequencyFactory calculationPeriodFrequencyFactory, PaymentDatesFactory paymentDatesFactory, ResetDatesFactory resetDatesFactory) {
    this.businessDayAdjustmentsFactory = businessDayAdjustmentsFactory;
    this.adjustableDateFactory = adjustableDateFactory;
    this.calculationPeriodFrequencyFactory = calculationPeriodFrequencyFactory;
    this.paymentDatesFactory = paymentDatesFactory;
    this.resetDatesFactory = resetDatesFactory;
  }

  public SwapStream readSwapStream(XMLStreamReader2 streamReader) throws XMLStreamException {
    AdjustableDate effectiveDate = null;
    AdjustableDate terminationDate = null;
    BusinessDayAdjustments calculationPeriodDatesAdjustments = null;
    CalculationPeriodFrequency calculationPeriodFrequency = null;
    DateWithDayCount firstRegularPeriodStartDate = null;
    DateWithDayCount lastRegularPeriodEndDate = null;
    PaymentDates paymentDates = null;
    ResetDates resetDates = null;
    Double notionalAmount = null;
    String notionalCurrency = null;
    DayCountFractionEnum dayCountFraction = null;
    CompoundingMethodEnum compoundingMethod = null;
    Double fixedRate = null;
    String floatingRateIndex = null;
    Integer indexTenorPeriodMultiplier = null;
    PeriodEnum indexTenorPeriod = null;
    Double spread = null;
    Double initialRate = null;

    int startingDepth = streamReader.getDepth();
    while(streamReader.hasNext()) {
      switch(streamReader.next()) {
        case XMLEvent.START_ELEMENT:
          String localName = streamReader.getLocalName();
          if("effectiveDate".equals(localName)) {
            effectiveDate = adjustableDateFactory.readAdjustableDate(streamReader);
          } else if("terminationDate".equals(localName)) {
            terminationDate = adjustableDateFactory.readAdjustableDate(streamReader);
          } else if("calculationPeriodDatesAdjustments".equals(localName)) {
            calculationPeriodDatesAdjustments = businessDayAdjustmentsFactory.readBusinessDayAdjustments(streamReader);
          } else if("calculationPeriodFrequency".equals(localName)) {
            calculationPeriodFrequency = calculationPeriodFrequencyFactory.readCalculationPeriodFrequency(streamReader);
          } else if("firstRegularPeriodStartDate".equals(localName)) {
            firstRegularPeriodStartDate = FpmlParser.readTextDate(streamReader);
          } else if("lastRegularPeriodEndDate".equals(localName)) {
            lastRegularPeriodEndDate = FpmlParser.readTextDate(streamReader);
          } else if("paymentDates".equals(localName)) {
            paymentDates = paymentDatesFactory.readPaymentDates(streamReader);
          } else if("resetDates".equals(localName)) {
            resetDates = resetDatesFactory.readResetDates(streamReader);
          } else if("notionalSchedule".equals(localName)) {
            Object[] notionalParts = readNotional(streamReader);
            notionalAmount = (Double)notionalParts[0];
            notionalCurrency = (String)notionalParts[1];
          } else if("dayCountFraction".equals(localName)) {
            dayCountFraction = DayCountFractionEnum.fromValue(FpmlParser.readText(streamReader));
          } else if("compoundingMethod".equals(localName)) {
            compoundingMethod = CompoundingMethodEnum.fromValue(FpmlParser.readText(streamReader));
          } else if("fixedRateSchedule".equals(localName)) {
            fixedRate = readInitialValue(streamReader);
          } else if("floatingRateIndex".equals(localName)) {
            floatingRateIndex = FpmlParser.readText(streamReader);
          } else if("indexTenor".equals(localName)) {
            Object[] indexTenorParts = readIndexTenor(streamReader);
            indexTenorPeriodMultiplier = (Integer)indexTenorParts[0];
            indexTenorPeriod = (PeriodEnum)indexTenorParts[1];
          } else if("spreadSchedule".equals(localName)) {
            spread = readInitialValue(streamReader);
          } else if("initialRate".equals(localName)) {
            initialRate = Double.valueOf(FpmlParser.readText(streamReader));
          }
        case XMLEvent.END_ELEMENT:
          if(streamReader.getDepth() == startingDepth) {
            return new SwapStream(effectiveDate, terminationDate, calculationPeriodDatesAdjustments, calculationPeriodFrequency, firstRegularPeriodStartDate, lastRegularPeriodEndDate, paymentDates, resetDates, notionalAmount, notionalCurrency, dayCountFraction, compoundingMethod, fixedRate, floatingRateIndex, indexTenorPeriodMultiplier, indexTenorPeriod, spread, initialRate);
          }
      }
    }
    throw new PricerException("No more events before element finished");

  }

  private Object[] readIndexTenor(XMLStreamReader2 streamReader) throws XMLStreamException {
    Integer periodMultiplier = null;
    PeriodEnum period = null;
    int startingDepth = streamReader.getDepth();
    while(streamReader.hasNext()) {
      switch(streamReader.next()) {
        case XMLEvent.START_ELEMENT:
          if(streamReader.getDepth() == startingDepth + 1) {
            String localName = streamReader.getLocalName();
            if("periodMultiplier".equals(localName)) {
              periodMultiplier = Integer.parseInt(FpmlParser.readText(streamReader));
            } else if("period".equals(localName)) {
              period = PeriodEnum.valueOf(FpmlParser.readText(streamReader));
            }
          }
          break;
        case XMLEvent.END_ELEMENT:
          if(streamReader.getDepth() == startingDepth) {
            return new Object[]{periodMultiplier, period};
          }
      }
    }
    throw new PricerException("No more events before element finished");

  }

  private Object[] readNotional(XMLStreamReader2 streamReader) throws XMLStreamException {
    Double notionalAmount = null;
    String notionalCurrency = null;
    int startingDepth = streamReader.getDepth();
    while(streamReader.hasNext()) {
      switch(streamReader.next()) {
        case XMLEvent.START_ELEMENT:
          String localName = streamReader.getLocalName();
          if("initialValue".equals(localName)) {
            notionalAmount = Double.valueOf(FpmlParser.readText(streamReader));
          } else if("currency".equals(localName)) {
            notionalCurrency = FpmlParser.readText(streamReader);
          }
        case XMLEvent.END_ELEMENT:
          if(streamReader.getDepth() == startingDepth) {
            return new Object[]{notionalAmount, notionalCurrency};
          }
      }
    }
    throw new PricerException("No more events before element finished");
  }

  private Double readInitialValue(XMLStreamReader2 streamReader) throws XMLStreamException {
    Double initialValue = null;
    int startingDepth = streamReader.getDepth();
    while(streamReader.hasNext()) {
      switch(streamReader.next()) {
        case XMLEvent.START_ELEMENT:
          String localName = streamReader.getLocalName();
          if("initialValue".equals(localName)) {
            initialValue = Double.valueOf(FpmlParser.readText(streamReader));
          }
        case XMLEvent.END_ELEMENT:
          if(streamReader.getDepth() == startingDepth) {
            return initialValue;
          }
      }
    }
    throw new PricerException("No more events before element finished");
  }
}
