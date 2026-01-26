/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.core.auth.services

import de.flavormate.configuration.properties.FlavorMateProperties
import de.flavormate.core.auth.dao.models.SessionEntity
import de.flavormate.core.auth.dtos.mappers.SessionDtoSessionEntityMapper
import de.flavormate.core.auth.dtos.models.SessionDto
import de.flavormate.core.auth.models.TokenResponseDao
import de.flavormate.core.auth.repositories.AccountSessionRepository
import de.flavormate.exceptions.FUnauthorizedException
import de.flavormate.features.account.dao.models.AccountEntity
import de.flavormate.features.role.enums.RoleTypes
import de.flavormate.shared.constants.AllowedSorts
import de.flavormate.shared.models.api.PageableDto
import de.flavormate.shared.models.api.Pagination
import de.flavormate.shared.services.AuthorizationDetails
import de.flavormate.utils.JwtUtils
import jakarta.enterprise.context.ApplicationScoped
import jakarta.ws.rs.core.Context
import jakarta.ws.rs.core.HttpHeaders
import java.time.LocalDateTime

@ApplicationScoped
class AuthSessionService(
  private val flavorMateProperties: FlavorMateProperties,
  private val accountSessionRepository: AccountSessionRepository,
  private val authorizationDetails: AuthorizationDetails,
) {

  @Context private lateinit var httpHeaders: HttpHeaders

  val jwtProperties
    get() = flavorMateProperties.auth().jwt()

  fun login(account: AccountEntity): TokenResponseDao {
    val refreshToken = createRefreshToken(account.id)

    SessionEntity.create(
        tokenHash = JwtUtils.hashJWT(refreshToken),
        expiresIn = jwtProperties.refreshToken().duration(),
        account = account,
        userAgent = authorizationDetails.userAgent,
      )
      .also { accountSessionRepository.persist(it) }

    val accessToken = createAccessToken(account)

    return TokenResponseDao.create(
      accessToken = accessToken,
      expiresIn = jwtProperties.accessToken().duration().toSeconds(),
      refreshToken = refreshToken,
    )
  }

  fun refreshToken(refreshToken: String, account: AccountEntity): TokenResponseDao {
    val session =
      accountSessionRepository.findByTokenHashAndAccountId(
        tokenHash = JwtUtils.hashJWT(refreshToken),
        accountId = account.id,
      ) ?: throw FUnauthorizedException("Invalid refresh token")

    if (session.revoked || session.expiresAt.isBefore(LocalDateTime.now())) {
      throw FUnauthorizedException("Refresh token expired")
    }

    val refreshToken = createRefreshToken(account.id)

    session.update(
      tokenHash = JwtUtils.hashJWT(refreshToken),
      expiresIn = jwtProperties.refreshToken().duration(),
      userAgent = authorizationDetails.userAgent,
    )

    val accessToken = createAccessToken(account)

    return TokenResponseDao.create(
      accessToken = accessToken,
      expiresIn = jwtProperties.accessToken().duration().toSeconds(),
      refreshToken = refreshToken,
    )
  }

  private fun createRefreshToken(accountId: String): String {
    return JwtUtils.generateToken(
      issuer = jwtProperties.issuer(),
      accountId = accountId,
      roles = setOf(RoleTypes.Refresh),
      duration = jwtProperties.refreshToken().duration(),
    )
  }

  private fun createAccessToken(account: AccountEntity): String {
    val roles = account.roles.map { it.role }.toSet()

    return JwtUtils.generateToken(
      jwtProperties.issuer(),
      account.id,
      roles,
      jwtProperties.accessToken().duration(),
    )
  }

  fun getAllSessions(pagination: Pagination): PageableDto<SessionDto> {
    val query =
      accountSessionRepository.findAllByOwner(
        sort = pagination.sortRequest(AllowedSorts.sessions),
        accountId = authorizationDetails.subject,
      )

    return PageableDto.fromQuery(
      dataQuery = query,
      countQuery = null,
      page = pagination.pageRequest,
      mapper = SessionDtoSessionEntityMapper::mapNotNullBasic,
    )
  }

  fun logout(): Boolean {
    val token = authorizationDetails.token
    val hash = JwtUtils.hashJWT(token)

    return accountSessionRepository.deleteByTokenHash(hash)
  }

  fun logoutAll(): Boolean {
    return accountSessionRepository.deleteByAccountId(accountId = authorizationDetails.subject)
  }

  fun deleteSession(id: String): Boolean {
    return accountSessionRepository.deleteByAccountIdAndId(
      accountId = authorizationDetails.subject,
      id = id,
    )
  }

  fun deleteAllSessionsButCurrent(): Boolean {
    val token = authorizationDetails.token
    val hash = JwtUtils.hashJWT(token)

    return accountSessionRepository.deleteAllSessionsButCurrent(
      accountId = authorizationDetails.subject,
      tokenHash = hash,
    )
  }
}
