package dev.cascadiatech.trackfi.api.transaction;

import dev.cascadiatech.trackfi.api.core.Datastore;
import dev.cascadiatech.trackfi.api.core.NotFoundException;
import java.util.Collection;
import java.util.Optional;
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
  Datastore<Integer, WriteTransaction, Transaction> transactionDatastore(TransactionRepository transactionRepository) {
    return new Datastore<>() {
      @Override
      public Transaction create(WriteTransaction object, String userId) {
        TransactionEntity e = transactionRepository.save(
          new TransactionEntity(null, userId, object.vendor(), object.amount(),
            object.date(), false));

        return new Transaction(e.id(), e.userId(), e.vendor(), e.amount(), e.date());
      }

      @Override
      public Transaction get(Integer id, String userId) throws NotFoundException {
        return transactionRepository.getByIdAndUserIdAndDeleted(id, userId, false)
          .map(transactionEntity -> new Transaction(transactionEntity.id(), transactionEntity.userId(), transactionEntity.vendor(), transactionEntity.amount(), transactionEntity.date()))
          .orElseThrow(() -> new NotFoundException("transaction with id=%d not found".formatted(id)));
      }

      @Override
      public Collection<Transaction> list(String userId) {
        return transactionRepository.findByUserIdAndDeleted(userId, false).stream()
          .map(transactionEntity -> new Transaction(transactionEntity.id(), transactionEntity.userId(), transactionEntity.vendor(), transactionEntity.amount(), transactionEntity.date()))
          .toList();
      }

      @Override
      public void delete(Integer id, String userId) throws NotFoundException {
        Optional<TransactionEntity> maybeTransaction = transactionRepository.getByIdAndUserIdAndDeleted(id, userId, false);
        if (maybeTransaction.isEmpty()) {
          throw new NotFoundException("transaction with id=%s not found");
        }
        TransactionEntity transactionEntity = maybeTransaction.get();
        transactionEntity.setDeleted(true);
        transactionRepository.save(transactionEntity);
      }
    };
  }

}
