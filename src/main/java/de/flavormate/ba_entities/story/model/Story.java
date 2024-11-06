/* Licensed under AGPLv3 2024 */
package de.flavormate.ba_entities.story.model;

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

@Entity
@Table(name = "stories")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@ToString(onlyExplicitlyIncluded = true, callSuper = true)
public class Story extends BaseEntity {

  @NotNull @ManyToOne
  @JoinColumn(name = "recipe_id", referencedColumnName = "id", nullable = false)
  @JsonIgnoreProperties({"author", "books", "categories", "tags"})
  @OnDelete(action = OnDeleteAction.CASCADE)
  private Recipe recipe;

  @NotNull @Column(columnDefinition = "TEXT")
  private String content;

  @NotNull @Column(columnDefinition = "TEXT")
  private String label;

  @NotNull @ManyToOne
  @JoinColumn(name = "author_id", referencedColumnName = "id", nullable = false)
  @NotNull @OnDelete(action = OnDeleteAction.CASCADE)
  @JsonIncludeProperties({"id", "account"})
  private Author author;
}
