package com.twock.swappricer;

import java.io.Reader;
import java.util.Map;
import java.util.TreeMap;

import au.com.bytecode.opencsv.CSVReader;
import com.twock.swappricer.fpml.model.enumeration.PeriodEnum;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

/**
 * @author Chris Pearson (chris@twock.com)
 */
public class ValuationCurveContainer {
  private static final Logger log = Logger.getLogger(ValuationCurveContainer.class);
  private Map<String, String> discountCurveMapping;
  private Map<String, String> forwardCurveMapping;

  public ValuationCurveContainer(Reader input) {
    loadFromCsv(input);
  }

  /**
   * Read a valuation curves mapping CSV from the given input stream, and close the input reader afterwards.
   *
   * @param input source of the CSV, will be closed before returning
   */
  public void loadFromCsv(Reader input) {
    Map<String, String> discountCurveMapping = new TreeMap<String, String>();
    Map<String, String> forwardCurveMapping = new TreeMap<String, String>();
    try {
      CSVReader reader = new CSVReader(input);
      reader.readNext(); // dump header row
      int rowNum = 1;
      String[] line;
      while((line = reader.readNext()) != null) {
        rowNum++;
        if(line.length == 3) {
          String index = line[0];
          String forwardCurve = line[1];
          String discountCurve = line[2];
          forwardCurveMapping.put(index, forwardCurve);
          discountCurveMapping.put(index, discountCurve);
        } else {
          log.debug("Ignoring valuation curve mapping CSV line " + rowNum + " of length " + line.length + " (expected 3)");
        }
      }
      this.discountCurveMapping = discountCurveMapping;
      this.forwardCurveMapping = forwardCurveMapping;
    } catch(Exception e) {
      throw new PricerException("Failed to read in tab separated holiday calendar file", e);
    } finally {
      IOUtils.closeQuietly(input);
    }
  }

  /**
   * Get the discount curve assigned for the given index, tenor, and currency.
   *
   * @param index FpML index name
   * @param periodMultiplier index tenor period multiplier, can be null if no index tenor provided
   * @param period index tenor period unit, can be null if no index tenor provided
   * @param currency currency of the index
   * @return the discount curve name to use
   */
  public String getDiscountCurve(String index, Integer periodMultiplier, PeriodEnum period, String currency) {
    return getMapping(index, periodMultiplier, period, currency, discountCurveMapping);
  }

  /**
   * Get the forward curve assigned for the given index, tenor, and currency.
   *
   * @param index FpML index name
   * @param periodMultiplier index tenor period multiplier, can be null if no index tenor provided
   * @param period index tenor period unit, can be null if no index tenor provided
   * @param currency currency of the index
   * @return the discount curve name to use
   */
  public String getForwardCurve(String index, Integer periodMultiplier, PeriodEnum period, String currency) {
    return getMapping(index, periodMultiplier, period, currency, forwardCurveMapping);
  }

  private static String getMapping(String index, Integer periodMultiplier, PeriodEnum period, String currency, Map<String, String> mappings) {
    String mapping = periodMultiplier == null || period == null ? null : mappings.get(index + ' ' + periodMultiplier + period.name());
    if(mapping == null) {
      mapping = mappings.get(index);
      if(mapping == null) {
        mapping = mappings.get(currency);
        if(mapping == null) {
          throw new MissingMappingException("index=" + index + ", currency=" + currency + ", periodMultiplier=" + periodMultiplier + ", period=" + period);
        }
      }
    }
    return mapping;
  }
}
