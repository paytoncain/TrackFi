package com.github.paytoncain.trackfi.category;

import com.github.paytoncain.trackfi.core.CRDController;
import com.github.paytoncain.trackfi.core.Datastore;
import com.github.paytoncain.trackfi.core.PageParameters;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST endpoints for managing categories
 */
@RestController
@RequestMapping("/api/v1/categories")
class CategoryController extends CRDController<WriteCategoryView, CategoryView<Integer>, PageParameters> {

  /**
   * Creates a {@link CategoryController}
   * @param datastore {@link Datastore} for managing categories
   */
  protected CategoryController(Datastore<WriteCategoryView, CategoryView<Integer>, PageParameters> datastore) {
    super(datastore);
  }

}
