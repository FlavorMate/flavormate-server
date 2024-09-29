package de.flavormate.ba_entities.backup.model.flavorMate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.flavormate.ba_entities.ingredient.wrapper.IngredientDraft;
import de.flavormate.ba_entities.ingredientGroup.model.IngredientGroup;
import de.flavormate.ba_entities.ingredientGroup.wrapper.IngredientGroupDraft;
import de.flavormate.ba_entities.unit.model.Unit;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public final class FMIngredientGroup {
	private String label;
	private List<FMIngredient> ingredients;

	public static FMIngredientGroup fromIngredientGroup(IngredientGroup ingredientGroup) {
		return new FMIngredientGroup(
				ingredientGroup.getLabel(),
				ingredientGroup.getIngredients().stream().map(FMIngredient::fromIngredient).toList()
		);
	}

	@JsonIgnore
	public IngredientGroupDraft toIngredientGroupDraft() {
		return new IngredientGroupDraft(
				label,
				ingredients.stream().map((i) -> new IngredientDraft(i.getAmount(), new Unit(i.getUnit()), i.getLabel())).toList()
		);
	}

}
