package dev.cascadiatech.trackfi.api.transaction;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Implementation of {@link JpaRepository} for managing {@link TransactionEntity}
 */
interface TransactionRepository extends JpaRepository<TransactionEntity, Integer> {

}
