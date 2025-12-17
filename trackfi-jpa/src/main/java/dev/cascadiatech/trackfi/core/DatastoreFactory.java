package dev.cascadiatech.trackfi.core;

import java.util.Collection;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Utility methods for creating instances of {@link Datastore} compatible with JPA
 */
public final class DatastoreFactory {

  /**
   * Creates instance of {@link Datastore} for managing objects
   * @param repository {@link BaseRepository} for managing object with JPA
   * @param entityTransform function for creating a view from an entity
   * @param viewTransform function for creating an entity from a write view
   * @return instance of {@link Datastore} for managing objects within application components
   * @param <ID> entity id type
   * @param <W> write view type
   * @param <T> view type
   * @param <E> entity type
   */
  public static <ID, W, T, E extends BaseEntity<ID>> Datastore<ID, W, T> create(BaseRepository<ID, E> repository, Function<E, T> entityTransform, BiFunction<W, String, E> viewTransform) {
    return new Datastore<>() {
      /**
       * Create new object
       * @param object input object
       * @param userId unique user identifier
       * @return created object
       */
      @Override
      public T create(W object, String userId) {
        E entity = viewTransform.apply(object, userId);
        assert entity.getDeleted() == false; // make sure that a user cannot create a hidden entity
        entity = repository.save(entity);
        return entityTransform.apply(entity);
      }

      /**
       * Get object belonging to user by id with delete flag = false
       * @param id object id
       * @param userId unique user identifier
       * @return object belonging to user
       * @throws NotFoundException if object could not be found
       */
      @Override
      public T get(ID id, String userId) throws NotFoundException {
        E entity = getEntity(id, userId);
        return entityTransform.apply(entity);
      }

      /**
       * Get objects belonging to user with delete flag = false
       * @param userId unique user identifier
       * @return objects belonging to user
       */
      @Override
      public Collection<T> list(String userId) {
        return repository.findByUserIdAndDeleted(userId, false).stream() // search across entities that are not deleted
          .map(entityTransform)
          .toList();
      }

      /**
       * Set object belonging to user's delete flag = true
       * @param id object id
       * @param userId unique user identifier
       * @throws NotFoundException if object cannot be found
       */
      @Override
      public void delete(ID id, String userId) throws NotFoundException {
        E entity = getEntity(id, userId);
        entity.setDeleted(true); // entities are not deleted, but hidden from user view with 'deleted' flag
        repository.save(entity);
      }

      /**
       * Get object by id with delete flag = false
       * @param id object id
       * @param userId unique user identifier
       * @return object matching id with delete flag = false
       * @throws NotFoundException if object could not be found
       */
      private E getEntity(ID id, String userId) throws NotFoundException {
        Optional<E> maybeEntity = repository.getByIdAndUserIdAndDeleted(id, userId, false); // only return entity if not deleted
        if (maybeEntity.isEmpty()) {
          throw new NotFoundException("object with id=%s not found".formatted(id));
        }

        return maybeEntity.get();
      }
    };
  }

}
