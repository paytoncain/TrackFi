package dev.cascadiatech.trackfi.core;

/**
 * Base exception encapsulating errors coming from database
 */
public abstract class DataIntegrityException extends Exception {
  protected DataIntegrityException(String message) {
    super(message);
  }
}
