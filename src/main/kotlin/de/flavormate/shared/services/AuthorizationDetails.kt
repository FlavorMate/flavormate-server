/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.shared.services

import de.flavormate.exceptions.FNotFoundException
import de.flavormate.features.account.dao.models.AccountEntity
import de.flavormate.features.account.repositories.AccountRepository
import de.flavormate.features.role.enums.RoleTypes
import de.flavormate.shared.models.entities.OwnedEntity
import de.flavormate.utils.ValidatorUtils
import io.quarkus.security.identity.SecurityIdentity
import jakarta.enterprise.context.RequestScoped
import jakarta.ws.rs.core.Context
import jakarta.ws.rs.core.HttpHeaders
import org.eclipse.microprofile.jwt.JsonWebToken

@RequestScoped
class AuthorizationDetails(
  private val accountRepository: AccountRepository,
  private val securityIdentity: SecurityIdentity,
) {
  @Context private lateinit var httpHeaders: HttpHeaders

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

  val userAgent: String?
    get() = httpHeaders.getHeaderString("User-Agent").takeIf(ValidatorUtils::validateUserAgent)

  fun isOwner(target: OwnedEntity): Boolean {
    return target.ownedById == subject
  }

  fun isAdmin(): Boolean {
    return groups.contains(RoleTypes.Admin.name)
  }

  fun isAdminOrOwner(target: OwnedEntity): Boolean {
    return isAdmin() || isOwner(target)
  }

  // TODO: introduce null aware cache
  private var cachedAccount: AccountEntity? = null

  val account
    get(): AccountEntity? =
      cachedAccount ?: accountRepository.findById(subject).also { cachedAccount = it }

  // TODO: make this a property named something like "requireAccount"
  fun getSelf() =
    account
      ?: throw FNotFoundException(message = "AccountEntity with id $subject not found", id = "")
}
