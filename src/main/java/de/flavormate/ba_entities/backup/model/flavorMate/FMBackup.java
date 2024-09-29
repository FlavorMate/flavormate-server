package de.flavormate.ba_entities.backup.model.flavorMate;

import de.flavormate.aa_interfaces.models.ManualBaseEntity;
import de.flavormate.ba_entities.recipe.enums.RecipeCourse;
import de.flavormate.ba_entities.recipe.enums.RecipeDiet;
import de.flavormate.ba_entities.recipe.model.Recipe;
import de.flavormate.ba_entities.recipe.wrapper.RecipeDraft;
import de.flavormate.ba_entities.serving.model.Serving;
import de.flavormate.ba_entities.tag.model.Tag;
import de.flavormate.ba_entities.tag.wrapper.TagDraft;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Duration;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class FMBackup {
	private List<String> files;
	private Double rating;
	private Duration prepTime;
	private Duration cookTime;
	private Duration restTime;
	private List<FMInstructionGroup> instructionGroups;
	private List<FMIngredientGroup> ingredientGroups;
	private List<Long> categories;
	private List<String> tags;
	private String label;
	private String author;
	private String description;
	private Serving serving;
	private RecipeCourse course;
	private RecipeDiet diet;
	private String url;

	public static FMBackup fromRecipe(Recipe recipe) {
		List<String> files = null;
		if (!recipe.getFiles().isEmpty())
			files = recipe.getFiles().stream().map((file) -> "./data/" + recipe.getId() + "/" + file.getId() + ".jpg").toList();
		var rating = recipe.getRating();
		var prepTime = recipe.getPrepTime();
		Duration cookTime = recipe.getCookTime();
		Duration restTime = recipe.getRestTime();
		List<FMInstructionGroup> instructionGroups = recipe.getInstructionGroups().stream().map(FMInstructionGroup::fromInstructionGroup).toList();
		List<FMIngredientGroup> ingredientGroups = recipe.getIngredientGroups().stream().map(FMIngredientGroup::fromIngredientGroup).toList();
		List<Long> categories = recipe.getCategories().stream().map(ManualBaseEntity::getId).toList();
		List<String> tags = recipe.getTags().stream().map(Tag::getLabel).toList();
		String label = recipe.getLabel();
		String author = recipe.getAuthor().getAccount().getUsername();
		String description = recipe.getDescription();
		Serving serving = recipe.getServing();
		RecipeCourse course = recipe.getCourse();
		RecipeDiet diet = recipe.getDiet();
		String url = recipe.getUrl();
		return new FMBackup(files, rating, prepTime, cookTime, restTime, instructionGroups, ingredientGroups, categories, tags, label, author, description, serving, course, diet, url);
	}

	public RecipeDraft toRecipeDraft() {
		return new RecipeDraft(categories, ingredientGroups.stream().map(FMIngredientGroup::toIngredientGroupDraft).toList(),
				instructionGroups.stream().map(FMInstructionGroup::toInstructionGroupDraft).toList(), serving.toDraft(),
				tags.stream().map(TagDraft::new).toList(), cookTime, prepTime, restTime, course, diet, description,
				label, url, null);
	}
}
