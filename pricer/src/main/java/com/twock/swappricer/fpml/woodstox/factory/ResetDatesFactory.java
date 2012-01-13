package com.twock.swappricer.fpml.woodstox.factory;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;

import com.twock.swappricer.PricerException;
import com.twock.swappricer.fpml.woodstox.FpmlParser;
import com.twock.swappricer.fpml.woodstox.model.BusinessDayAdjustments;
import com.twock.swappricer.fpml.woodstox.model.RelativeDateOffset;
import com.twock.swappricer.fpml.woodstox.model.ResetDates;
import com.twock.swappricer.fpml.woodstox.model.enumeration.PeriodEnum;
import com.twock.swappricer.fpml.woodstox.model.enumeration.ResetRelativeToEnum;
import org.codehaus.stax2.XMLStreamReader2;

/**
 * @author Chris Pearson (chris@twock.com)
 */
public class ResetDatesFactory {
  private final BusinessDayAdjustmentsFactory businessDayAdjustmentsFactory;
  private final RelativeDateOffsetFactory relativeDateOffsetFactory;

  public ResetDatesFactory(BusinessDayAdjustmentsFactory businessDayAdjustmentsFactory, RelativeDateOffsetFactory relativeDateOffsetFactory) {
    this.businessDayAdjustmentsFactory = businessDayAdjustmentsFactory;
    this.relativeDateOffsetFactory = relativeDateOffsetFactory;
  }

  public ResetDates readResetDates(XMLStreamReader2 streamReader) throws XMLStreamException {
    ResetRelativeToEnum resetRelativeTo = null;
    RelativeDateOffset initialFixingDate = null;
    RelativeDateOffset fixingDates = null;
    Integer frequencyPeriodMultiplier = null;
    PeriodEnum frequencyPeriod = null;
    BusinessDayAdjustments resetDatesAdjustments = null;
    int startingDepth = streamReader.getDepth();
    while(streamReader.hasNext()) {
      switch(streamReader.next()) {
        case XMLEvent.START_ELEMENT:
          String localName = streamReader.getLocalName();
          if("resetRelativeTo".equals(localName)) {
            resetRelativeTo = ResetRelativeToEnum.fromValue(FpmlParser.readText(streamReader));
          } else if("initialFixingDate".equals(localName)) {
            initialFixingDate = relativeDateOffsetFactory.readOffset(streamReader);
          } else if("fixingDates".equals(localName)) {
            fixingDates = relativeDateOffsetFactory.readOffset(streamReader);
          } else if("periodMultiplier".equals(localName)) {
            frequencyPeriodMultiplier = Integer.parseInt(FpmlParser.readText(streamReader));
          } else if("period".equals(localName)) {
            frequencyPeriod = PeriodEnum.valueOf(FpmlParser.readText(streamReader));
          } else if("resetDatesAdjustments".equals(localName)) {
            resetDatesAdjustments = businessDayAdjustmentsFactory.readBusinessDayAdjustments(streamReader);
          }
          break;
        case XMLEvent.END_ELEMENT:
          if(streamReader.getDepth() == startingDepth) {
            return new ResetDates(resetRelativeTo, initialFixingDate, fixingDates, frequencyPeriodMultiplier, frequencyPeriod, resetDatesAdjustments);
          }
      }
    }
    throw new PricerException("No more events before element finished");
  }
}
