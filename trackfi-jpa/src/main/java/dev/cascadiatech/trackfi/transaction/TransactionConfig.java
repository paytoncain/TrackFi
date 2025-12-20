package dev.cascadiatech.trackfi.transaction;

import dev.cascadiatech.trackfi.core.Datastore;
import dev.cascadiatech.trackfi.core.DatastoreFactory;
import dev.cascadiatech.trackfi.core.FieldDataIntegrityException;
import dev.cascadiatech.trackfi.core.PageParameters;
import dev.cascadiatech.trackfi.core.PageView;
import dev.cascadiatech.trackfi.core.UnknownDataIntegrityException;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import org.apache.commons.lang3.tuple.Pair;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.domain.Specification;

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
  Datastore<Integer, WriteTransactionView, TransactionView, TransactionSearchParameters> transactionDatastore(TransactionRepository transactionRepository) {
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
      },
      (p) -> {
        Specification<TransactionEntity> specification = Specification.unrestricted();

        String vendor = p.getVendor();

        if (vendor == null || vendor.isEmpty()) {
          return specification;
        }

        return specification.and((root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("vendor"), vendor));
      }
    );
  }

  /**
   * Create instance of {@link RuleApplicationService} for applying categoryIds to transactions
   * @param transactionDatastore {@link Datastore} for managing transactions
   * @param externalSearchRules function searching for rules by page parameter and user id. returns paginated items, each containing categoryId and rule vendor pattern
   * @param transactionRepository {@link TransactionRepository} for managing {@link TransactionEntity} with JPA (should only be used for updates)
   * @return {@link RuleApplicationService} for rule application in application components
   */
  @Bean
  Consumer<String> ruleApplicationService(
    Datastore<Integer, WriteTransactionView, TransactionView, TransactionSearchParameters> transactionDatastore,
    BiFunction<PageParameters, String, PageView<Pair<Integer, String>>> externalSearchRules,
    TransactionRepository transactionRepository
  ) {
    return new RuleApplicationService(
      externalSearchRules,
      transactionDatastore::list,
      (transactionWithUserId) -> {
        String userId = transactionWithUserId.getLeft();
        TransactionView view = transactionWithUserId.getRight();
        transactionRepository.save(new TransactionEntity(view.id(), userId, false, view.categoryId(), view.vendor(), view.amount(), view.date()));
      }
    );
  }

}
