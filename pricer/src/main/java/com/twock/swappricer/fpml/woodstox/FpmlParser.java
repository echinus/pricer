package com.twock.swappricer.fpml.woodstox;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;

import com.twock.swappricer.PricerException;
import com.twock.swappricer.StaxReporter;
import com.twock.swappricer.fpml.woodstox.factory.SwapStreamFactory;
import com.twock.swappricer.fpml.woodstox.model.DateWithDayCount;
import com.twock.swappricer.fpml.woodstox.model.SwapStream;
import org.apache.log4j.Logger;
import org.codehaus.stax2.XMLInputFactory2;
import org.codehaus.stax2.XMLStreamReader2;

/**
 * @author Chris Pearson (chris@twock.com)
 */
public class FpmlParser {
  private static final Logger log = Logger.getLogger(FpmlParser.class);
  private final XMLInputFactory2 xmlInputFactory;
  private final SwapStreamFactory swapStreamFactory;

  public FpmlParser(SwapStreamFactory swapStreamFactory) {
    this.swapStreamFactory = swapStreamFactory;
    XMLInputFactory2 xmlInputFactory = (XMLInputFactory2)XMLInputFactory2.newInstance();
    xmlInputFactory.setProperty(XMLInputFactory.SUPPORT_DTD, false);
    xmlInputFactory.setProperty(XMLInputFactory.IS_NAMESPACE_AWARE, false);
    xmlInputFactory.setProperty(XMLInputFactory.REPORTER, new StaxReporter());
    xmlInputFactory.configureForSpeed();
    this.xmlInputFactory = xmlInputFactory;
  }

  public void parse(InputStream inputStream) {
    XMLStreamReader2 streamReader = null;
    try {
      try {
        streamReader = (XMLStreamReader2)xmlInputFactory.createXMLStreamReader(inputStream, "UTF8");
      } catch(Exception e) {
        throw new PricerException("Failed to create reader to parse XML document", e);
      }
      parseFpml(streamReader);
    } catch(XMLStreamException e) {
      throw new PricerException("Error parsing XML document", e);
    } finally {
      if(streamReader != null) {
        try {
          streamReader.close();
        } catch(XMLStreamException e) {
          log.warn("Failed to close streamReader", e);
        }
      }
    }
  }

  private void parseFpml(XMLStreamReader2 streamReader) throws XMLStreamException {
    while(streamReader.hasNext()) {
      switch(streamReader.next()) {
        case XMLEvent.START_ELEMENT:
          if("swapStream".equals(streamReader.getLocalName())) {
            SwapStream swapStream = swapStreamFactory.readSwapStream(streamReader);
            log.info("Read " + swapStream);
          }
      }
    }
  }

  public static DateWithDayCount readTextDate(XMLStreamReader2 streamReader) throws XMLStreamException {
    String sb = readText(streamReader);
    return new DateWithDayCount(new short[]{Short.parseShort(sb.substring(0, 4)), Short.parseShort(sb.substring(5, 7)), Short.parseShort(sb.substring(8, 10))});
  }

  public static String readText(XMLStreamReader2 streamReader) throws XMLStreamException {
    streamReader.next();
    StringWriter writer = new StringWriter();
    try {
      streamReader.getText(writer, false);
      return writer.toString();
    } catch(IOException e) {
      throw new PricerException("Failed to read text from XML document", e);
    }
  }
}