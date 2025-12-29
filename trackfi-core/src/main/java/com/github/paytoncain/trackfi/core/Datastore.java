package com.github.paytoncain.trackfi.core;

/**
 * Manages object storage
 * @param <W> input class
 * @param <O> output class
 */
public interface Datastore<W, O, P extends PageParameters> {

  /**
   * Creates a new object which does not yet exist in storage
   * @param object input object
   * @param userId unique user identifier
   * @return saved object
   * @throws DataIntegrityException if database transaction fails
   */
  O create(W object, String userId) throws DataIntegrityException;

  /**
   * Get an object by the object's id and unique user identifier
   * @param id object id
   * @param userId unique user identifier
   * @return object belonging to user
   * @throws NotFoundException if object could not be found
   */
  O get(String id, String userId) throws NotFoundException;

  /**
   * Lists objects belonging to user
   * @param parameters {@link PageParameters} for paging and filtering results
   * @param userId unique user identifier
   * @return paged items belonging to user
   */
  PageView<O> list(P parameters, String userId);

  /**
   * Deletes an object belonging to a user
   * @param id object id
   * @param userId unique user identifier
   * @throws NotFoundException if object could not be found
   */
  void delete(String id, String userId) throws NotFoundException;

}
