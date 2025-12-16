/* Licensed under AGPLv3 2024 - 2025 */
package de.flavormate.core.auth.models

import io.netty.handler.codec.http.HttpResponseStatus
import io.quarkus.security.identity.IdentityProviderManager
import io.quarkus.security.identity.SecurityIdentity
import io.quarkus.security.identity.request.AnonymousAuthenticationRequest
import io.quarkus.security.identity.request.AuthenticationRequest
import io.quarkus.vertx.http.runtime.security.ChallengeData
import io.quarkus.vertx.http.runtime.security.HttpAuthenticationMechanism
import io.quarkus.vertx.http.runtime.security.HttpCredentialTransport
import io.smallrye.mutiny.Uni
import io.vertx.ext.web.RoutingContext
import java.util.*

/** Same as [io.quarkus.vertx.http.runtime.security.HttpAuthenticator.NoAuthenticationMechanism] */
class NoAuthenticationMechanism internal constructor() : HttpAuthenticationMechanism {
  override fun authenticate(
    context: RoutingContext,
    identityProviderManager: IdentityProviderManager,
  ): Uni<SecurityIdentity> {
    return Uni.createFrom().optional(Optional.empty())
  }

  override fun getChallenge(context: RoutingContext): Uni<ChallengeData> {
    val challengeData =
      ChallengeData(HttpResponseStatus.UNAUTHORIZED.code(), null as CharSequence?, null as String?)
    return Uni.createFrom().item(challengeData)
  }

  override fun getCredentialTypes(): Set<Class<out AuthenticationRequest?>> {
    return setOf<Class<out AuthenticationRequest?>>(AnonymousAuthenticationRequest::class.java)
  }

  override fun getCredentialTransport(context: RoutingContext): Uni<HttpCredentialTransport> {
    return Uni.createFrom().nullItem()
  }
}
