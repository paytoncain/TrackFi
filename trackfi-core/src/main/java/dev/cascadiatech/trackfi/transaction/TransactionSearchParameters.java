package dev.cascadiatech.trackfi.transaction;

import dev.cascadiatech.trackfi.core.PageParameters;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Extension of {@link PageParameters} for paging and filtering transactions
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
class TransactionSearchParameters extends PageParameters {

  /**
   * filter for 'vendor' field
   */
  private String vendor;

  /**
   * Creates a {@link TransactionSearchParameters}
   * @param page requested page
   * @param itemPerPage requested items per page
   * @param vendor pattern for filtering by transaction 'vendor'
   */
  public TransactionSearchParameters(Integer page, Integer itemPerPage, String vendor) {
    super(page, itemPerPage);
    this.vendor = vendor;
  }


}
