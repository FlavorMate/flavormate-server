/* Licensed under AGPLv3 2024 */
package de.flavormate.ba_entities.recipe.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import de.flavormate.aa_interfaces.models.BaseEntity;
import de.flavormate.ad_configurations.flavormate.CommonConfig;
import de.flavormate.ba_entities.author.model.Author;
import de.flavormate.ba_entities.book.model.Book;
import de.flavormate.ba_entities.category.model.Category;
import de.flavormate.ba_entities.file.model.File;
import de.flavormate.ba_entities.ingredientGroup.model.IngredientGroup;
import de.flavormate.ba_entities.instructionGroup.model.InstructionGroup;
import de.flavormate.ba_entities.recipe.enums.RecipeCourse;
import de.flavormate.ba_entities.recipe.enums.RecipeDiet;
import de.flavormate.ba_entities.serving.model.Serving;
import de.flavormate.ba_entities.tag.model.Tag;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(
    name = "recipes",
    indexes = {@Index(name = "idx_course", columnList = "course")})
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@ToString(onlyExplicitlyIncluded = true, callSuper = true)
public class Recipe extends BaseEntity {

  @Column(nullable = false)
  @NotNull private String label;

  @Builder.Default
  @NotNull @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
  @JoinColumn(name = "recipe_id", referencedColumnName = "id")
  private List<File> files = new ArrayList<>();

  @ManyToOne
  @JoinColumn(name = "author_id", referencedColumnName = "id", nullable = false)
  @NotNull @OnDelete(action = OnDeleteAction.CASCADE)
  @JsonIncludeProperties({"id", "account"})
  private Author author;

  private String description;

  @NotNull @Column(nullable = false)
  @Builder.Default
  private Double rating = 0.0;

  @NotNull @Column(nullable = false)
  @Builder.Default
  private Duration prepTime = Duration.ZERO;

  @NotNull @Column(nullable = false)
  @Builder.Default
  private Duration cookTime = Duration.ZERO;

  @NotNull @Column(nullable = false)
  @Builder.Default
  private Duration restTime = Duration.ZERO;

  @NotNull @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "serving_id", referencedColumnName = "id", nullable = false)
  private Serving serving;

  @NotNull @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "recipe_id", referencedColumnName = "id")
  @Builder.Default
  private List<InstructionGroup> instructionGroups = new ArrayList<>();

  @NotNull @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "recipe_id", referencedColumnName = "id")
  @Builder.Default
  private List<IngredientGroup> ingredientGroups = new ArrayList<>();

  @ManyToMany(mappedBy = "recipes")
  @Builder.Default
  @JsonIgnoreProperties({"recipes"})
  @OnDelete(action = OnDeleteAction.CASCADE)
  private List<Book> books = new ArrayList<>();

  @NotNull @Column(nullable = false)
  @ManyToMany(mappedBy = "recipes")
  @JsonIgnoreProperties({"recipes"})
  @OnDelete(action = OnDeleteAction.CASCADE)
  @Builder.Default
  private List<Category> categories = new ArrayList<>();

  @NotNull @Column(nullable = false)
  @ManyToMany(mappedBy = "recipes")
  @JsonIgnoreProperties({"recipes"})
  @OnDelete(action = OnDeleteAction.CASCADE)
  @Builder.Default
  private List<Tag> tags = new ArrayList<>();

  @NotNull @Column(nullable = false, name = "course")
  @Enumerated(EnumType.STRING)
  private RecipeCourse course;

  @NotNull @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private RecipeDiet diet;

  private String url;

  public void translate(String language) {
    for (var category : categories) {
      var l10n =
          category.getLocalizations().stream()
              .filter(c -> c.getId().getLanguage().equals(language))
              .findFirst()
              .get();
      category.setLabel(l10n.getValue());
    }
  }

  public void toggleBook(Book book) {
    var bookFound = this.books.removeIf(b -> b.getId().equals(book.getId()));
    if (!bookFound) {
      this.books.add(book);
    }
  }

  public String getCoverUrl() {
    try {
      return CommonConfig.backendUrl() + "/" + files.getFirst().getPath();
    } catch (Exception e) {
      return null;
    }
  }
}
