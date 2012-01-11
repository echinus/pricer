package com.twock.swappricer.fpml.woodstox.factory;

import java.util.ArrayList;
import java.util.List;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;

import com.twock.swappricer.PricerException;
import com.twock.swappricer.fpml.woodstox.FpmlParser;
import com.twock.swappricer.fpml.woodstox.model.BusinessDayAdjustments;
import com.twock.swappricer.fpml.woodstox.model.BusinessDayConventionEnum;
import org.codehaus.stax2.XMLStreamReader2;

/**
 * @author Chris Pearson (chris@twock.com)
 */
public class BusinessDayAdjustmentsFactory {
  public BusinessDayAdjustments readBusinessDayAdjustments(XMLStreamReader2 streamReader) throws XMLStreamException {
    BusinessDayConventionEnum businessDayConvention = null;
    List<String> businessCenters = null;
    int startingDepth = streamReader.getDepth();
    while(streamReader.hasNext()) {
      switch(streamReader.next()) {
        case XMLEvent.START_ELEMENT:
          if(streamReader.getDepth() == startingDepth + 1) {
            String localName = streamReader.getLocalName();
            if("businessDayConvention".equals(localName)) {
              businessDayConvention = BusinessDayConventionEnum.fromValue(FpmlParser.readText(streamReader));
            }
            if("businessCenter".equals(localName)) {
              if(businessCenters == null) {
                businessCenters = new ArrayList<String>();
              }
              businessCenters.add(FpmlParser.readText(streamReader));
            }
          }
          break;
        case XMLEvent.END_ELEMENT:
          if(streamReader.getDepth() == startingDepth) {
            return new BusinessDayAdjustments(businessDayConvention, businessCenters == null ? null : businessCenters.toArray(new String[businessCenters.size()]));
          }
      }
    }
    throw new PricerException("No more events before element finished");
  }
}
