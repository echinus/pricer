package com.twock.swappricer;

/**
 * @author Chris Pearson (chris@twock.com)
 */
public class MissingMappingException extends PricerException {
  public MissingMappingException(String message) {
    super(message);
  }

  public MissingMappingException(String message, Throwable cause) {
    super(message, cause);
  }
}
