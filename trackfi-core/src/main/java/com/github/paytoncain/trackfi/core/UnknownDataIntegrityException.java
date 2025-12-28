package com.github.paytoncain.trackfi.core;

/**
 * Database error which is not attributable to field
 */
public class UnknownDataIntegrityException extends DataIntegrityException {

  /**
   * Creates an {@link UnknownDataIntegrityException}
   */
  public UnknownDataIntegrityException() {
    super("an unknown datastore error occurred");
  }

}
