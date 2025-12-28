package com.github.paytoncain.trackfi.core;

/**
 * Exception to be thrown when resources requested by a user cannot be found
 */
public class NotFoundException extends Exception {

  /**
   * Creates a {@link NotFoundException}
   * @param message Error message
   */
  public NotFoundException(String message) {
    super(message);
  }

}
