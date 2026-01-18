/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.extensions.auth.oidc.services

import de.flavormate.core.auth.models.LoginForm
import de.flavormate.core.auth.models.TokenResponseDao
import de.flavormate.core.auth.services.AuthService
import de.flavormate.exceptions.FNotFoundException
import de.flavormate.extensions.auth.oidc.client.OIDCClient
import de.flavormate.extensions.auth.oidc.dao.models.OIDCMappingEntity
import de.flavormate.extensions.auth.oidc.dto.models.OIDCExchangeForm
import de.flavormate.extensions.auth.oidc.repositories.OIDCMappingRepository
import de.flavormate.extensions.auth.oidc.repositories.OIDCProviderRepository
import de.flavormate.features.account.repositories.AccountRepository
import de.flavormate.shared.services.AuthorizationDetails
import jakarta.enterprise.context.RequestScoped

@RequestScoped
class OIDCMutationService(
  private val accountRepository: AccountRepository,
  private val authService: AuthService,
  private val authorizationDetails: AuthorizationDetails,
  private val oidcMappingRepository: OIDCMappingRepository,
  private val oidcProviderRepository: OIDCProviderRepository,
) {
  fun login(): TokenResponseDao {
    val issuer = authorizationDetails.issuer
    val audiences = authorizationDetails.audiences
    val subject = authorizationDetails.subject

    val mapping =
      oidcMappingRepository.findByIssuerAndAudiencesAndSubject(issuer, audiences, subject)
        ?: throw FNotFoundException("No mapping found for issuer '$issuer' and subject '$subject'")

    mapping.name = authorizationDetails.name
    mapping.email = authorizationDetails.email

    return authService.loginOIDC(accountId = mapping.accountId)
  }

  fun link(form: LoginForm): TokenResponseDao {
    val account =
      accountRepository.findByUsername(form.username)
        ?: throw FNotFoundException(message = "Account with id ${form.username} not found")

    val token = authService.login(form)

    // At this point the credentials are valid
    val issuer = authorizationDetails.issuer
    val audiences = authorizationDetails.audiences

    val subject = authorizationDetails.subject

    val provider =
      oidcProviderRepository.findByIssuerAndClientIds(issuer, audiences)
        ?: throw FNotFoundException(message = "Provider not found")

    OIDCMappingEntity.create(
        providerId = provider.id,
        subject = subject,
        accountId = account.id,
        name = authorizationDetails.name,
        email = authorizationDetails.email,
      )
      .also { oidcMappingRepository.persist(it) }

    return token
  }

  fun exchangeToken(form: OIDCExchangeForm): String {
    val provider =
      oidcProviderRepository.findByIssuerAndClientId(form.issuer, form.clientId)
        ?: throw FNotFoundException(message = "Provider not found")

    return OIDCClient.exchangeToken(provider, form)
  }

  fun deleteLink(providerId: String): Boolean {
    val self = authorizationDetails.getSelf()
    return oidcMappingRepository.deleteByAccountIdAndProviderId(
      accountId = self.id,
      providerId = providerId,
    )
  }
}
