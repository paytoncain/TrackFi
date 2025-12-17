package dev.cascadiatech.trackfi.category;

import dev.cascadiatech.trackfi.core.BaseRepository;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Implementation of {@link BaseRepository} for managing {@link CategoryEntity}
 */
interface CategoryRepository extends BaseRepository<Integer, CategoryEntity> {
}
