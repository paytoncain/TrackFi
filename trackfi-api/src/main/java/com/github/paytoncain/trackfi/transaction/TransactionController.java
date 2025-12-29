package com.github.paytoncain.trackfi.transaction;

import com.github.paytoncain.trackfi.core.CRDController;
import com.github.paytoncain.trackfi.core.Datastore;
import java.util.function.Consumer;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST endpoints for managing transactions
 */
@RestController
@RequestMapping("/api/v1/transactions")
class TransactionController extends CRDController<WriteTransactionView<Integer>, TransactionView<Integer, Integer>, TransactionSearchParameters> {

  private final Consumer<String> ruleApplicationService;

  /**
     * Creates a {@link TransactionController}
     * @param datastore {@link Datastore} for managing transactions
     * @param ruleApplicationService {@link RuleApplicationService} for applying categoryIds to transactions via rules
     */
  protected TransactionController(
    Datastore<WriteTransactionView<Integer>, TransactionView<Integer, Integer>, TransactionSearchParameters> datastore,
    Consumer<String> ruleApplicationService
  ) {
    super(datastore);
    this.ruleApplicationService = ruleApplicationService;
  }

  /**
   * Applies category ids to transactions via rules
   * @param authentication {@link Authentication} containing principal for indicating user object ownership
   */
  @PatchMapping(path = "/apply-rules")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void applyRules(Authentication authentication) {
    ruleApplicationService.accept(CRDController.getUserId(authentication));
  }

}
