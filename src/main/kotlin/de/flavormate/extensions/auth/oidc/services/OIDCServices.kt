/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.extensions.auth.oidc.services

import de.flavormate.core.auth.models.LoginForm
import de.flavormate.extensions.auth.oidc.dto.models.OIDCExchangeForm
import de.flavormate.shared.models.api.Pagination
import jakarta.enterprise.context.RequestScoped
import jakarta.transaction.Transactional

@RequestScoped
class OIDCServices(
  private val mutationService: OIDCMutationService,
  private val queryService: OIDCQueryService,
) {

  fun getProviders() = queryService.getProviders()

  fun getLinks(pagination: Pagination) = queryService.getLinks(pagination = pagination)

  // Mutations
  @Transactional fun login() = mutationService.login()

  @Transactional fun link(form: LoginForm) = mutationService.link(form = form)

  fun exchangeToken(form: OIDCExchangeForm) = mutationService.exchangeToken(form = form)

  @Transactional
  fun deleteLink(providerId: String) = mutationService.deleteLink(providerId = providerId)
}
