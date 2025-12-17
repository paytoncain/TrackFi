package dev.cascadiatech.trackfi.transaction;

import dev.cascadiatech.trackfi.core.Datastore;
import dev.cascadiatech.trackfi.core.DatastoreFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Beans supporting transactions functionality
 */
@Configuration
class TransactionConfig {

  /**
   * Create instance of {@link Datastore} for managing {@link Transaction}
   * @param transactionRepository {@link org.springframework.data.jpa.repository.JpaRepository} for managing transactions with JPA
   * @return {@link Datastore} for managing transactions within application components
   */
  @Bean
  Datastore<Integer, WriteTransaction, Transaction> transactionDatastore(TransactionRepository transactionRepository) {
    return DatastoreFactory.create(
      transactionRepository,
      transactionEntity -> new Transaction(transactionEntity.getId(), transactionEntity.getVendor(), transactionEntity.getAmount(), transactionEntity.getDate()),
      (object, userId) -> new TransactionEntity(null, userId, false, object.vendor(), object.amount(), object.date())
    );
  }

}
