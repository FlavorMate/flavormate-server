package de.flavormate.ba_entities.author.repository;

import de.flavormate.ba_entities.author.model.Author;
import de.flavormate.ba_entities.recipe.model.Recipe;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AuthorRepository extends JpaRepository<Author, Long> {
	Optional<Author> findByAccountId(Long id);

	@Query("select a from Author a join a.books b where b.id in :ids")
	List<Author> findAllByBookIds(@Param("ids") List<Long> ids);

	Optional<Author> findByAccountUsername(String username);

	@Query("select r from Author a join a.recipes r where a.id = :id")
	Page<Recipe> findRecipesFromParent(@Param("id") Long id, Pageable pageable);

	@Query("select a from Author a where lower(a.account.displayName) like lower(concat('%', :searchTerm, '%'))")
	Page<Author> findBySearch(@Param("searchTerm") String searchTerm, Pageable pageable);
}
