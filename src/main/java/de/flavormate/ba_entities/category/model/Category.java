package de.flavormate.ba_entities.category.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import de.flavormate.aa_interfaces.models.ManualBaseEntity;
import de.flavormate.ba_entities.categoryGroup.model.CategoryGroup;
import de.flavormate.ba_entities.recipe.model.Recipe;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "categories")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@ToString(onlyExplicitlyIncluded = true, callSuper = true)
public class Category extends ManualBaseEntity {
	@NotNull
	@Column(nullable = false)
	private String label;
	@NotNull
	@ManyToOne
	@JoinColumn(name = "group_id", referencedColumnName = "id", nullable = false)
	private CategoryGroup group;
	@ManyToMany
	@JoinTable(name = "category_recipe", joinColumns = @JoinColumn(name = "category_id"),
			inverseJoinColumns = @JoinColumn(name = "recipe_id"))
	@NotNull
	@Builder.Default
	@JsonIgnoreProperties({"author", "books", "categories"})
	private List<Recipe> recipes = new ArrayList<>();

	@NotNull
	@OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	@Builder.Default
	@JsonIgnoreProperties({"category"})
	private List<CategoryLocalization> localizations = new ArrayList<>();

	public Category(Category category, String label) {
		super(category.id, category.version, category.createdOn, category.lastModifiedOn);
		this.label = label;
		this.group = category.getGroup();
		this.recipes = category.getRecipes();
		this.localizations = category.getLocalizations();
	}

	public void addOrRemoveRecipe(Recipe recipe) {
		if (!recipes.removeIf(r -> r.getId().equals(recipe.getId()))) {
			recipes.add(recipe);
		}

		new Category(label, group, recipes, localizations);
	}
}
