package de.flavormate.ba_entities.book.service;

import com.fasterxml.jackson.databind.JsonNode;
import de.flavormate.aa_interfaces.models.BaseEntity;
import de.flavormate.aa_interfaces.services.BaseService;
import de.flavormate.aa_interfaces.services.ICRUDService;
import de.flavormate.aa_interfaces.services.IPageableService;
import de.flavormate.aa_interfaces.services.ISearchService;
import de.flavormate.ab_exeptions.exceptions.CustomException;
import de.flavormate.ab_exeptions.exceptions.ForbiddenException;
import de.flavormate.ab_exeptions.exceptions.NotFoundException;
import de.flavormate.ba_entities.author.model.Author;
import de.flavormate.ba_entities.author.repository.AuthorRepository;
import de.flavormate.ba_entities.book.model.Book;
import de.flavormate.ba_entities.book.repository.BookRepository;
import de.flavormate.ba_entities.book.wrapper.BookDraft;
import de.flavormate.ba_entities.recipe.model.Recipe;
import de.flavormate.ba_entities.recipe.repository.RecipeRepository;
import de.flavormate.utils.JSONUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService extends BaseService implements ICRUDService<Book, BookDraft>, IPageableService<Book>, ISearchService<Book> {

	private final BookRepository repository;

	private final AuthorRepository authorRepository;

	private final RecipeRepository recipeRepository;

	protected BookService(BookRepository repository, AuthorRepository authorRepository, RecipeRepository recipeRepository) {
		this.repository = repository;
		this.authorRepository = authorRepository;
		this.recipeRepository = recipeRepository;
	}

	@Override
	public Book create(BookDraft object) throws CustomException {
		var author = authorRepository.findByAccountUsername(getPrincipal().getUsername())
				.orElseThrow(() -> new NotFoundException(Author.class));

		var book = Book.builder().label(object.label()).owner(author).visible(false).build();

		book = repository.save(book);

		author.getBooks().add(book);

		authorRepository.save(author);

		return book;
	}

	@Override
	public Book update(Long id, JsonNode json) throws CustomException {
		var book = this.findById(id);

		if (json.has("label")) {
			var label = JSONUtils.parseObject(json.get("label"), String.class);
			book.setLabel(label);
		}

		if (json.has("visible")) {
			var visible = JSONUtils.parseObject(json.get("visible"), Boolean.class);
			book.setVisible(visible);
		}

		return repository.save(book);
	}

	@Override
	public boolean deleteById(Long id) throws CustomException {
		var book = findById(id);

		var authors = authorRepository.findAllByBookIds(List.of((id)));

		for (var author : authors) {
			author.getBooks().removeIf(b -> b.getId().equals(id));
			authorRepository.save(author);
		}


		for (var recipe : book.getRecipes()) {
			recipe.toggleBook(book);
			recipeRepository.save(recipe);
		}

		repository.deleteById(id);

		return true;
	}

	@Override
	public Book findById(Long id) throws CustomException {
		return repository.findById(id, getPrincipal().getUsername()).orElseThrow(() -> new NotFoundException(Book.class));
	}

	@Override
	public List<Book> findAll() throws CustomException {
		return repository.findAll(getPrincipal().getUsername());
	}

	@Override
	public Page<Book> findByPage(Pageable pageable) throws CustomException {
		var author = authorRepository.findByAccountUsername(getPrincipal().getUsername())
				.orElseThrow(() -> new NotFoundException(Author.class));

		var ids = author.getBooks().stream().map(BaseEntity::getId).toList();

		return repository.findByIds(ids, pageable);
	}


	@Override
	public Page<Book> findBySearch(String searchTerm, Pageable pageable) {
		return repository.findBySearch(searchTerm, getPrincipal().getUsername(), pageable);
	}

	public Page<Recipe> findRecipesFromParent(Long id, Pageable pageable) throws CustomException {
		if (findById(id).getVisible())
			return recipeRepository.findByBook(id, pageable);
		else
			return recipeRepository.findByBookPrivate(id, getPrincipal().getUsername(), pageable);
	}

	public Book toggleRecipe(Long bookId, Long recipeId) throws CustomException {
		var book = findById(bookId);

		var recipe = recipeRepository.findById(recipeId)
				.orElseThrow(() -> new NotFoundException(Recipe.class));

		book.toggleRecipe(recipe);

		recipe.toggleBook(book);

		recipeRepository.save(recipe);

		return repository.save(book);

	}

	public Boolean toggleSubscription(Long id) throws NotFoundException, ForbiddenException {
		var author = authorRepository.findByAccountUsername(getPrincipal().getUsername())
				.orElseThrow(() -> new NotFoundException(Author.class));

		var book = repository.findById(id)
				.orElseThrow(() -> new NotFoundException(Book.class));

		var isPublic = !book.getOwner().getId().equals(author.getId()) && book.getVisible();

		if (!isPublic) {
			throw new ForbiddenException(Book.class);
		}

		var bookIsPresent =
				author.getSubscribedBooks().removeIf(b -> b.getId().equals(book.getId()));

		if (!bookIsPresent)
			author.getSubscribedBooks().add(book);

		authorRepository.save(author);

		return true;
	}

	public Boolean subscribed(Long id) throws NotFoundException {
		var author = authorRepository.findByAccountUsername(getPrincipal().getUsername())
				.orElseThrow(() -> new NotFoundException(Author.class));

		return author.getSubscribedBooks().stream().anyMatch(book -> book.getId().equals(id)
				&& !book.getOwner().getAccount().getUsername().equals(getPrincipal().getUsername()));
	}


}
