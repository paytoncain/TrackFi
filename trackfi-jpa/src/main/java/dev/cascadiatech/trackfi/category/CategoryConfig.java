package dev.cascadiatech.trackfi.category;

import dev.cascadiatech.trackfi.core.Datastore;
import dev.cascadiatech.trackfi.core.DatastoreFactory;
import dev.cascadiatech.trackfi.core.PageParameters;
import dev.cascadiatech.trackfi.core.UnknownDataIntegrityException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.domain.Specification;

/**
 * Beans supporting categories functionality
 */
@Configuration
class CategoryConfig {

  /**
   * Create instance of {@link Datastore} for managing {@link CategoryView}
   * @param categoryRepository {@link dev.cascadiatech.trackfi.core.BaseRepository} for managing categories with JPA
   * @return {@link Datastore} for managing categories within application components
   */
  @Bean
  Datastore<Integer, WriteCategoryView, CategoryView, PageParameters> categoryDatastore(CategoryRepository categoryRepository) {
    return DatastoreFactory.create(
      categoryRepository,
      categoryEntity -> new CategoryView(categoryEntity.getId(), categoryEntity.getName()),
      (writeCategory, userId) -> new CategoryEntity(null, userId, false, writeCategory.name()),
      violation -> new UnknownDataIntegrityException(),
      (p) -> Specification.unrestricted()
    );
  }

}
