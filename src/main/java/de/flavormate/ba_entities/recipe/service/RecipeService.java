package de.flavormate.ba_entities.recipe.service;

import com.fasterxml.jackson.databind.JsonNode;
import de.flavormate.aa_interfaces.services.BaseService;
import de.flavormate.aa_interfaces.services.ICRUDService;
import de.flavormate.aa_interfaces.services.IPageableDietService;
import de.flavormate.aa_interfaces.services.ISearchDietService;
import de.flavormate.ab_exeptions.exceptions.CustomException;
import de.flavormate.ab_exeptions.exceptions.ForbiddenException;
import de.flavormate.ab_exeptions.exceptions.NotFoundException;
import de.flavormate.ad_configurations.flavormate.PathsConfig;
import de.flavormate.ba_entities.account.model.Account;
import de.flavormate.ba_entities.author.model.Author;
import de.flavormate.ba_entities.author.repository.AuthorRepository;
import de.flavormate.ba_entities.book.repository.BookRepository;
import de.flavormate.ba_entities.category.model.Category;
import de.flavormate.ba_entities.category.repository.CategoryRepository;
import de.flavormate.ba_entities.file.repository.FileRepository;
import de.flavormate.ba_entities.highlight.repository.HighlightRepository;
import de.flavormate.ba_entities.ingredient.model.Ingredient;
import de.flavormate.ba_entities.ingredientGroup.model.IngredientGroup;
import de.flavormate.ba_entities.instruction.model.Instruction;
import de.flavormate.ba_entities.instructionGroup.model.InstructionGroup;
import de.flavormate.ba_entities.nutrition.model.Nutrition;
import de.flavormate.ba_entities.nutrition.repository.NutritionRepository;
import de.flavormate.ba_entities.recipe.enums.RecipeDiet;
import de.flavormate.ba_entities.recipe.model.Recipe;
import de.flavormate.ba_entities.recipe.repository.RecipeRepository;
import de.flavormate.ba_entities.recipe.wrapper.ChangeOwnerForm;
import de.flavormate.ba_entities.recipe.wrapper.RecipeDraft;
import de.flavormate.ba_entities.serving.model.Serving;
import de.flavormate.ba_entities.story.repository.StoryRepository;
import de.flavormate.ba_entities.tag.model.Tag;
import de.flavormate.ba_entities.tag.repository.TagRepository;
import de.flavormate.ba_entities.unit.model.Unit;
import de.flavormate.ba_entities.unit.repository.UnitRepository;
import de.flavormate.utils.JSONUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecipeService extends BaseService implements ICRUDService<Recipe, RecipeDraft>, IPageableDietService<Recipe>, ISearchDietService<Recipe> {

	private final AuthorRepository authorRepository;
	private final BookRepository bookRepository;
	private final CategoryRepository categoryRepository;
	private final FileRepository fileRepository;
	private final HighlightRepository highlightRepository;
	private final RecipeRepository recipeRepository;
	private final StoryRepository storyRepository;
	private final NutritionRepository nutritionRepository;
	private final TagRepository tagRepository;
	private final UnitRepository unitRepository;
	private final PathsConfig pathsConfig;


	@Transactional
	@Override
	public Recipe create(RecipeDraft form) throws CustomException {

		var author = authorRepository.findByAccountUsername(getPrincipal().getUsername())
				.orElseThrow(() -> new NotFoundException(Account.class));

		var categories =
				form.categories().stream().map(c -> categoryRepository.findById(c).orElse(null))
						.filter(Objects::nonNull).toList();

		var ingredientGroups = form.ingredientGroups().stream().map(iG -> {
			var ingredients = iG.ingredients().stream().map(i -> {
				Unit unit = null;
				if (i.unit() != null) {
					unit = unitRepository.findByLabel(i.unit().getLabel()).orElse(null);
					if (unit == null) {
						i.unit().setId(null);
						unit = unitRepository.save(i.unit());
					}
				}

				var nutrition = Nutrition.fromNutritionDraft(i.nutrition());

				return (Ingredient) Ingredient.builder()
						.amount(i.amount()).label(i.label()).unit(unit).unitLocalized(i.unitLocalized()).nutrition(nutrition).build();
			}).toList();
			return (IngredientGroup) IngredientGroup.builder().ingredients(ingredients)
					.label(iG.label()).build();
		}).toList();

		var instructionGroups = form.instructionGroups().stream().map(iG -> {
			var instructions = iG.instructions().stream()
					.map(i -> (Instruction) Instruction.builder().label(i.label()).build())
					.toList();

			return (InstructionGroup) InstructionGroup.builder().instructions(instructions)
					.label(iG.label()).build();
		}).toList();

		var serving = Serving.builder().amount(form.serving().amount())
				.label(form.serving().label()).build();

		var tags = form.tags().stream()
				.map(tag -> tagRepository.findByLabel(tag.label().toLowerCase())
						.orElseGet(() -> tagRepository
								.save(Tag.builder().label(tag.label().toLowerCase()).build())))
				.toList();

		var recipe = Recipe.builder().author(author).categories(categories)
				.cookTime(form.cookTime()).course(form.course()).description(form.description())
				.diet(form.diet()).ingredientGroups(ingredientGroups)
				.instructionGroups(instructionGroups).label(form.label()).prepTime(form.prepTime())
				.rating(0.0).restTime(form.restTime()).serving(serving).tags(tags).url(form.url())
				.build();

		recipe = recipeRepository.save(recipe);

		for (var category : categories) {
			category.addOrRemoveRecipe(recipe);
		}

		for (var tag : tags) {
			tag.addOrRemoveRecipe(recipe);
		}

		return recipe;
	}


	@Transactional
	@Override
	public Recipe update(Long id, JsonNode json) throws CustomException {
		var author = authorRepository.findByAccountUsername(getPrincipal().getUsername()).orElseThrow(() -> new NotFoundException(Account.class));

		var recipe = this.findById(id);

		if (!recipe.getAuthor().getId().equals(author.getId())) {
			throw new ForbiddenException(Account.class);
		}

		var data = JSONUtils.parseObject(json, RecipeDraft.class);

		if (data.categories() != null) {
			var categories = categoryRepository.findAllById(data.categories());

			var categoriesR =
					filterMissingItems(recipe.getCategories(), categories, Category::getId);

			var categoriesA =
					filterMissingItems(categories, recipe.getCategories(), Category::getId);

			Stream.concat(categoriesR.stream(), categoriesA.stream())
					.forEach(category -> category.addOrRemoveRecipe(recipe));
		}

		if (data.tags() != null) {
			var tags = tagRepository.findAllByLabelIn(
					data.tags().stream().map(t -> t.label().toLowerCase()).toList());

			var tagsR = filterMissingItems(recipe.getTags(), tags, Tag::getId);

			var tagsA = filterMissingItems(tags, recipe.getTags(), Tag::getId);

			Stream.concat(tagsR.stream(), tagsA.stream())
					.forEach(tag -> tag.addOrRemoveRecipe(recipe));
		}

		if (data.cookTime() != null) {
			recipe.setCookTime(data.cookTime());
		}

		if (data.course() != null) {
			recipe.setCourse(data.course());
		}

		if (data.description() != null) {
			recipe.setDescription(data.description());
		}

		if (data.diet() != null) {
			recipe.setDiet(data.diet());
		}

		if (data.ingredientGroups() != null) {
			recipe.getIngredientGroups().clear();

			var ingredientGroups = data.ingredientGroups().stream().map(iG -> {
				var ingredients = iG
						.ingredients().stream().map(i -> {
							Unit unit = null;
							if (i.unit() != null) {
								unit = unitRepository.findByLabel(i.unit().getLabel()).orElse(null);
								if (unit == null) {
									i.unit().setId(null);
									unit = unitRepository.save(i.unit());
								}
							}

							var nutrition = Nutrition.fromNutritionDraft(i.nutrition());

							return (Ingredient) Ingredient.builder()
									.amount(i.amount()).label(i.label()).unit(unit).unitLocalized(i.unitLocalized()).nutrition(nutrition).build();
						})
						.toList();
				return (IngredientGroup) IngredientGroup.builder().ingredients(ingredients)
						.label(iG.label()).build();
			}).toList();

			recipe.getIngredientGroups().addAll(ingredientGroups);
		}

		if (data.instructionGroups() != null) {
			recipe.getInstructionGroups().clear();

			var instructionGroups = data.instructionGroups().stream().map(iG -> {
				var instructions = iG.instructions().stream()
						.map(i -> (Instruction) Instruction.builder().label(i.label()).build())
						.toList();

				return (InstructionGroup) InstructionGroup.builder().instructions(instructions)
						.label(iG.label()).build();
			}).toList();

			recipe.getInstructionGroups().addAll(instructionGroups);
		}

		if (data.label() != null) {
			recipe.setLabel(data.label());
		}

		if (data.prepTime() != null) {
			recipe.setPrepTime(data.prepTime());
		}

		if (data.restTime() != null) {
			recipe.setRestTime(data.restTime());
		}

		if (data.serving() != null) {
			var serving = Serving.builder().amount(data.serving().amount())
					.label(data.serving().label()).build();

			recipe.setServing(serving);
		}

		if (data.files() != null) {
			var files = fileRepository.findAllById(data.files());

			recipe.getFiles().clear();

			recipe.getFiles().addAll(files);
		}

		return recipe;
	}


	/**
	 * Deletes a recipe identified by its unique ID and performs necessary clean-up actions.
	 * <p>
	 * This method allows you to delete a recipe and perform associated actions like updating
	 * related books, categories, tags, and deleting associated files, highlights, and stories.
	 *
	 * @param id The unique identifier of the recipe to delete.
	 * @return `true` if the recipe was successfully deleted; otherwise, an exception may be thrown.
	 * @throws CustomException If there is an error during the deletion process, such as if the
	 *                         recipe or related entities are not found, or if there are issues during cleanup.
	 */
	@Override
	public boolean deleteById(Long id) throws CustomException {
		var author = authorRepository.findByAccountUsername(getPrincipal().getUsername()).orElseThrow(() -> new NotFoundException(Account.class));
		var recipe = this.findById(id);

		if (!author.getId().equals(recipe.getAuthor().getId())) {
			throw new ForbiddenException(Account.class);
		}

		for (var book : recipe.getBooks()) {
			book.addOrRemoveRecipe(recipe);
			bookRepository.save(book);
		}

		for (var category : recipe.getCategories()) {
			category.addOrRemoveRecipe(recipe);
			categoryRepository.save(category);
		}

		for (var tag : recipe.getTags()) {
			tag.addOrRemoveRecipe(recipe);
			tagRepository.save(tag);
		}

		for (var file : recipe.getFiles()) {
			try {
				var deleted = Files.deleteIfExists(Paths.get(pathsConfig.content().toExternalForm(), file.getPath()));

				if (deleted) {
					fileRepository.deleteById(id);
				}
			} catch (Exception e) {
				log.error("File not found!", e);
			}
		}

		highlightRepository.deleteAllByRecipeId(recipe.getId());

		storyRepository.deleteAllByRecipeId(recipe.getId());

		recipeRepository.deleteById(id);
		return true;
	}

	@Override
	public Recipe findById(Long id) throws CustomException {
		return recipeRepository.findById(id).orElseThrow(() -> new NotFoundException(Recipe.class));
	}

	@Override
	public List<Recipe> findAll() throws CustomException {
		return recipeRepository.findAll();
	}

	public Recipe findByIdL10n(Long id, String language) throws CustomException {
		var recipe = recipeRepository.findById(id).orElseThrow(() -> new NotFoundException(Recipe.class));
		recipe.translate(language);
		return recipe;
	}

	/**
	 * Changes the owner of a recipe identified by its unique ID and updates the necessary author
	 * information.
	 * <p>
	 * This method allows you to change the owner of a recipe and update the author information for
	 * both the old and new owners.
	 *
	 * @param id   The unique identifier of the recipe to change the owner of.
	 * @param form A JSON object containing the new owner information, with the "owner" field
	 *             specifying the new owner's username.
	 * @return The updated Recipe object with the new owner and author information.
	 * @throws CustomException If there is an error during the owner change process, such as if the recipe
	 *                         or authors are not found.
	 */
	public Boolean changeOwner(Long id, ChangeOwnerForm form) throws CustomException {
		var currentUser = authorRepository.findByAccountUsername(getPrincipal().getUsername())
				.orElseThrow(() -> new NotFoundException(Author.class));

		var recipe = this.findById(id);

		if (!(currentUser.getAccount().hasRole("ROLE_ADMIN")
				|| recipe.getAuthor().getId().equals(currentUser.getId()))) {
			throw new ForbiddenException(Author.class);
		}

		var newOwner = authorRepository.findById(form.owner())
				.orElseThrow(() -> new NotFoundException(Author.class));

		recipe.setAuthor(newOwner);

		recipeRepository.save(recipe);

		return true;
	}


	/**
	 * Filters items that are missing in the source list compared to the target list.
	 *
	 * @param <T>         The type of items in the lists.
	 * @param source      The source list of items.
	 * @param target      The target list to compare against.
	 * @param idExtractor A function to extract the ID from an item.
	 * @return A list of items from the source list that are missing in the target list.
	 */
	private <T> List<T> filterMissingItems(List<T> source, List<T> target,
	                                       Function<T, Object> idExtractor) {
		Set<Object> targetIds = target.stream().map(idExtractor).collect(Collectors.toSet());

		return source.stream().filter(item -> !targetIds.contains(idExtractor.apply(item)))
				.collect(Collectors.toList());
	}

	public List<Recipe> findRandomByDietAndCourse(RecipeDiet diet, String course, int amount) throws CustomException {
		return recipeRepository.findRandomRecipeByDiet(RecipeDiet.getFilterNames(diet), course, amount);
	}

	@Override
	public Page<Recipe> findBySearch(String searchTerm, RecipeDiet filter, Pageable pageable) {
		return recipeRepository.findBySearch(RecipeDiet.getFilter(filter),
				searchTerm, pageable);
	}


	@Override
	public Page<Recipe> findByPage(RecipeDiet diet, Pageable pageable) throws CustomException {
		return recipeRepository.findByDiet(RecipeDiet.getFilterNames(diet), pageable);
	}
}
