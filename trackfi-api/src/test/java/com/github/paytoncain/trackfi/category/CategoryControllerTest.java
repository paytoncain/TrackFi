package com.github.paytoncain.trackfi.category;

import com.github.paytoncain.trackfi.core.CRDControllerTest;
import com.github.paytoncain.trackfi.core.PageParameters;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;

/**
 * Tests security features and functionality of {@link CategoryController}
 */
@WebMvcTest({CategoryController.class})
class CategoryControllerTest extends CRDControllerTest<WriteCategoryView, CategoryView<Integer>, PageParameters> {

  @Override
  protected String endpoint() {
    return "categories";
  }

  @Override
  protected WriteCategoryView createWriteView() {
    return new WriteCategoryView("name");
  }

  @Override
  protected CategoryView<Integer> createOutputView() {
    return new CategoryView<>(1, "name");
  }

  @Test
  @WithMockUser
  void invalidCategory() throws Exception {
    createBadObject(
      Map.of(),
      """
        {fieldErrors: {name: ['must not be blank']}}
        """
    );
    createBadObject(
      Map.of("name", ""),
      """
        {fieldErrors: {name: ['must not be blank']}}
        """
    );
  }
}