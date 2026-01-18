/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.shared.services

import de.flavormate.exceptions.FNotFoundException
import de.flavormate.features.account.dao.models.AccountEntity
import de.flavormate.features.account.repositories.AccountRepository
import de.flavormate.features.role.enums.RoleTypes
import de.flavormate.shared.models.entities.OwnedEntity
import io.quarkus.security.identity.SecurityIdentity
import jakarta.enterprise.context.RequestScoped
import org.eclipse.microprofile.jwt.JsonWebToken

@RequestScoped
class AuthorizationDetails(
  private val accountRepository: AccountRepository,
  private val securityIdentity: SecurityIdentity,
) {
  private var self: AccountEntity? = null

  private val jwt: JsonWebToken
    get() = securityIdentity.principal as JsonWebToken

  val subject: String
    get() = jwt.getClaim("sub")

  val groups: Set<String>
    get() = jwt.groups

  val token: String
    get() = jwt.rawToken

  val issuer: String
    get() = jwt.issuer

  val audiences: Set<String>
    get() = jwt.audience

  val name: String?
    get() = jwt.getClaim<String>("name")

  val email: String?
    get() = jwt.getClaim<String>("email")

  private fun getAccount(): AccountEntity {
    if (self == null) {
      self =
        accountRepository.findById(subject)
          ?: throw FNotFoundException(message = "AccountEntity with id $subject not found", id = "")
    }
    return self!!
  }

  fun isOwner(target: OwnedEntity): Boolean {
    return target.ownedById == subject
  }

  fun isAdmin(): Boolean {
    return groups.contains(RoleTypes.Admin.name)
  }

  fun isAdminOrOwner(target: OwnedEntity): Boolean {
    return isAdmin() || isOwner(target)
  }

  fun getSelf() = getAccount()
}
