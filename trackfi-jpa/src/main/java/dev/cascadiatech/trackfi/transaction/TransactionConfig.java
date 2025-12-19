package dev.cascadiatech.trackfi.transaction;

import dev.cascadiatech.trackfi.core.Datastore;
import dev.cascadiatech.trackfi.core.DatastoreFactory;
import dev.cascadiatech.trackfi.core.FieldDataIntegrityException;
import dev.cascadiatech.trackfi.core.PageParameters;
import dev.cascadiatech.trackfi.core.UnknownDataIntegrityException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Beans supporting transactions functionality
 */
@Configuration
class TransactionConfig {

  /**
   * Create instance of {@link Datastore} for managing {@link TransactionView}
   * @param transactionRepository {@link org.springframework.data.jpa.repository.JpaRepository} for managing transactions with JPA
   * @return {@link Datastore} for managing transactions within application components
   */
  @Bean
  Datastore<Integer, WriteTransactionView, TransactionView, PageParameters> transactionDatastore(TransactionRepository transactionRepository) {
    return DatastoreFactory.create(
      transactionRepository,
      transactionEntity -> new TransactionView(transactionEntity.getId(), transactionEntity.getCategoryId(), transactionEntity.getVendor(), transactionEntity.getAmount(), transactionEntity.getDate()),
      (object, userId) -> new TransactionEntity(null, userId, false, object.categoryId(), object.vendor(), object.amount(), object.date()),
      violation -> {
        if (violation.getCause() instanceof ConstraintViolationException constraintViolationException) {
          if ("TRANSACTION_CATEGORY_FK".equals(constraintViolationException.getConstraintName())) {
            return new FieldDataIntegrityException("categoryId", "category not found");
          }
        }
        return new UnknownDataIntegrityException();
      }
    );
  }

}
