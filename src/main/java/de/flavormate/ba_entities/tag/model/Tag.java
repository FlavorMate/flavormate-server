package de.flavormate.ba_entities.tag.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import de.flavormate.aa_interfaces.models.BaseEntity;
import de.flavormate.ba_entities.recipe.model.Recipe;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tags")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@ToString(onlyExplicitlyIncluded = true, callSuper = true)
public class Tag extends BaseEntity {

	@NotNull
	@Column(nullable = false)
	private String label;

	@ManyToMany
	@JoinTable(name = "tag_recipe", joinColumns = @JoinColumn(name = "tag_id"),
			inverseJoinColumns = @JoinColumn(name = "recipe_id"))
	@NotNull
	@Builder.Default
	@JsonIgnoreProperties({"tags"})
	private List<Recipe> recipes = new ArrayList<>();

	public void addOrRemoveRecipe(Recipe recipe) {
		if (!recipes.removeIf(r -> r.getId().equals(recipe.getId()))) {
			recipes.add(recipe);
		}
	}
}
