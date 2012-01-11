package com.twock.swappricer;

import javax.xml.stream.Location;
import javax.xml.stream.XMLReporter;
import javax.xml.stream.XMLStreamException;

import org.apache.log4j.Logger;

/**
 * @author Chris Pearson (chris@twock.com)
 */
public class StaxReporter implements XMLReporter {
  private static final Logger log = Logger.getLogger(StaxReporter.class);

  @Override
  public void report(String message, String errorType, Object relatedInformation, Location location) throws XMLStreamException {
    log.warn("message=" + message + ", relatedInformation=" + relatedInformation + ", location=" + location);
  }
}
