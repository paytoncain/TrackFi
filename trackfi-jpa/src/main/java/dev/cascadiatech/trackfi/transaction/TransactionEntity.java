package dev.cascadiatech.trackfi.transaction;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Transaction model compatible with JPA
 */
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
class TransactionEntity {

  /**
   * transaction id (database primary key)
   */
  @Id
  @GeneratedValue
  private Integer id;

  /**
   * transaction user id (database foreign key)
   */
  private String userId;

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

  /**
   * Transaction deletion status
   */
  private Boolean deleted;
}
