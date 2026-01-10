/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.shared.models.entities

import jakarta.persistence.Column
import jakarta.persistence.MappedSuperclass
import jakarta.persistence.Version
import java.time.LocalDateTime
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp

@MappedSuperclass
open class TracedEntity : CoreEntity() {
  @Version var version: Long = 0

  @CreationTimestamp @Column(name = "created_on") var createdOn = LocalDateTime.now()

  @UpdateTimestamp @Column(name = "last_modified_on") var lastModifiedOn = LocalDateTime.now()
}
