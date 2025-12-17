package dev.cascadiatech.trackfi.core;

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
   * @throws NotFoundException if object could not be found
   */
  O get(ID id, String userId) throws NotFoundException;

  /**
   * Lists objects belonging to user
   * @param userId unique user identifier
   * @return objects belonging to user
   */
  Collection<O> list(String userId);

  /**
   * Deletes an object belonging to a user
   * @param id object id
   * @param userId unique user identifier
   * @throws NotFoundException if object could not be found
   */
  void delete(Integer id, String userId) throws NotFoundException;

}
