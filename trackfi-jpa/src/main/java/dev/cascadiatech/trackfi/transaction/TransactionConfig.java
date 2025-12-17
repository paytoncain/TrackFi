package dev.cascadiatech.trackfi.transaction;

import dev.cascadiatech.trackfi.core.Datastore;
import dev.cascadiatech.trackfi.core.NotFoundException;
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
        TransactionEntity transactionEntity = transactionRepository.save(new TransactionEntity(null, userId, object.vendor(), object.amount(), object.date(), false));

        return transactionFromTransactionEntity(transactionEntity);
      }

      @Override
      public Transaction get(Integer id, String userId) throws NotFoundException {
        TransactionEntity transactionEntity = getTransactionEntity(id, userId);
        return transactionFromTransactionEntity(transactionEntity);
      }

      @Override
      public Collection<Transaction> list(String userId) {
        return transactionRepository.findByUserIdAndDeleted(userId, false).stream()
          .map(this::transactionFromTransactionEntity)
          .toList();
      }

      @Override
      public void delete(Integer id, String userId) throws NotFoundException {
        TransactionEntity transactionEntity = getTransactionEntity(id, userId);
        transactionEntity.setDeleted(true);
        transactionRepository.save(transactionEntity);
      }

      private TransactionEntity getTransactionEntity(Integer id, String userId) throws NotFoundException {
        Optional<TransactionEntity> maybeTransaction = transactionRepository.getByIdAndUserIdAndDeleted(id, userId, false);
        if (maybeTransaction.isEmpty()) {
          throw new NotFoundException("transaction with id=%s not found");
        }

        return maybeTransaction.get();
      }

      private Transaction transactionFromTransactionEntity(TransactionEntity transactionEntity) {
        return new Transaction(transactionEntity.getId(), transactionEntity.getUserId(), transactionEntity.getVendor(), transactionEntity.getAmount(), transactionEntity.getDate());
      }
    };
  }

}
