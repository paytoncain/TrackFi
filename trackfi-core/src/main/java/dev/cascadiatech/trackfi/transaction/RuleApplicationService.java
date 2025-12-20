package dev.cascadiatech.trackfi.transaction;

import dev.cascadiatech.trackfi.core.PageParameters;
import dev.cascadiatech.trackfi.core.PageView;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import org.apache.commons.lang3.tuple.Pair;

/**
 * Service for applying category ids to transactions via rules
 */
class RuleApplicationService implements Consumer<String> {

  private final BiFunction<PageParameters, String, PageView<Pair<Integer, String>>> searchRules;
  private final BiFunction<TransactionSearchParameters, String, PageView<TransactionView>> searchTransactions;
  private final Consumer<Pair<String, TransactionView>> saveTransaction;

  /**
   * Creates a {@link RuleApplicationService}
   * @param searchRules function for searching for rules by page parameters and userId. returns a page of items, each containing a rule categoryId and vendor pattern
   * @param searchTransactions function searching for transactions by page parameters and user id
   * @param saveTransaction function for saving edits to a transaction's categoryId
   */
  RuleApplicationService(
    BiFunction<PageParameters, String, PageView<Pair<Integer, String>>> searchRules,
    BiFunction<TransactionSearchParameters, String, PageView<TransactionView>> searchTransactions,
    Consumer<Pair<String, TransactionView>> saveTransaction
  ) {
    this.searchRules = searchRules;
    this.searchTransactions = searchTransactions;
    this.saveTransaction = saveTransaction;
  }

  /**
   * Apply category ids to transactions via rules
   * @param userId user unique identifier
   */
  @Override
  public void accept(String userId) {
    PageParameters ruleParameters = new PageParameters(1, 200);

    processPages(userId, ruleParameters, searchRules, (rule) -> {
      Integer categoryId = rule.getLeft();
      String vendorPattern = rule.getRight();

      TransactionSearchParameters transactionParameters = new TransactionSearchParameters(1, 200, vendorPattern);
      processPages(userId, transactionParameters, searchTransactions, transactionView ->

        saveTransaction.accept(
          Pair.of(userId, new TransactionView(transactionView.id(), categoryId, transactionView.vendor(), transactionView.amount(), transactionView.date()))
        )
      );
    });
  }

  /**
   * Process all paginated results matching page parameter values
   * @param userId unique user identifier
   * @param pageParameters page parameters (maybe containing search parameters) for filtering results
   * @param search function for retrieving paginated results of {@link T} by userId and page parameters
   * @param processItem function for processing each item in paginated results
   * @param <T> internal paged result type
   * @param <P> page parameters type
   */
  private static <T, P extends PageParameters> void processPages(String userId, P pageParameters, BiFunction<P, String, PageView<T>> search, Consumer<T> processItem) {
    PageView<?> pageView = processPage(userId, pageParameters, search, processItem);
    while (!pageView.getPage().equals(pageView.getTotalPages())) {
      pageParameters.setPage(pageParameters.getPage() + 1);
      pageView = processPage(userId, pageParameters, search, processItem);
    }
  }

  /**
   *
   * @param userId unique user identifier
   * @param pageParameters page parameters (maybe containing search parameters) for filtering results
   * @param search function for retrieving paginated results of {@link T} by userId and page parameters
   * @param processItem function for processing each item in paginated results
   * @return paginated results from 'search' function
   * @param <T> internal paged result type
   * @param <P> page parameters type
   */
  private static <T, P extends PageParameters> PageView<T> processPage(String userId, P pageParameters, BiFunction<P, String, PageView<T>> search, Consumer<T> processItem) {
    PageView<T> pageView = search.apply(pageParameters, userId);
    pageView.getItems().forEach(processItem);
    return pageView;
  }

}
