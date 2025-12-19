package dev.cascadiatech.trackfi.transaction;

import dev.cascadiatech.trackfi.core.PageParameters;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Extension of {@link PageParameters} for paging and filtering transactions
 */
@SuperBuilder
@Getter
@AllArgsConstructor
@NoArgsConstructor
class TransactionSearchParameters extends PageParameters {

  /**
   * Regular expression filter for 'vendor' field
   */
  private String vendorRegex;

  /**
   * Creates a {@link TransactionSearchParameters}
   * @param page requested page
   * @param itemPerPage requested items per page
   * @param vendorRegex regular expression for filtering by transaction 'vendor'
   */
  public TransactionSearchParameters(Integer page, Integer itemPerPage, String vendorRegex) {
    super(page, itemPerPage);
    this.vendorRegex = vendorRegex;
  }


}
