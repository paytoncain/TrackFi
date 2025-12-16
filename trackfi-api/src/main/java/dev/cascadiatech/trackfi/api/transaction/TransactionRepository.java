package dev.cascadiatech.trackfi.api.transaction;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Implementation of {@link JpaRepository} for managing {@link TransactionEntity}
 */
interface TransactionRepository extends JpaRepository<TransactionEntity, Integer> {
  List<TransactionEntity> findByUserId(String userId);
}
