package com.github.paytoncain.trackfi.core;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

/**
 * Utility methods for creating instances of {@link Datastore} compatible with JPA
 */
public final class DatastoreFactory {

  /**
   * Creates instance of {@link Datastore} for managing objects
   *
   * @param <ID>              entity id type
   * @param <W>               write view type
   * @param <T>               view type
   * @param <E>               entity type
   * @param <P>               page parameters type, default can be used if search functionality is not being provided
   * @param repository        {@link BaseRepository} for managing object with JPA
   * @param entityTransform   function for creating a view from an entity
   * @param viewTransform     function for creating an entity from a write view
   * @param getSpecifications function for creating an array JPA {@link Specification} from parameters
   * @param idTranslator      function for translating id string to required database type
   * @return instance of {@link Datastore} for managing objects within application components
   */
  public static <ID, W, T, E extends BaseEntity<ID>, P extends PageParameters> Datastore<W, T, P> create(
    BaseRepository<ID, E> repository,
    Function<E, T> entityTransform,
    BiFunction<W, String, E> viewTransform,
    Function<DataIntegrityViolationException, DataIntegrityException> exceptionHandler,
    Function<P, Specification<E>> getSpecifications,
    Function<String, ID> idTranslator
  ) {
    return new Datastore<>() {
      /**
       * Create new object
       * @param object input object
       * @param userId unique user identifier
       * @return created object
       */
      @Override
      public T create(W object, String userId) throws DataIntegrityException {
        E entity = viewTransform.apply(object, userId);
        assert entity.getDeleted() == false; // make sure that a user cannot create a hidden entity

        try {
          entity = repository.save(entity);
        } catch (DataIntegrityViolationException e) {
          throw exceptionHandler.apply(e);
        }

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
      public T get(String id, String userId) throws NotFoundException {
        E entity = getEntity(id, userId);
        return entityTransform.apply(entity);
      }

      /**
       * Get paginated objects belonging to user with delete flag = false
       * @param parameters {@link PageParameters} including parameters for filtering results
       * @param userId unique user identifier
       * @return objects belonging to user
       */
      @Override
      public PageView<T> list(P parameters, String userId) {
        Specification<E> specification = Specification.<E>unrestricted()
          .and(getSpecifications.apply(parameters))
          .and((root, query, criteriaBuilder) -> criteriaBuilder.and(
            criteriaBuilder.equal(root.get("userId"), userId),
            criteriaBuilder.equal(root.get("deleted"), false)
          ));

        Page<T> page = repository.findAll(
            specification,
            PageRequest.of(parameters.getPage() - 1, parameters.getItemPerPage())
          ).map(entityTransform);

        return PageView.<T>builder()
          .page(page.getNumber() + 1)
          .itemsPerPage(page.getSize())
          .items(page.getContent())
          .totalItems(page.getTotalElements())
          .totalPages(page.getTotalPages())
          .build();
      }

      /**
       * Set object belonging to user's delete flag = true
       * @param id object id
       * @param userId unique user identifier
       * @throws NotFoundException if object cannot be found
       */
      @Override
      public void delete(String id, String userId) throws NotFoundException {
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
      private E getEntity(String id, String userId) throws NotFoundException {
        Optional<E> maybeEntity = repository.getByIdAndUserIdAndDeleted(idTranslator.apply(id), userId, false); // only return entity if not deleted
        if (maybeEntity.isEmpty()) {
          throw new NotFoundException("object with id=%s not found".formatted(id));
        }

        return maybeEntity.get();
      }
    };
  }

}
