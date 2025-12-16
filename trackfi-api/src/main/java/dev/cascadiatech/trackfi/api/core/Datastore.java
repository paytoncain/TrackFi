package dev.cascadiatech.trackfi.api.core;

import java.util.Collection;

/**
 * Manages object storage
 * @param <W> input class
 * @param <O> output class
 */
public interface Datastore<W, O> {

  /**
   * Creates a new object which does not yet exist in storage
   * @param object input object
   * @param userId unique user identifier
   * @return saved object
   */
  O create(W object, String userId);

  /**
   * Lists objects belonging to user
   * @param userId unique user identifier
   * @return objects belonging to user
   */
  Collection<O> list(String userId);

}
