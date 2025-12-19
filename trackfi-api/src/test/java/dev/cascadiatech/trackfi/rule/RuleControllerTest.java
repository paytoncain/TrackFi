package dev.cascadiatech.trackfi.rule;

import dev.cascadiatech.trackfi.core.CRDControllerTest;
import dev.cascadiatech.trackfi.core.PageParameters;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;

/**
 * Tests security features and functionality of {@link RuleController}
 */
@WebMvcTest(RuleController.class)
class RuleControllerTest extends CRDControllerTest<Integer, WriteRuleView, RuleView, PageParameters> {

  @Override
  protected String endpoint() {
    return "rules";
  }

  @Override
  protected WriteRuleView createWriteView() {
    return new WriteRuleView(1, "vendor");
  }

  @Override
  protected RuleView createOutputView() {
    return new RuleView(1, 2, "vendor");
  }

  @Test
  @WithMockUser
  void invalidRule() throws Exception {
    createBadObject(
      Map.of(),
      """
        {fieldErrors: {categoryId: ['must not be null'], vendor: ['must not be blank']}}
        """
    );
    createBadObject(
      Map.of(
        "vendor", ""
      ),
      """
        {fieldErrors: {categoryId: ['must not be null'], vendor: ['must not be blank']}}
        """
    );
  }
}