package com.twock.swappricer;

import java.io.Reader;
import java.util.*;

import au.com.bytecode.opencsv.CSVReader;
import com.twock.swappricer.fpml.model.DateWithDayCount;
import com.twock.swappricer.fpml.model.enumeration.PeriodEnum;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

/**
 * @author Chris Pearson (chris@twock.com)
 */
public class CurveContainer {
  private static final Logger log = Logger.getLogger(CurveContainer.class);
  private SortedMap<String, SortedMap<Integer, Double>> historicIndexRates;
  private Map<String, String> discountCurveMapping;
  private Map<String, String> forwardCurveMapping;
  private Map<String, String> lchToFpmlCurveNameMapping;
  private Map<String, ValuationCurve> curves;

  public CurveContainer(Reader mappingsCsv, Reader curvesTsv, Reader indexRates) {
    loadMappingsFromCsv(mappingsCsv);
    loadCurvesFromTsv(curvesTsv);
    loadIndexRatesTsv(indexRates);
  }

  /**
   * Read a valuation curves mapping CSV from the given input stream, and close the input reader afterwards.
   *
   * @param input source of the CSV, will be closed before returning
   */
  public void loadMappingsFromCsv(Reader input) {
    Map<String, String> discountCurveMapping = new TreeMap<String, String>();
    Map<String, String> forwardCurveMapping = new TreeMap<String, String>();
    Map<String, String> lchToFpmlCurveNameMapping = new TreeMap<String, String>();
    try {
      CSVReader reader = new CSVReader(input);
      reader.readNext(); // dump header row
      int rowNum = 1;
      String[] line;
      while((line = reader.readNext()) != null) {
        rowNum++;
        if(line.length == 4) {
          String fpmlIndex = line[0];
          String lchIndex = line[1];
          String forwardCurve = line[2];
          String discountCurve = line[3];
          forwardCurveMapping.put(fpmlIndex, forwardCurve);
          discountCurveMapping.put(fpmlIndex, discountCurve);
          lchToFpmlCurveNameMapping.put(lchIndex, fpmlIndex);
        } else {
          log.debug("Ignoring valuation curve mapping CSV line " + rowNum + " of length " + line.length + " (expected 3)");
        }
      }
      this.discountCurveMapping = discountCurveMapping;
      this.forwardCurveMapping = forwardCurveMapping;
      this.lchToFpmlCurveNameMapping = lchToFpmlCurveNameMapping;
    } catch(Exception e) {
      throw new PricerException("Failed to read in tab separated holiday calendar file", e);
    } finally {
      IOUtils.closeQuietly(input);
    }
  }

