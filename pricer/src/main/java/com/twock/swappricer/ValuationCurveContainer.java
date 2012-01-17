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
public class ValuationCurveContainer {
  private static final Logger log = Logger.getLogger(ValuationCurveContainer.class);
  private Map<String, String> discountCurveMapping;
  private Map<String, String> forwardCurveMapping;
  private Map<String, ValuationCurve> curves;

  public ValuationCurveContainer(Reader mappingsCsv, Reader curvesTsv) {
    loadMappingsFromCsv(mappingsCsv);
    loadCurvesFromTsv(curvesTsv);
  }

  /**
   * Read a valuation curves mapping CSV from the given input stream, and close the input reader afterwards.
   *
   * @param input source of the CSV, will be closed before returning
   */
  public void loadMappingsFromCsv(Reader input) {
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

  /**
   * When given a curve name and a date, obtain the discount factor by linearly interpolating between tenors.
   *
   * @param curveName mapped curve name
   * @param dayCount numeric date from DateUtil
   * @return the discount factor for the given date from the given curve
   */
  public double getDiscountFactor(String curveName, int dayCount) {
    ValuationCurve valuationCurve = curves.get(curveName);
    if(valuationCurve == null) {
      throw new PricerException("No such curve " + curveName + ", available curves are " + curves.keySet());
    }
    int[] maturityDates = valuationCurve.getMaturityDates();
    int position = Arrays.binarySearch(maturityDates, dayCount);
    if(position >= 0) {
      if(position == 0) {
        throw new PricerException("Extrapolation earlier than first date " + Arrays.toString(DateUtil.dayCountToDate(maturityDates[0])) + " not currently supported for curve " + curveName + " and date " + Arrays.toString(DateUtil.dayCountToDate(dayCount)));
      } else if(position == maturityDates.length) {
        throw new PricerException("Extrapolation not currently supported for curve " + curveName + " and date " + Arrays.toString(DateUtil.dayCountToDate(dayCount)));
      }
      return valuationCurve.getDiscountFactors()[position];
    } else {
      position = -(position + 1);
      if(position == 0) {
        throw new PricerException("Extrapolation earlier than first date " + Arrays.toString(DateUtil.dayCountToDate(maturityDates[0])) + " not currently supported for curve " + curveName + " and date " + Arrays.toString(DateUtil.dayCountToDate(dayCount)));
      } else if(position == maturityDates.length) {
        throw new PricerException("Extrapolation not currently supported for curve " + curveName + " and date " + Arrays.toString(DateUtil.dayCountToDate(dayCount)));
      }
      int earlierDate = maturityDates[position - 1];
      int laterDate = maturityDates[position];
      double[] zeroRates = valuationCurve.getZeroRates();
      double earlierRate = zeroRates[position - 1];
      double laterRate = zeroRates[position];
      // df = EXP(zero rate * -(flow date – valuation date)/365)
      // R = R1 + (D – D1)*(R2 – R1)/(D2 – D1)
      // R = interpolated zero rate
      // R1= zero rate for closest curve pillar with earlier date
      // R2 = zero rate for closest curve pillar with later date
      // D = value date for forward flow
      // D1 = value date for closest curve pillar with earlier date
      // D2 = value date for closest curve pillar with later date
      double interpolatedZeroRate = earlierRate + (dayCount - earlierDate) * (laterRate - earlierRate) / (laterDate - earlierDate);
      return Math.exp(interpolatedZeroRate * -(dayCount - valuationCurve.getCurveDate().getDayCount()) / 365.0);
    }
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
}
