package dev.cascadiatech.trackfi.core;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Object for specifying request page parameters (page / items per page).
 * Extensions of this class may also include search parameter for filter results.
 */
@SuperBuilder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PageParameters {

  /**
   * Page to request (1-indexed)
   */
  @Min(1)
  @Default
  private final Integer page = 1;

  /**
   * Page size to request (100 items / 10 items per page = 10 pages of results)
   */
  @Min(1)
  @Max(200)
  @Default
  private final Integer itemPerPage = 10;

}
