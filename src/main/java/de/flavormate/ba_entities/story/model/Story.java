package de.flavormate.ba_entities.story.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import de.flavormate.aa_interfaces.models.BaseEntity;
import de.flavormate.ba_entities.recipe.model.Recipe;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "stories")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@ToString(onlyExplicitlyIncluded = true, callSuper = true)
public class Story extends BaseEntity {

	@NotNull
	@ManyToOne
	@JoinColumn(name = "recipe_id", referencedColumnName = "id", nullable = false)
	@JsonIgnoreProperties({"author", "books", "categories", "tags"})
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Recipe recipe;

	@NotNull
	@Column(columnDefinition = "TEXT")
	private String content;

	@NotNull
	@Column(columnDefinition = "TEXT")
	private String label;
}
