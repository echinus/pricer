package com.twock.swappricer;

/**
 * @author Chris Pearson (chris@twock.com)
 */
public class PricerException extends RuntimeException {
  public PricerException(String message) {
    super(message);
  }

  public PricerException(String message, Throwable cause) {
    super(message, cause);
  }
}