  /**
   * Read a valuation curves mapping CSV from the given input stream, and close the input reader afterwards.
   *
   * @param input source of the CSV, will be closed before returning
   */
  public void loadCurvesFromTsv(Reader input) {
    Map<String, ValuationCurve> curves = new TreeMap<String, ValuationCurve>();
    try {
      CSVReader reader = new CSVReader(input, '\t');
      reader.readNext(); // dump header row
      int rowNum = 1;
      String[] line;
      String currentCurve = null;
      List<Integer> maturityDates = new ArrayList<Integer>();
      List<Double> zeroRates = new ArrayList<Double>();
      List<Double> discountFactors = new ArrayList<Double>();
      DateWithDayCount closeDate = null;
      while((line = reader.readNext()) != null) {
        rowNum++;
        if(line.length == 6) {
          String curve = line[0];
          int maturityDate = DateUtil.dateToDayCount(new short[]{Short.parseShort(line[2].substring(6, 10)), Short.parseShort(line[2].substring(3, 5)), Short.parseShort(line[2].substring(0, 2))});
          double zeroRate = Double.parseDouble(line[4]);
          double discountFactor = Double.parseDouble(line[5]);

          if(currentCurve != null && !currentCurve.equals(curve)) {
            curves.put(currentCurve, new ValuationCurve(currentCurve, closeDate, convertIntegers(maturityDates), convertDoubles(zeroRates), convertDoubles(discountFactors)));
            maturityDates = new ArrayList<Integer>();
            zeroRates = new ArrayList<Double>();
            discountFactors = new ArrayList<Double>();
          }
          currentCurve = curve;
          closeDate = new DateWithDayCount(new short[]{Short.parseShort(line[1].substring(6, 10)), Short.parseShort(line[1].substring(3, 5)), Short.parseShort(line[1].substring(0, 2))});
          int insertPosition = -(Collections.binarySearch(maturityDates, maturityDate) + 1);
          maturityDates.add(insertPosition, maturityDate);
          zeroRates.add(insertPosition, zeroRate);
          discountFactors.add(insertPosition, discountFactor);
        } else {
          log.debug("Ignoring zero curve TSV line " + rowNum + " of length " + line.length + " (expected 6)");
        }
      }
      if(currentCurve != null) {
        curves.put(currentCurve, new ValuationCurve(currentCurve, closeDate, convertIntegers(maturityDates), convertDoubles(zeroRates), convertDoubles(discountFactors)));
      }
    } catch(Exception e) {
      throw new PricerException("Failed to read in tab separated holiday calendar file", e);
    } finally {
      IOUtils.closeQuietly(input);
    }
    this.curves = curves;
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
      mapping = index == null ? null : mappings.get(index);
      if(mapping == null) {
        mapping = mappings.get(currency);
        if(mapping == null) {
          throw new MissingMappingException("index=" + index + ", currency=" + currency + ", periodMultiplier=" + periodMultiplier + ", period=" + period);
        }
      }
    }
    return mapping;
  }

  private static int[] convertIntegers(List<Integer> integers) {
    int[] ret = new int[integers.size()];
    Iterator<Integer> iterator = integers.iterator();
    for(int i = 0; i < ret.length; i++) {
      ret[i] = iterator.next();
    }
    return ret;
  }

  private static double[] convertDoubles(List<Double> doubles) {
    double[] ret = new double[doubles.size()];
    Iterator<Double> iterator = doubles.iterator();
    for(int i = 0; i < ret.length; i++) {
      ret[i] = iterator.next();
    }
    return ret;
  }

  /**
   * Obtain the curve stored with the current name.
   *
   * @param curveName name of the curve to get
   * @return the curve, otherwise an exception is thrown if it is unknown
   */
  public ValuationCurve getCurve(String curveName) {
    ValuationCurve valuationCurve = curves.get(curveName);
    if(valuationCurve == null) {
      throw new PricerException("No such curve " + curveName + ", available curves are " + curves.keySet());
    }
    return valuationCurve;
  }

  public void loadIndexRatesTsv(Reader input) {
    SortedMap<String, SortedMap<Integer, Double>> historicRates = new TreeMap<String, SortedMap<Integer, Double>>();
    Set<String> unmappedLchIndices = new TreeSet<String>();
    try {
      CSVReader reader = new CSVReader(input, '\t');
      reader.readNext(); // dump header row
      int rowNum = 1;
      String[] line;
      DateWithDayCount closeDate = null;
      while((line = reader.readNext()) != null) {
        rowNum++;
        if(line.length == 8) {
//          Currency	Indexname	Tenorunit	Tenorperiod	Fixingdate	Effectivedate	Indexrate	regulatoryBody
//          AUD	LIBOR	1	M	04/10/2010 00:00:00	06/10/2010 00:00:00	4.71250	BBA
          String currency = line[0];
          String indexName = line[1];
          int tenorPeriodMultiplier = Integer.parseInt(line[2]);
          PeriodEnum tenorPeriod = PeriodEnum.valueOf(line[3]);
          int fixingDate = DateUtil.dateToDayCount(new short[]{Short.parseShort(line[4].substring(6, 10)), Short.parseShort(line[4].substring(3, 5)), Short.parseShort(line[4].substring(0, 2))});
          double indexRate = Double.parseDouble(line[6]);
          String regulatoryBody = line[7];
          String lchIndexKey = currency + ' ' + indexName + ' ' + regulatoryBody + ' ' + tenorPeriodMultiplier + tenorPeriod.name();
          String indexKey = unmappedLchIndices.contains(lchIndexKey) ? null : lchToFpmlCurveNameMapping.get(lchIndexKey);
          if(indexKey == null) {
            unmappedLchIndices.add(lchIndexKey);
          } else {
            SortedMap<Integer, Double> ratesForIndex = historicRates.get(indexKey);
            if(ratesForIndex == null) {
              ratesForIndex = new TreeMap<Integer, Double>();
              historicRates.put(indexKey, ratesForIndex);
            }
            ratesForIndex.put(fixingDate, indexRate);
          }
        } else {
          log.debug("Ignoring index rate TSV line " + rowNum + " of length " + line.length + " (expected 8)");
        }
      }
      if(!unmappedLchIndices.isEmpty()) {
        log.debug(unmappedLchIndices.size() + " unmapped LCH indices have historic rates that haven't been loaded: " + unmappedLchIndices);
      }
    } catch(Exception e) {
      throw new PricerException("Failed to read in tab separated holiday calendar file", e);
    } finally {
      IOUtils.closeQuietly(input);
    }
    historicIndexRates = historicRates;
  }

  public SortedMap<Integer, Double> getHistoricIndexRates(String fpmlIndex, Integer periodMultiplier, PeriodEnum period) {
    SortedMap<Integer, Double> historicRates = periodMultiplier == null || period == null ? null : historicIndexRates.get(fpmlIndex + ' ' + periodMultiplier + period.name());
    if(historicRates == null) {
      historicRates = fpmlIndex == null ? null : historicIndexRates.get(fpmlIndex);
      if(historicRates == null) {
        throw new MissingMappingException("index=" + fpmlIndex + ", periodMultiplier=" + periodMultiplier + ", period=" + period);
      }
    }
    return historicRates;
  }
}
