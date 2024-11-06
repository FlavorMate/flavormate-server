/* Licensed under AGPLv3 2024 */
package de.flavormate.ba_entities.file.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.flavormate.aa_interfaces.models.BaseEntity;
import de.flavormate.ba_entities.file.enums.FileCategory;
import de.flavormate.ba_entities.file.enums.FileType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.nio.file.Paths;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "files")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@ToString(onlyExplicitlyIncluded = true, callSuper = true)
public class File extends BaseEntity {

  // e.g. image, video, etc.
  @NotNull @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private FileType type;

  // e.g. recipe, author, etc.
  @NotNull @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private FileCategory category;

  // e.g. id of owner
  @NotNull @Column(nullable = false)
  private Long owner;

  @Transient private String content;

  @JsonIgnore
  private String getTypePath() {
    return switch (type) {
      case IMAGE -> "images";
      default -> "";
    };
  }

  @JsonIgnore
  private String getCategoryPath() {
    return switch (category) {
      case ACCOUNT -> "accounts";
      case AUTHOR -> "authors";
      case RECIPE -> "recipes";
      default -> "";
    };
  }

  @JsonIgnore
  public String getName() {
    return switch (type) {
      case IMAGE -> this.id.toString() + ".jpg";
      default -> "";
    };
  }

  @JsonIgnore
  public String getPath() {
    return Paths.get(getCategoryPath(), owner.toString(), getTypePath(), getName()).toString();
  }
}
