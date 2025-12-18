package dev.cascadiatech.trackfi.rule;

import dev.cascadiatech.trackfi.core.CRDControllerTest;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;

/**
 * Tests security features and functionality of {@link RuleController}
 */
@WebMvcTest(RuleController.class)
class RuleControllerTest extends CRDControllerTest<Integer, WriteRule, Rule> {

  @Override
  protected String endpoint() {
    return "rules";
  }

  @Override
  protected WriteRule createWriteView() {
    return new WriteRule(1, "vendorRegex");
  }

  @Override
  protected Rule createOutputView() {
    return new Rule(1, 2, "vendorRegex");
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