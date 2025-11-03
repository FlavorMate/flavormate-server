/* Licensed under AGPLv3 2024 - 2025 */
package de.flavormate.features.account.dao.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import de.flavormate.features.book.daos.models.BookEntity
import de.flavormate.features.recipe.daos.models.RecipeEntity
import de.flavormate.features.role.models.RoleEntity
import de.flavormate.features.story.daos.models.StoryEntity
import de.flavormate.shared.enums.Diet
import de.flavormate.shared.models.entities.OwnedEntity
import jakarta.persistence.*
import java.util.*
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction

/**
 * Represents an account entity in the system. This entity maps to the "accounts" database table and
 * is used to manage account-related data, such as user credentials, roles, and preferences.
 *
 * Properties:
 * - `displayName`: The display name associated with the account.
 * - `username`: A unique username identifying the account.
 * - `password`: The hashed password associated with the account.
 * - `lastActivity`: The timestamp of the user's most recent activity.
 * - `valid`: Indicates whether the account is currently valid or active.
 * - `roles`: A set of roles assigned to the account, linking the account to the corresponding
 *   `Role` entities.
 * - `diet`: The dietary preference of the user, represented using the `Diet` enum.
 * - `email`: The email address associated with the account, which may be null.
 * - `avatar`: A file entity representing the avatar image for the account, which may be null.
 *
 * Relationships:
 * - Many-to-many relationship with `Role`, defining the roles assigned to the account with
 *   cascading operations.
 * - Many-to-one relationship with `File`, representing the avatar associated with the account.
 *
 * Methods:
 * - `hasRole(role: RoleTypes)`: Checks if the account has a specific role assigned. Returns true if
 *   the role exists in the account's roles set.
 */
@Entity
@Table(name = "v3__account")
class AccountEntity : OwnedEntity() {
  @Column(name = "display_name") lateinit var displayName: String

  lateinit var username: String

  lateinit var password: String

  // Used by admins to selectively enable accounts
  var enabled: Boolean = false

  // Indicates if the email was verified
  var verified: Boolean = false

  @Enumerated(EnumType.STRING) var diet: Diet = Diet.Meat

  lateinit var email: String

  @Column(name = "first_login") var firstLogin = true

  //    @ManyToOne(cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
  //    @JoinColumn(name = "avatar", referencedColumnName = "id")
  //    var avatar: AccountFileEntity? = null

  @OneToOne(cascade = [CascadeType.ALL])
  @OnDelete(action = OnDeleteAction.SET_NULL)
  @JoinColumn(name = "avatar", referencedColumnName = "id")
  var avatar: AccountFileEntity? = null

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(
    name = "v3__account__role",
    joinColumns = [JoinColumn(name = "account_id")],
    inverseJoinColumns = [JoinColumn(name = "role_id")],
  )
  @JsonIgnoreProperties("users")
  var roles: MutableSet<RoleEntity> = mutableSetOf()

  @OneToMany(
    cascade = [CascadeType.ALL],
    mappedBy = "ownedBy",
    orphanRemoval = true,
    fetch = FetchType.LAZY,
  )
  var books: MutableSet<BookEntity> = mutableSetOf()

  @ManyToMany(mappedBy = "subscriber", fetch = FetchType.LAZY)
  @JsonIgnoreProperties("subscriber")
  var subscribedBooks: MutableSet<BookEntity> = mutableSetOf()

  @OneToMany(mappedBy = "ownedBy", fetch = FetchType.LAZY)
  @JsonIgnoreProperties("ownedBy")
  var recipes: MutableSet<RecipeEntity> = mutableSetOf()

  @OneToMany(mappedBy = "ownedBy", fetch = FetchType.LAZY)
  @JsonIgnoreProperties("ownedBy")
  var stories: MutableSet<StoryEntity> = mutableSetOf()

  override fun hashCode(): Int {
    return Objects.hash(id, displayName, username, password, enabled, diet, email, firstLogin)
  }
}
