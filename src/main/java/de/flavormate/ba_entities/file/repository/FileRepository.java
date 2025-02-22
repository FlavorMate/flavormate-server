/* Licensed under AGPLv3 2024 */
package de.flavormate.ba_entities.file.repository;

import de.flavormate.ba_entities.file.model.File;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FileRepository extends JpaRepository<File, Long> {
  @Query("select f from File f where f.id = :id and f.owner = :owner")
  Optional<File> findById(@Param("id") Long id, @Param("owner") Long owner);
}
