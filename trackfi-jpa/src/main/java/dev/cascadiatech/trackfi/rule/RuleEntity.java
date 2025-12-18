package dev.cascadiatech.trackfi.rule;

import dev.cascadiatech.trackfi.core.BaseEntity;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Rule model compatible with JPA
 */
@Entity
@NoArgsConstructor
@Getter
@Setter
class RuleEntity extends BaseEntity<Integer> {

  /**
   * Creates {@link RuleEntity}
   * @param id rule id
   * @param userId rule user id
   * @param deleted rule deletion status
   * @param categoryId rule category id
   * @param vendorRegex regular expression matching transaction vendors
   */
  RuleEntity(Integer id, String userId, Boolean deleted, Integer categoryId,
    String vendorRegex) {
    super(id, userId, deleted);
    this.categoryId = categoryId;
    this.vendorRegex = vendorRegex;
  }

  /**
   * rule category id (database foreign key)
   */
  private Integer categoryId;

  /**
   * regular expression matching transaction vendors
   */
  private String vendorRegex;

}
