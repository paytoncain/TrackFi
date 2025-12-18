package dev.cascadiatech.trackfi.core;

/**
 * Database error that is attributable to a field name
 */
public class FieldDataIntegrityException extends DataIntegrityException {

  private final String field;

  /**
   * Creates a {@link FieldDataIntegrityException}
   * @param field field name causing error
   * @param message message describing problem with field's value
   */
  public FieldDataIntegrityException(String field, String message) {
    super(message);
    this.field = field;
  }

  public String getField() {
    return field;
  }

}
