package com.twock.swappricer.fpml.woodstox.factory;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;

import com.twock.swappricer.PricerException;
import com.twock.swappricer.fpml.woodstox.FpmlParser;
import com.twock.swappricer.fpml.woodstox.model.AdjustableDate;
import com.twock.swappricer.fpml.woodstox.model.BusinessDayAdjustments;
import com.twock.swappricer.fpml.woodstox.model.DateWithDayCount;
import org.codehaus.stax2.XMLStreamReader2;

/**
 * @author Chris Pearson (chris@twock.com)
 */
public class AdjustableDateFactory {
  private final BusinessDayAdjustmentsFactory businessDayAdjustmentsFactory;

  public AdjustableDateFactory(BusinessDayAdjustmentsFactory businessDayAdjustmentsFactory) {
    this.businessDayAdjustmentsFactory = businessDayAdjustmentsFactory;
  }

  public AdjustableDate readAdjustableDate(XMLStreamReader2 streamReader) throws XMLStreamException {
    DateWithDayCount unadjustedDate = null;
    BusinessDayAdjustments businessDayAdjustments = null;

    int startingDepth = streamReader.getDepth();
    while(streamReader.hasNext()) {
      switch(streamReader.next()) {
        case XMLEvent.START_ELEMENT:
          String localName = streamReader.getLocalName();
          if("unadjustedDate".equals(localName)) {
            unadjustedDate = FpmlParser.readTextDate(streamReader);
          }
          if("dateAdjustments".equals(localName)) {
            businessDayAdjustments = businessDayAdjustmentsFactory.readBusinessDayAdjustments(streamReader);
          }
          break;
        case XMLEvent.END_ELEMENT:
          if(streamReader.getDepth() == startingDepth) {
            return new AdjustableDate(unadjustedDate, businessDayAdjustments);
          }
      }
    }
    throw new PricerException("No more events before element finished");
  }
}

