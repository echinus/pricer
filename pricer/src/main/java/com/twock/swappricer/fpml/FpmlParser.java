package com.twock.swappricer.fpml;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;

import com.twock.swappricer.PricerException;
import com.twock.swappricer.StaxReporter;
import com.twock.swappricer.fpml.factory.SwapStreamFactory;
import com.twock.swappricer.fpml.model.DateWithDayCount;
import com.twock.swappricer.fpml.model.SwapStream;
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

  /**
   * Parse the FpML in the given inputStream and return the two swapStreams. Will always close the provided inputStream.
   *
   * @param inputStream stream from which to read the FpML, will be closed on return/exception
   * @return a List of two SwapStreams
   */
  public List<SwapStream> parse(InputStream inputStream) {
    XMLStreamReader2 streamReader = null;
    try {
      try {
        streamReader = (XMLStreamReader2)xmlInputFactory.createXMLStreamReader(inputStream, "UTF8");
      } catch(Exception e) {
        throw new PricerException("Failed to create reader to parse XML document", e);
      }
      return parseFpml(streamReader);
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

  private List<SwapStream> parseFpml(XMLStreamReader2 streamReader) throws XMLStreamException {
    List<SwapStream> swapStreams = new ArrayList<SwapStream>();
    while(streamReader.hasNext()) {
      switch(streamReader.next()) {
        case XMLEvent.START_ELEMENT:
          if("swapStream".equals(streamReader.getLocalName())) {
            SwapStream swapStream = swapStreamFactory.readSwapStream(streamReader);
            swapStreams.add(swapStream);
          }
      }
    }
    return swapStreams;
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