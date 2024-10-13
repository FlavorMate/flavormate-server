package de.flavormate.ba_entities.book.controller;

import com.fasterxml.jackson.databind.JsonNode;
import de.flavormate.aa_interfaces.controllers.ICRUDController;
import de.flavormate.aa_interfaces.controllers.IExtractRecipesController;
import de.flavormate.aa_interfaces.controllers.IPageableController;
import de.flavormate.aa_interfaces.controllers.ISearchController;
import de.flavormate.ab_exeptions.exceptions.CustomException;
import de.flavormate.ab_exeptions.exceptions.ForbiddenException;
import de.flavormate.ab_exeptions.exceptions.NotFoundException;
import de.flavormate.ba_entities.book.model.Book;
import de.flavormate.ba_entities.book.service.BookService;
import de.flavormate.ba_entities.book.wrapper.BookDraft;
import de.flavormate.ba_entities.recipe.model.Recipe;
import de.flavormate.utils.RequestUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v2/books")
public class BookController implements ICRUDController<Book, BookDraft>, IExtractRecipesController, IPageableController<Book>, ISearchController<Book> {

	private final BookService bookService;

	@Override
	public Book create(BookDraft form) throws CustomException {
		return bookService.create(form);
	}

	@Override
	public Book update(Long id, JsonNode form) throws CustomException {
		return bookService.update(id, form);
	}

	@Override
	public boolean deleteById(Long id) throws CustomException {
		return bookService.deleteById(id);
	}

	@Override
	public Book findById(Long id) throws CustomException {
		return bookService.findById(id);
	}

	@Override
	public List<Book> findAll() throws CustomException {
		return bookService.findAll();
	}

	@Override
	public Page<Recipe> findRecipesFromParent(Long id, int page, int size, String sortBy, String sortDirection) throws CustomException {
		var pageable = RequestUtils.convertPageable(page, size, sortBy, sortDirection);
		return bookService.findRecipesFromParent(id, pageable);
	}

	@Override
	public Page<Book> findByPage(int page, int size, String sortBy, String sortDirection) throws CustomException {
		var pageable = RequestUtils.convertPageable(page, size, sortBy, sortDirection);
		return bookService.findByPage(pageable);
	}

	@Override
	public Page<Book> findBySearch(String searchTerm, int page, int size, String sortBy, String sortDirection) {
		var pageable = RequestUtils.convertPageable(page, size, sortBy, sortDirection);

		return bookService.findBySearch(searchTerm, pageable);
	}

	@PostMapping("/{bookId}/toggle/{recipeId}")
	public Book toggleRecipeInBook(@PathVariable Long bookId, @PathVariable Long recipeId) throws CustomException {
		return bookService.toggleRecipe(bookId, recipeId);

	}

	@PutMapping("/{id}/subscribe")
	public Boolean toggleSubscription(@PathVariable Long id)
			throws NotFoundException, ForbiddenException {
		return bookService.toggleSubscription(id);
	}


	@GetMapping("/subscribed/{id}")
	public Boolean isSubscribed(@PathVariable Long id) throws NotFoundException {
		return bookService.subscribed(id);
	}

	@GetMapping("/own")
	public List<Book> findOwn() throws CustomException {
		return bookService.findOwn();
	}

}
