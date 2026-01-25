/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.features.token.services

import de.flavormate.features.token.dtos.mappers.TokenDtoTokenEntityMapper
import de.flavormate.features.token.dtos.models.TokenDto
import de.flavormate.features.token.repositories.TokenRepository
import de.flavormate.shared.constants.AllowedSorts
import de.flavormate.shared.models.api.PageableDto
import de.flavormate.shared.models.api.Pagination
import de.flavormate.shared.services.AuthorizationDetails
import jakarta.enterprise.context.RequestScoped
import jakarta.transaction.Transactional

@RequestScoped
class TokenService(
  private val authorizationDetails: AuthorizationDetails,
  private val tokenRepository: TokenRepository,
) {

  fun getAllTokens(pagination: Pagination): PageableDto<TokenDto> {
    val query =
      tokenRepository.findAllByOwner(
        sort = pagination.sortRequest(AllowedSorts.tokens),
        ownedById = authorizationDetails.subject,
      )

    return PageableDto.fromQuery(
      dataQuery = query,
      countQuery = null,
      page = pagination.pageRequest,
      mapper = TokenDtoTokenEntityMapper::mapNotNullBasic,
    )
  }

  @Transactional
  fun deleteToken(id: String): Boolean {
    return tokenRepository.deleteByOwnedByIdAndId(ownedById = authorizationDetails.subject, id = id)
  }
}
