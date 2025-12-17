package dev.cascadiatech.trackfi.core;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * General base class for JPA entities
 * @param <ID> object id type
 */
@MappedSuperclass
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BaseEntity<ID> {

  /**
   * object id (database primary key)
   */
  @Id
  @GeneratedValue
  private ID id;

  /**
   * object user id (database foreign key)
   */
  private String userId;

  /**
   * object deletion status
   */
  private Boolean deleted;

}
