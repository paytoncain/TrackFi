package dev.cascadiatech.trackfi.api.transaction;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Implementation of {@link JpaRepository} for managing {@link TransactionEntity}
 */
interface TransactionRepository extends JpaRepository<TransactionEntity, Integer> {
  List<TransactionEntity> findByUserIdAndDeleted(String userId, Boolean deleted);
  Optional<TransactionEntity> getByIdAndUserIdAndDeleted(Integer id, String userId, Boolean deleted);
}
