package dev.cascadiatech.trackfi.api.transaction;

import dev.cascadiatech.trackfi.api.core.Datastore;
import jakarta.validation.Valid;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST endpoints for managing transactions
 */
@RestController
@RequestMapping("/api/v1/transactions")
class TransactionController {

  private final Datastore<WriteTransaction, Transaction> datastore;

  /**
   * Creates a {@link TransactionController}
   * @param datastore {@link Datastore} for managing transactions
   */
  TransactionController(Datastore<WriteTransaction, Transaction> datastore) {
    this.datastore = datastore;
  }

  /**
   * Creates a new {@link Transaction}
   * @param writeTransaction valid {@link WriteTransaction} which will be created by {@link TransactionController#datastore}
   * @param authentication {@link Authentication} containing principal for indicating user object ownership
   * @return {@link Transaction} representing created transaction
   */
  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.CREATED)
  public Transaction create(@Valid @RequestBody WriteTransaction writeTransaction, Authentication authentication) {
    return datastore.create(writeTransaction, getUserId(authentication));
  }

  /**
   * Handles Jakarta validation errors by returning field validation messages in a response body
   * @param e {@link MethodArgumentNotValidException} typically sourced from Jakarta validation errors
   * @return {@link Map} containing field validation errors
   */
  @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
  @ExceptionHandler(produces = MediaType.APPLICATION_JSON_VALUE)
  public Map<String, Object> handleError(MethodArgumentNotValidException e) {
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
   * Return user's unique identifier from spring security context
   * @param authentication {@link Authentication} containing unique user identifier
   * @return unique user identifier
   */
  private String getUserId(Authentication authentication) {
    User user = (User) authentication.getPrincipal();
    return user.getUsername();
  }
}
