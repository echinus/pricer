package com.twock.swappricer.fpml.jaxb;

import java.io.InputStream;
import javax.xml.bind.*;

import com.twock.swappricer.PricerException;
import com.twock.swappricer.jaxb.fpml_5_2.DataDocument;
import org.apache.log4j.Logger;

/**
 * @author Chris Pearson (chris@twock.com)
 */
public class JaxbParser {
  private static final Logger log = Logger.getLogger(JaxbParser.class);
  private JAXBContext context;

  public JaxbParser() {
    try {
      long startTime = System.currentTimeMillis();
      this.context = JAXBContext.newInstance(DataDocument.class);
      long endTime = System.currentTimeMillis();
      log.info("Took " + (endTime - startTime) + "ms to create JAXBContext for " + DataDocument.class.getName());
    } catch(JAXBException e) {
      throw new PricerException("Failed to create JAXBContext for " + DataDocument.class.getName(), e);
    }
  }

  public DataDocument parseFpml(InputStream inputStream) {
    try {
      Unmarshaller unmarshaller = context.createUnmarshaller();
      long startTime = System.currentTimeMillis();
      @SuppressWarnings("unchecked")
      JAXBElement<DataDocument> jaxbElement = (JAXBElement<DataDocument>)unmarshaller.unmarshal(inputStream);
      long endTime = System.currentTimeMillis();
      log.info("Took " + (endTime - startTime) + "ms to unmarshal document");
      return jaxbElement.getValue();
    } catch(Exception e) {
      throw new PricerException("Failed to unmarshal XML document", e);
    }
  }
}
