package dev.cascadiatech.trackfi.category;

import dev.cascadiatech.trackfi.core.CRDController;
import dev.cascadiatech.trackfi.core.Datastore;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST endpoints for managing categories
 */
@RestController
@RequestMapping("/api/v1/categories")
class CategoryController extends CRDController<Integer, WriteCategory, Category> {

  /**
   * Creates a {@link CategoryController}
   * @param datastore {@link Datastore} for managing categories
   */
  protected CategoryController(Datastore<Integer, WriteCategory, Category> datastore) {
    super(datastore);
  }

}
