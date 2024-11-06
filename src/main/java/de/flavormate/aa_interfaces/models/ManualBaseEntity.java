/* Licensed under AGPLv3 2024 */
package de.flavormate.aa_interfaces.models;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Version;
import java.time.Instant;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

/**
 * An abstract base class for JPA entities, providing common fields such as an identifier and a
 * version number.
 *
 * <p>The class is annotated with Lombok's {@code @Data} annotation, which automatically generates
 * standard boilerplate code such as getters, setters, and the toString method.
 *
 * <p>The {@code @NoArgsConstructor} annotation is used to generate a no-argument constructor.
 *
 * <p>The {@code @EqualsAndHashCode} annotation is configured with {@code onlyExplicitlyIncluded =
 * true} to include only explicitly marked fields (in this case, the {@code id} and {@code version}
 * fields) in the equals and hashCode methods.
 *
 * <p>The class is marked with {@code @MappedSuperclass}, indicating that it should be mapped to the
 * database but is not an entity itself.
 *
 * <p>The {@code id} field represents the primary key of the entity, and it is annotated with
 * {@code @Id}.
 *
 * <p>The {@code version} field represents the optimistic locking version of the entity, and it is
 * annotated with {@code @Version}.
 */
@MappedSuperclass
@SuperBuilder
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public abstract class ManualBaseEntity {

  /** The primary key of the entity. */
  @Id
  @Column(nullable = false)
  @EqualsAndHashCode.Include
  @ToString.Include
  protected Long id;

  /** The optimistic locking version of the entity. */
  @Version
  @Column(nullable = false)
  @EqualsAndHashCode.Include
  @Builder.Default
  @ToString.Include
  protected Long version = 0L;

  /** Timestamp indicating when the entity was initially published or created. */
  @CreationTimestamp protected Instant createdOn;

  /** Timestamp indicating the last modification time of the entity. */
  @UpdateTimestamp protected Instant lastModifiedOn;
}
