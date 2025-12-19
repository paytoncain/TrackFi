package dev.cascadiatech.trackfi.core;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Base interface extending {@link JpaRepository} for managing extensions of {@link BaseEntity}
 * @param <ID> object id type
 * @param <T> object type
 */
@NoRepositoryBean // required so that this interface is not autoconfigured on application start
public interface BaseRepository<ID, T extends BaseEntity<ID>> extends JpaRepository<T, ID>, PagingAndSortingRepository<T, ID> {

  /**
   * Get paginated objects belonging to user
   * @param userId unique user identifier
   * @param deleted object deletion status
   * @return objects belonging to user
   */
  Page<T> findAllByUserIdAndDeleted(String userId, Boolean deleted, Pageable pageable);

  /**
   * Get object belonging to user
   * @param id object id
   * @param userId unique user identifier
   * @param deleted object deletion status
   * @return object belonging to user
   */
  Optional<T> getByIdAndUserIdAndDeleted(ID id, String userId, Boolean deleted);
}
