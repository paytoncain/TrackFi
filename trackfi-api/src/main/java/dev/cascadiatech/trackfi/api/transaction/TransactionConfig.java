package dev.cascadiatech.trackfi.api.transaction;

import dev.cascadiatech.trackfi.api.core.Datastore;
import java.util.Collection;
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
    return new Datastore<>() {
      @Override
      public Transaction create(WriteTransaction object, String userId) {
        TransactionEntity e = transactionRepository.save(
          new TransactionEntity(null, userId, object.vendor(), object.amount(),
            object.date()));

        return new Transaction(e.id(), e.userId(), e.vendor(), e.amount(), e.date());
      }

      @Override
      public Collection<Transaction> list(String userId) {
        return transactionRepository.findByUserId(userId).stream()
          .map(transactionEntity -> new Transaction(transactionEntity.id(), transactionEntity.userId(), transactionEntity.vendor(), transactionEntity.amount(), transactionEntity.date()))
          .toList();
      }
    };
  }

}
