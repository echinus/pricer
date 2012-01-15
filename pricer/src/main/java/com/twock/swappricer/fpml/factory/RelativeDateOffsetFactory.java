package com.twock.swappricer.fpml.factory;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;

import com.twock.swappricer.PricerException;
import com.twock.swappricer.fpml.FpmlParser;
import com.twock.swappricer.fpml.model.BusinessDayAdjustments;
import com.twock.swappricer.fpml.model.RelativeDateOffset;
import com.twock.swappricer.fpml.model.enumeration.DayTypeEnum;
import com.twock.swappricer.fpml.model.enumeration.PeriodEnum;
import org.codehaus.stax2.XMLStreamReader2;

/**
 * @author Chris Pearson (chris@twock.com)
 */
public class RelativeDateOffsetFactory {
  private final BusinessDayAdjustmentsFactory businessDayAdjustmentsFactory;

  public RelativeDateOffsetFactory(BusinessDayAdjustmentsFactory businessDayAdjustmentsFactory) {
    this.businessDayAdjustmentsFactory = businessDayAdjustmentsFactory;
  }

  public RelativeDateOffset readOffset(XMLStreamReader2 streamReader) throws XMLStreamException {
    Integer periodMultiplier = null;
    PeriodEnum period = null;
    DayTypeEnum dayType = null;
    BusinessDayAdjustments businessDayAdjustments = null;
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
            } else if("dayType".equals(localName)) {
              dayType = DayTypeEnum.fromValue(FpmlParser.readText(streamReader));
            } else if("businessDayAdjustments".equals(localName)) {
              businessDayAdjustments = businessDayAdjustmentsFactory.readBusinessDayAdjustments(streamReader);
            }
          }
          break;
        case XMLEvent.END_ELEMENT:
          if(streamReader.getDepth() == startingDepth) {
            return new RelativeDateOffset(periodMultiplier, period, dayType, businessDayAdjustments);
          }
      }
    }
    throw new PricerException("No more events before element finished");
  }
}
