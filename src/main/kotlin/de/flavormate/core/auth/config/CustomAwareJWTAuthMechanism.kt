/* Licensed under AGPLv3 2024 - 2025 */
package de.flavormate.core.auth.config

import de.flavormate.configuration.properties.FlavorMateProperties
import de.flavormate.core.auth.models.NoAuthenticationMechanism
import io.quarkus.oidc.runtime.OidcAuthenticationMechanism
import io.quarkus.security.identity.IdentityProviderManager
import io.quarkus.security.identity.SecurityIdentity
import io.quarkus.security.identity.request.AuthenticationRequest
import io.quarkus.smallrye.jwt.runtime.auth.JWTAuthMechanism
import io.quarkus.vertx.http.runtime.security.ChallengeData
import io.quarkus.vertx.http.runtime.security.HttpAuthenticationMechanism
import io.quarkus.vertx.http.runtime.security.HttpCredentialTransport
import io.smallrye.jwt.auth.principal.JWTParser
import io.smallrye.mutiny.Uni
import io.vertx.ext.web.RoutingContext
import jakarta.annotation.Priority
import jakarta.enterprise.context.ApplicationScoped
import jakarta.enterprise.inject.Alternative
import jakarta.ws.rs.core.HttpHeaders

@Alternative
@Priority(1)
@ApplicationScoped
class CustomAwareJWTAuthMechanism(
  private val jwtMechanism: JWTAuthMechanism,
  private val oidcMechanism: OidcAuthenticationMechanism,
  private val jwtParser: JWTParser,
  private val jwtBlockList: JwtBlockList,
  private val flavorMateProperties: FlavorMateProperties,
) : HttpAuthenticationMechanism {

  val issuer
    get(): String = flavorMateProperties.auth().jwt().issuer()

  override fun authenticate(
    context: RoutingContext,
    identityProviderManager: IdentityProviderManager,
  ): Uni<SecurityIdentity> {
    addTokenToContext(context)

    return selectBetweenJwtAndOidc(context).authenticate(context, identityProviderManager)
  }

  override fun getChallenge(context: RoutingContext): Uni<ChallengeData> {
    return selectBetweenJwtAndOidc(context).getChallenge(context)
  }

  override fun getCredentialTypes(): Set<Class<out AuthenticationRequest?>> {
    val credentialTypes: MutableSet<Class<out AuthenticationRequest?>> = HashSet()
    credentialTypes.addAll(jwtMechanism.credentialTypes)
    credentialTypes.addAll(oidcMechanism.credentialTypes)
    return credentialTypes
  }

  override fun getCredentialTransport(context: RoutingContext): Uni<HttpCredentialTransport> {
    return selectBetweenJwtAndOidc(context).getCredentialTransport(context)
  }

  /**
   * Determines the appropriate `HttpAuthenticationMechanism` to use based on the provided routing
   * context. It evaluates the "Authorization" header in the request to check for a JWT token's
   * presence, validates the issuer, and decides whether to use the JWT mechanism or the OIDC
   * mechanism. If the JWT token is blocked or the "Authorization" header is missing, a
   * `NoAuthenticationMechanism` is returned.
   *
   * @param context The routing context representing the incoming HTTP request and associated data.
   * @return An `HttpAuthenticationMechanism` representing the selected authentication mechanism.
   */
  private fun selectBetweenJwtAndOidc(context: RoutingContext): HttpAuthenticationMechanism {
    try {
      val authHeader =
        context.request().getHeader(HttpHeaders.AUTHORIZATION) ?: return NoAuthenticationMechanism()

      if (!authHeader.startsWith("Bearer ")) return NoAuthenticationMechanism()

      val token = authHeader.substring("Bearer ".length)

      if (jwtBlockList.contains(token)) return NoAuthenticationMechanism()

      val jwt = jwtParser.parseOnly(token)
      val iss = jwt.issuer

      return when (iss) {
        issuer -> jwtMechanism
        else -> oidcMechanism
      }
    } catch (e: Exception) {
      return NoAuthenticationMechanism()
    }
  }

  /**
   * Adds an "Authorization" header to the request within the provided routing context if it does
   * not already exist. If the "Authorization" header is missing and a "token" query parameter is
   * present, this token is used to create a "Bearer" token and is added as the "Authorization"
   * header.
   *
   * @param context The routing context representing the incoming HTTP request and its data.
   */
  fun addTokenToContext(context: RoutingContext) {
    val authHeader: String? = context.request().getHeader("Authorization")

    if (authHeader != null) return

    val token: String? = context.request().getParam("token")
    if (!token.isNullOrBlank()) {
      context.request().headers().add("Authorization", "Bearer $token")
    }
  }
}
