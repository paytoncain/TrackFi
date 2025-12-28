package com.github.paytoncain.trackfi.category;

import com.github.paytoncain.trackfi.core.BaseEntity;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Category model compatible with JPA
 */
@Entity
@NoArgsConstructor
@Getter
@Setter
class CategoryEntity extends BaseEntity<Integer> {

  /**
   * Creates {@link CategoryEntity}
   * @param integer category id
   * @param userId category user id
   * @param deleted category deletion status
   * @param name category name
   */
  CategoryEntity(Integer integer, String userId, Boolean deleted, String name) {
    super(integer, userId, deleted);
    this.name = name;
  }

  /**
   * category name
   */
  private String name;

}
