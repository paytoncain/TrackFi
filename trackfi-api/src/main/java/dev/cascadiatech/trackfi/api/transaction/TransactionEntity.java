package dev.cascadiatech.trackfi.api.transaction;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Implementation of {@link BaseTransaction} compatible with JPA
 */
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
class TransactionEntity implements BaseTransaction {

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

  @Override
  public Integer id() {
    return id;
  }

  @Override
  public String userId() {
    return userId;
  }

  @Override
  public String vendor() {
    return vendor;
  }

  @Override
  public Float amount() {
    return amount;
  }

  @Override
  public LocalDate date() {
    return date;
  }
}
