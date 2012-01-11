package com.twock.swappricer.test.fpml.jaxb;

import com.twock.swappricer.fpml.jaxb.JaxbParser;
import com.twock.swappricer.jaxb.fpml_5_2.DataDocument;
import org.apache.log4j.Logger;
import org.junit.Test;

/**
 * @author Chris Pearson (chris@twock.com)
 */
public class JaxbParserTest {
  private static final Logger log = Logger.getLogger(JaxbParserTest.class);

//  @Test
  public void parseDocument() {
    DataDocument dataDocument = new JaxbParser().parseFpml(getClass().getResourceAsStream("/LCH00000513426.xml"));
    log.info("got " + dataDocument);
  }
}
