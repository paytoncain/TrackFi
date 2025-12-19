package dev.cascadiatech.trackfi.rule;

import dev.cascadiatech.trackfi.core.CRDController;
import dev.cascadiatech.trackfi.core.Datastore;
import dev.cascadiatech.trackfi.core.PageParameters;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST endpoints for managing transactions
 */
@RestController
@RequestMapping("/api/v1/rules")
class RuleController extends CRDController<Integer, WriteRuleView, RuleView, PageParameters> {

  /**
   * Creates a {@link RuleController}
   *
   * @param datastore {@link Datastore} for managing rules
   */
  protected RuleController(
    Datastore<Integer, WriteRuleView, RuleView, PageParameters> datastore) {
    super(datastore);
  }
}
