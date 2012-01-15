package com.twock.swappricer.fpml.factory;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;

import com.twock.swappricer.PricerException;
import com.twock.swappricer.fpml.FpmlParser;
import com.twock.swappricer.fpml.model.CalculationPeriodFrequency;
import com.twock.swappricer.fpml.model.enumeration.PeriodEnum;
import com.twock.swappricer.fpml.model.enumeration.RollConventionEnum;
import org.codehaus.stax2.XMLStreamReader2;

/**
 * @author Chris Pearson (chris@twock.com)
 */
public class CalculationPeriodFrequencyFactory {
  public CalculationPeriodFrequency readCalculationPeriodFrequency(XMLStreamReader2 streamReader) throws XMLStreamException {
    Integer periodMultiplier = null;
    PeriodEnum period = null;
    RollConventionEnum rollConvention = null;
    int startingDepth = streamReader.getDepth();
    while(streamReader.hasNext()) {
      switch(streamReader.next()) {
        case XMLEvent.START_ELEMENT:
          if(streamReader.getDepth() == startingDepth + 1) {
            String localName = streamReader.getLocalName();
            if("periodMultiplier".equals(localName)) {
              periodMultiplier = Integer.parseInt(FpmlParser.readText(streamReader));
            }
            if("period".equals(localName)) {
              period = PeriodEnum.valueOf(FpmlParser.readText(streamReader));
            }
            if("rollConvention".equals(localName)) {
              rollConvention = RollConventionEnum.fromValue(FpmlParser.readText(streamReader));
            }
          }
          break;
        case XMLEvent.END_ELEMENT:
          if(streamReader.getDepth() == startingDepth) {
            return new CalculationPeriodFrequency(periodMultiplier, period, rollConvention);
          }
      }
    }
    throw new PricerException("No more events before element finished");
  }

}
