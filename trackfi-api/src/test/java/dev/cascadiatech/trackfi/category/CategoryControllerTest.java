package dev.cascadiatech.trackfi.category;

import dev.cascadiatech.trackfi.core.CRDControllerTest;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;

/**
 * Tests security features and functionality of {@link CategoryController}
 */
@WebMvcTest({CategoryController.class})
class CategoryControllerTest extends CRDControllerTest<Integer, WriteCategory, Category> {

  @Override
  protected String endpoint() {
    return "categories";
  }

  @Override
  protected WriteCategory createWriteView() {
    return new WriteCategory("name");
  }

  @Override
  protected Category createOutputView() {
    return new Category(1, "name");
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