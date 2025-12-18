package dev.cascadiatech.trackfi.transaction;

import dev.cascadiatech.trackfi.core.BaseEntity;
import jakarta.persistence.Entity;
import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Transaction model compatible with JPA
 */
@Entity
@NoArgsConstructor
@Getter
@Setter
class TransactionEntity extends BaseEntity<Integer> {

  /**
   * Creates {@link TransactionEntity}
   * @param integer transaction id
   * @param userId transaction user id
   * @param deleted transaction deletion status
   * @param vendor transaction vendor name
   * @param amount transaction amount
   * @param date transaction date
   */
  TransactionEntity(Integer integer, String userId, Boolean deleted, Integer categoryId, String vendor, Float amount, LocalDate date) {
    super(integer, userId, deleted);
    this.categoryId = categoryId;
    this.vendor = vendor;
    this.amount = amount;
    this.date = date;
  }

  /**
   * Transaction category id (database foreign key)
   */
  private Integer categoryId;

  /**
   * Transaction vendor name
   */
  private String vendor;

  /**
   * Transaction amount
   */
  private Float amount;

  /**
   * Transaction date
   */
  private LocalDate date;

}
