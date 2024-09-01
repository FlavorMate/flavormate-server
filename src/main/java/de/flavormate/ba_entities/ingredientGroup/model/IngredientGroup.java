package de.flavormate.ba_entities.ingredientGroup.model;


import de.flavormate.aa_interfaces.models.BaseEntity;
import de.flavormate.ba_entities.ingredient.model.Ingredient;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ingredient_groups")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@ToString(onlyExplicitlyIncluded = true, callSuper = true)
public class IngredientGroup extends BaseEntity {

	private String label;

	@NotNull
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "group_id", referencedColumnName = "id")
	@Builder.Default
	private List<Ingredient> ingredients = new ArrayList<>();

}
