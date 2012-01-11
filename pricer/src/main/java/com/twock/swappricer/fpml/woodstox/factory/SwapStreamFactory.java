package com.twock.swappricer.fpml.woodstox.factory;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;

import com.twock.swappricer.PricerException;
import com.twock.swappricer.fpml.woodstox.FpmlParser;
import com.twock.swappricer.fpml.woodstox.model.*;
import org.codehaus.stax2.XMLStreamReader2;

/**
 * @author Chris Pearson (chris@twock.com)
 */
public class SwapStreamFactory {
  private final BusinessDayAdjustmentsFactory businessDayAdjustmentsFactory;
  private final AdjustableDateFactory adjustableDateFactory;
  private final CalculationPeriodFrequencyFactory calculationPeriodFrequencyFactory;

  public SwapStreamFactory(BusinessDayAdjustmentsFactory businessDayAdjustmentsFactory, AdjustableDateFactory adjustableDateFactory, CalculationPeriodFrequencyFactory calculationPeriodFrequencyFactory) {
    this.businessDayAdjustmentsFactory = businessDayAdjustmentsFactory;
    this.adjustableDateFactory = adjustableDateFactory;
    this.calculationPeriodFrequencyFactory = calculationPeriodFrequencyFactory;
  }

  public SwapStream readSwapStream(XMLStreamReader2 streamReader) throws XMLStreamException {
    AdjustableDate effectiveDate = null;
    AdjustableDate terminationDate = null;
    BusinessDayAdjustments calculationPeriodDatesAdjustments = null;
    CalculationPeriodFrequency calculationPeriodFrequency = null;
    DateWithDayCount firstRegularPeriodStartDate = null;
    DateWithDayCount lastRegularPeriodEndDate = null;
    int startingDepth = streamReader.getDepth();
    while(streamReader.hasNext()) {
      switch(streamReader.next()) {
        case XMLEvent.START_ELEMENT:
          String localName = streamReader.getLocalName();
          if("effectiveDate".equals(localName)) {
            effectiveDate = adjustableDateFactory.readAdjustableDate(streamReader);
          }
          if("terminationDate".equals(localName)) {
            terminationDate = adjustableDateFactory.readAdjustableDate(streamReader);
          }
          if("calculationPeriodDatesAdjustments".equals(localName)) {
            calculationPeriodDatesAdjustments = businessDayAdjustmentsFactory.readBusinessDayAdjustments(streamReader);
          }
          if("calculationPeriodFrequency".equals(localName)) {
            calculationPeriodFrequency = calculationPeriodFrequencyFactory.readCalculationPeriodFrequency(streamReader);
          }
          if("firstRegularPeriodStartDate".equals(localName)) {
            firstRegularPeriodStartDate = FpmlParser.readTextDate(streamReader);
          }
          if("lastRegularPeriodEndDate".equals(localName)) {
            lastRegularPeriodEndDate = FpmlParser.readTextDate(streamReader);
          }
        case XMLEvent.END_ELEMENT:
          if(streamReader.getDepth() == startingDepth) {
            return new SwapStream(effectiveDate, terminationDate, calculationPeriodDatesAdjustments, calculationPeriodFrequency, firstRegularPeriodStartDate, lastRegularPeriodEndDate);
          }
      }
    }
    throw new PricerException("No more events before element finished");

  }
}
