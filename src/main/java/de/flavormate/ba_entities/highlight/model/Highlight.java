package de.flavormate.ba_entities.highlight.model;

import de.flavormate.aa_interfaces.models.BaseEntity;
import de.flavormate.ba_entities.recipe.enums.RecipeDiet;
import de.flavormate.ba_entities.recipe.model.Recipe;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;

@Entity
@Table(name = "highlights")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@ToString(onlyExplicitlyIncluded = true, callSuper = true)
public class Highlight extends BaseEntity {

	@NotNull
	@ManyToOne
	@JoinColumn(name = "recipe_id", referencedColumnName = "id", nullable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Recipe recipe;

	@NotNull
	@Column(nullable = false)
	@Builder.Default
	private LocalDate date = LocalDate.now();

	@NotNull
	@Column(nullable = false)
	@Builder.Default
	@Enumerated(EnumType.STRING)
	private RecipeDiet diet = RecipeDiet.Meat;
}
