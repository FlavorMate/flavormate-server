package de.flavormate.ba_entities.book.repository;

import de.flavormate.ba_entities.book.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {

	@Query("select b from Book b where b.id in :ids")
	Page<Book> findByIds(@Param("ids") List<Long> ids, Pageable pageable);

	@Query("select b from Book b where b.id in :ids")
	List<Book> findByIds(@Param("ids") List<Long> ids);

	@Query("select b from Book b where b.id = :id and (b.visible or b.owner.account.username = :username)")
	Optional<Book> findById(@Param("id") Long id, @Param("username") String username);

	@Query("select b from Book b where b.visible or b.owner.account.username = :username")
	List<Book> findAll(@Param("username") String username);

	@Query("select b from Book b where lower(b.label) like lower(concat('%', :searchTerm, '%')) and (b.visible or b.owner.account.username = :username)")
	Page<Book> findBySearch(@Param("searchTerm") String searchTerm, @Param("username") String username, Pageable pageable);
}
