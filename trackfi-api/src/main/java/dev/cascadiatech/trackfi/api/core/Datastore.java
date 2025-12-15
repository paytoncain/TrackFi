package dev.cascadiatech.trackfi.api.core;

/**
 * Manages object storage
 * @param <W> input class
 * @param <O> output class
 */
public interface Datastore<W, O> {

  /**
   * Creates a new object which does not yet exist in storage
   * @param object input object
   * @return saved object
   */
  O create(W object);

}
