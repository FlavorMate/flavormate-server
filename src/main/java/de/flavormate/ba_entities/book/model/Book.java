package de.flavormate.ba_entities.book.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import de.flavormate.aa_interfaces.models.BaseEntity;
import de.flavormate.ba_entities.author.model.Author;
import de.flavormate.ba_entities.recipe.model.Recipe;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "books")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@ToString(onlyExplicitlyIncluded = true, callSuper = true)
public class Book extends BaseEntity {

	@NotNull
	@Column(nullable = false)
	private String label;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "owner_id", referencedColumnName = "id", nullable = false)
	@JsonIncludeProperties({"id", "account"})
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Author owner;

	@NotNull
	@ManyToMany
	@JoinTable(name = "book_subscriber", joinColumns = @JoinColumn(name = "book_id"),
			inverseJoinColumns = @JoinColumn(name = "author_id"))
	@JsonIncludeProperties({"id", "account"})
	@Builder.Default
	private List<Author> subscriber = new ArrayList<>();

	@NotNull
	@Column(nullable = false)
	@Builder.Default
	private Boolean visible = false;

	@ManyToMany
	@JoinTable(name = "book_recipe", joinColumns = @JoinColumn(name = "book_id"),
			inverseJoinColumns = @JoinColumn(name = "recipe_id"))
	@Builder.Default
	@JsonIgnoreProperties({"author", "books"})
	@OnDelete(action = OnDeleteAction.CASCADE)
	private List<Recipe> recipes = new ArrayList<>();


	public void toggleRecipe(Recipe recipe) {

		var recipeFound = this.recipes.removeIf(r -> r.getId().equals(recipe.getId()));
		if (!recipeFound) {
			this.recipes.add(recipe);
		}
	}

	public void addOrRemoveRecipe(Recipe recipe) {
		if (!recipes.removeIf(r -> r.getId().equals(recipe.getId()))) {
			recipes.add(recipe);
		}
	}
}
