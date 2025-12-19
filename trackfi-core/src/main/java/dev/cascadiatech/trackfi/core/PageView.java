package dev.cascadiatech.trackfi.core;

import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

/**
 * Item holding paginated search results
 * @param <T> internal item type
 */
@Builder
@Getter
public class PageView<T> {

  /**
   * Current page
   */
  @NonNull
  private final Integer page;

  /**
   * Requested items per page
   */
  @NonNull
  private final Integer itemsPerPage;

  /**
   * Total items across all pages
   */
  @NonNull
  private final Long totalItems;

  /**
   * Total pages matching specified criteria
   */
  @NonNull
  private final Integer totalPages;

  /**
   * Current page content
   */
  @NonNull
  private final List<T> items;

}
