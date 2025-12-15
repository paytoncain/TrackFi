package dev.cascadiatech.trackfi.api.transaction;

import dev.cascadiatech.trackfi.api.core.Datastore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Beans supporting transactions functionality
 */
@Configuration
class TransactionConfig {

  /**
   *
   * @param transactionRepository {@link org.springframework.data.jpa.repository.JpaRepository} for managing transactions with JPA
   * @return {@link Datastore} for managing transactions within application components
   */
  @Bean
  Datastore<WriteTransaction, Transaction> transactionDatastore(TransactionRepository transactionRepository) {
    return object -> {
      TransactionEntity e = transactionRepository.save(
        new TransactionEntity(null, object.userId(), object.vendor(), object.amount(),
          object.date()));

      return new Transaction(e.id(), e.userId(), e.vendor(), e.amount(), e.date());
    };
  }

}
