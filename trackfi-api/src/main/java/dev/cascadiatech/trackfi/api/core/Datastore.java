package dev.cascadiatech.trackfi.api.core;

import java.util.Collection;

/**
 * Manages object storage
 * @param <W> input class
 * @param <O> output class
 */
public interface Datastore<ID, W, O> {

  /**
   * Creates a new object which does not yet exist in storage
   * @param object input object
   * @param userId unique user identifier
   * @return saved object
   */
  O create(W object, String userId);

  /**
   * Get an object by the object's id and unique user identifier
   * @param id object id
   * @param userId unique user identifier
   * @return object belonging to user
   */
  O get(ID id, String userId) throws NotFoundException;

  /**
   * Lists objects belonging to user
   * @param userId unique user identifier
   * @return objects belonging to user
   */
  Collection<O> list(String userId);

}
