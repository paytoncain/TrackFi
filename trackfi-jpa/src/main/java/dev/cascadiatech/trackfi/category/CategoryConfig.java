package dev.cascadiatech.trackfi.category;

import dev.cascadiatech.trackfi.core.Datastore;
import dev.cascadiatech.trackfi.core.DatastoreFactory;
import dev.cascadiatech.trackfi.core.UnknownDataIntegrityException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Beans supporting categories functionality
 */
@Configuration
class CategoryConfig {

  /**
   * Create instance of {@link Datastore} for managing {@link Category}
   * @param categoryRepository {@link org.springframework.data.jpa.repository.JpaRepository} for managing categories with JPA
   * @return {@link Datastore} for managing categories within application components
   */
  @Bean
  Datastore<Integer, WriteCategory, Category> categoryDatastore(CategoryRepository categoryRepository) {
    return DatastoreFactory.create(
      categoryRepository,
      categoryEntity -> new Category(categoryEntity.getId(), categoryEntity.getName()),
      (writeCategory, userId) -> new CategoryEntity(null, userId, false, writeCategory.name()),
      violation -> new UnknownDataIntegrityException()
    );
  }

}
