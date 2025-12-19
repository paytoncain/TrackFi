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
    return new WriteRuleView(1, "vendorRegex");
  }

  @Override
  protected RuleView createOutputView() {
    return new RuleView(1, 2, "vendorRegex");
  }

  @Test
  @WithMockUser
  void invalidRule() throws Exception {
    createBadObject(
      Map.of(),
      """
        {fieldErrors: {categoryId: ['must not be null'], vendorRegex: ['must not be blank']}}
        """
    );
    createBadObject(
      Map.of(
        "vendorRegex", ""
      ),
      """
        {fieldErrors: {categoryId: ['must not be null'], vendorRegex: ['must not be blank']}}
        """
    );
  }
}