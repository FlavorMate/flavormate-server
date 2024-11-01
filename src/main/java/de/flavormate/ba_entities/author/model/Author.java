/* Licensed under AGPLv3 2024 */
package de.flavormate.ba_entities.author.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import de.flavormate.aa_interfaces.models.BaseEntity;
import de.flavormate.ba_entities.account.model.Account;
import de.flavormate.ba_entities.book.model.Book;
import de.flavormate.ba_entities.recipe.model.Recipe;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "authors")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@ToString(onlyExplicitlyIncluded = true, callSuper = true)
public class Author extends BaseEntity {

  @OnDelete(action = OnDeleteAction.CASCADE)
  @OneToOne
  @JoinColumn(name = "account_id", referencedColumnName = "id", nullable = false)
  @JsonIncludeProperties({"id", "username", "displayName", "avatar"})
  private Account account;

  @NotNull @OneToMany(cascade = CascadeType.ALL, mappedBy = "owner", orphanRemoval = true)
  @Builder.Default
  @OnDelete(action = OnDeleteAction.CASCADE)
  private List<Book> books = new ArrayList<>();

  @NotNull @ManyToMany(mappedBy = "subscriber")
  @Builder.Default
  @OnDelete(action = OnDeleteAction.SET_NULL)
  @JsonIgnoreProperties({"subscriber"})
  private List<Book> subscribedBooks = new ArrayList<>();

  @NotNull @Builder.Default
  @JsonIgnoreProperties({"author"})
  @OneToMany(mappedBy = "author")
  private List<Recipe> recipes = new ArrayList<>();

  public void addOrRemoveRecipe(Recipe recipe) {
    if (!recipes.removeIf(r -> r.getId().equals(recipe.getId()))) {
      recipes.add(recipe);
    }
  }
}
