package dev.cascadiatech.trackfi.rule;

import dev.cascadiatech.trackfi.core.Datastore;
import dev.cascadiatech.trackfi.core.DatastoreFactory;
import dev.cascadiatech.trackfi.core.FieldDataIntegrityException;
import dev.cascadiatech.trackfi.core.PageParameters;
import dev.cascadiatech.trackfi.core.UnknownDataIntegrityException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
      ruleEntity -> new RuleView(ruleEntity.getId(), ruleEntity.getCategoryId(), ruleEntity.getVendorRegex()),
      (writeRule, userId) -> new RuleEntity(null, userId, false, writeRule.categoryId(), writeRule.vendorRegex()),
      violation -> {
        if (violation.getCause() instanceof ConstraintViolationException constraintViolationException) {
          if ("RULE_CATEGORY_FK".equals(constraintViolationException.getConstraintName())) {
            return new FieldDataIntegrityException("categoryId", "category not found");
          }
        }
        return new UnknownDataIntegrityException();
      }
    );
  }

}
