package com.github.paytoncain.trackfi.rule;

import com.github.paytoncain.trackfi.core.CRDControllerTest;
import com.github.paytoncain.trackfi.core.PageParameters;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;

/**
 * Tests security features and functionality of {@link RuleController}
 */
@WebMvcTest(RuleController.class)
class RuleControllerTest extends CRDControllerTest<WriteRuleView<Integer>, RuleView<Integer, Integer>, PageParameters> {

  @Override
  protected String endpoint() {
    return "rules";
  }

  @Override
  protected WriteRuleView<Integer> createWriteView() {
    return new WriteRuleView<>(1, "vendor");
  }

  @Override
  protected RuleView<Integer, Integer> createOutputView() {
    return new RuleView<>(1, 2, "vendor");
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