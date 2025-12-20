package dev.cascadiatech.trackfi.rule;

import dev.cascadiatech.trackfi.core.Datastore;
import dev.cascadiatech.trackfi.core.DatastoreFactory;
import dev.cascadiatech.trackfi.core.FieldDataIntegrityException;
import dev.cascadiatech.trackfi.core.PageParameters;
import dev.cascadiatech.trackfi.core.PageView;
import dev.cascadiatech.trackfi.core.UnknownDataIntegrityException;
import java.util.function.BiFunction;
import org.apache.commons.lang3.tuple.Pair;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.domain.Specification;

/**
 * Beans supporting rule functionality
 */
@Configuration
class RuleConfig {

  /**
   * Create instance of {@link Datastore} for managing {@link RuleView}
   * @param repository {@link dev.cascadiatech.trackfi.core.BaseRepository} for managing rules with JPA
   * @return {@link Datastore} for managing rules within application components
   */
  @Bean
  Datastore<Integer, WriteRuleView, RuleView, PageParameters> ruleDatastore(RuleRepository repository) {
    return DatastoreFactory.create(
      repository,
      ruleEntity -> new RuleView(ruleEntity.getId(), ruleEntity.getCategoryId(), ruleEntity.getVendor()),
      (writeRule, userId) -> new RuleEntity(null, userId, false, writeRule.categoryId(), writeRule.vendor()),
      violation -> {
        if (violation.getCause() instanceof ConstraintViolationException constraintViolationException) {
          if ("RULE_CATEGORY_FK".equals(constraintViolationException.getConstraintName())) {
            return new FieldDataIntegrityException("categoryId", "category not found");
          }
        }
        return new UnknownDataIntegrityException();
      },
      (p) -> Specification.unrestricted()
    );
  }

  /**
   * Creates function for searching for rules outside of this package by userId and page parameters. Returns a paginated result of items, each item containing a rule's categoryId and vendor fields.
   * @param ruleDatastore {@link Datastore} for managing rules storage
   * @return rules search function
   */
  @Bean
  BiFunction<PageParameters, String, PageView<Pair<Integer, String>>> externalSearchRules(
    Datastore<Integer, WriteRuleView, RuleView, PageParameters> ruleDatastore) {
    return (parameters, userId) -> ruleDatastore.list(parameters, userId).map(
      ruleView -> Pair.of(ruleView.categoryId(), ruleView.vendor())
    );
  }

}
