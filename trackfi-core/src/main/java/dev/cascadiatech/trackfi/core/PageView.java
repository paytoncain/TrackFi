package dev.cascadiatech.trackfi.core;

import java.util.List;
import java.util.function.Function;
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

  /**
   * Transforms {@link PageView} internal data type
   * @param transform function for translating page's internal data type to the specified output data type
   * @return new {@link PageView} with desired data type
   * @param <O> output data type
   */
  public <O> PageView<O> map(Function<T, O> transform) {
    return PageView.<O>builder()
      .page(this.page)
      .itemsPerPage(this.itemsPerPage)
      .totalItems(this.totalItems)
      .totalPages(this.totalPages)
      .items(this.items.stream().map(transform).toList())
      .build();
  }

}
