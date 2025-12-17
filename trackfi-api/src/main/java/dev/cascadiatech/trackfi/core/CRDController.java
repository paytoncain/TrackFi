package dev.cascadiatech.trackfi.core;

import jakarta.validation.Valid;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * REST endpoints for managing objects
 * @param <ID> object id type
 * @param <W> write object type
 * @param <T> object type
 */
public abstract class CRDController<ID, W, T> {

  private final Datastore<ID, W, T> datastore;

  /**
   * Creates a {@link CRDController}
   * @param datastore {@link Datastore} for managing objects
   */
  protected CRDController(Datastore<ID, W, T> datastore) {
    this.datastore = datastore;
  }

  /**
   * Creates a new object
   * @param writeObject valid object which will be created by {@link CRDController#datastore}
   * @param authentication {@link Authentication} containing principal for indicating user object ownership
   * @return created object
   */
  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.CREATED)
  public T create(@Valid @RequestBody W writeObject, Authentication authentication) {
    return datastore.create(writeObject, getUserId(authentication));
  }

  /**
   * Lists object belonging to the currently authenticated user
   * @param authentication {@link Authentication} containing principal for indicating user object ownership
   * @return objects belonging to user
   */
  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public Collection<T> list(Authentication authentication) {
    return datastore.list(getUserId(authentication));
  }

  /**
   * Gets an object belonging to a user by its id
   * @param id object id
   * @param authentication {@link Authentication} containing principal for indicating user object ownership
   * @return object belonging to user
   * @throws NotFoundException if object cannot be found
   */
  @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public T get(@PathVariable("id") ID id, Authentication authentication) throws NotFoundException {
    return datastore.get(id, getUserId(authentication));
  }

  /**
   * Deletes an object belonging to a user
   * @param id object id
   * @param authentication {@link Authentication} containing principal for indicating user object ownership
   * @throws NotFoundException if object cannot be found
   */
  @DeleteMapping(path = "/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable("id") ID id, Authentication authentication) throws NotFoundException {
    datastore.delete(id, getUserId(authentication));
  }

  /**
   * Return user's unique identifier from spring security context
   * @param authentication {@link Authentication} containing unique user identifier
   * @return unique user identifier
   */
  public static String getUserId(Authentication authentication) {
    User user = (User) authentication.getPrincipal();
    return user.getUsername();
  }

  /**
   * Handles Jakarta validation errors by returning field validation messages in a response body
   * @param e {@link MethodArgumentNotValidException} typically sourced from Jakarta validation errors
   * @return {@link Map} containing field validation errors
   */
  @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
  @ExceptionHandler(produces = MediaType.APPLICATION_JSON_VALUE)
  private Map<String, Object> handleValidationException(MethodArgumentNotValidException e) {
    return Map.of(
      "fieldErrors", e.getFieldErrors().stream()
        .collect(Collectors.groupingBy(
          FieldError::getField,
          Collectors.mapping(
            DefaultMessageSourceResolvable::getDefaultMessage,
            Collectors.toList()
          )
        ))
    );
  }

  /**
   * Handles {@link NotFoundException}, which should be thrown when a resource requested by a user cannot be found
   * @param e {@link NotFoundException}
   * @return {@link Map} containing error message(s)
   */
  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler(produces = MediaType.APPLICATION_JSON_VALUE)
  private Map<String, Object> handleNotFoundException(NotFoundException e) {
    return Map.of(
      "requestErrors", Collections.singletonList(e.getMessage())
    );
  }

}
