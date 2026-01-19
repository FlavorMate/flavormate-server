/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.extensions.auth.oidc.config

import de.flavormate.configuration.properties.FlavorMateProperties
import de.flavormate.extensions.auth.oidc.client.OIDCClient
import de.flavormate.extensions.auth.oidc.dao.models.OIDCProviderEntity
import de.flavormate.extensions.auth.oidc.dto.models.OIDCConfigWrapper
import de.flavormate.extensions.auth.oidc.repositories.OIDCProviderRepository
import de.flavormate.shared.enums.ImageResolution
import de.flavormate.utils.ImageUtils
import de.flavormate.utils.MimeTypes
import io.quarkus.runtime.Startup
import jakarta.annotation.PostConstruct
import jakarta.enterprise.context.ApplicationScoped
import jakarta.transaction.Transactional
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*
import kotlin.io.path.pathString
import kotlin.jvm.optionals.getOrNull
import org.apache.commons.io.FilenameUtils

@ApplicationScoped
@Startup
class OIDCConfig(
  private val flavorMateProperties: FlavorMateProperties,
  private val oidcProviderRepository: OIDCProviderRepository,
) {
  @PostConstruct
  fun initializeOIDC() {
    val wrappers =
      flavorMateProperties.auth().oidc().map {
        val endpoints = OIDCClient.fetchEndpoints(it.url())
        OIDCConfigWrapper(
          issuer = endpoints.get("issuer").asText(),
          clientId = it.clientId(),
          clientSecret = it.clientSecret().getOrNull(),
          label = it.name(),
          iconPath = it.icon().getOrNull(),
          overrideRedirectUri = it.redirectUriOverride().getOrNull() ?: false,
          urlTokenEndpoint = endpoints.get("token_endpoint").asText(),
          urlDiscoveryEndpoint = it.url(),
        )
      }

    // STEP 1: disable all providers not found in config
    disableOIDCProviders(wrappers)

    // STEP 2: upsert all providers found in config
    upsertOIDCProviders(wrappers)
  }

  @Transactional
  fun disableOIDCProviders(wrappers: List<OIDCConfigWrapper>) {
    oidcProviderRepository
      .findAllEnabled()
      .filter { entity ->
        wrappers.none { it.issuer == entity.issuer && it.clientId == entity.clientId }
      }
      .forEach { entity -> entity.enabled = false }
  }

  @Transactional
  fun upsertOIDCProviders(configProviders: List<OIDCConfigWrapper>) {
    for (prop in configProviders) {
      val provider =
        oidcProviderRepository.findByIssuerAndClientId(
          issuer = prop.issuer,
          clientId = prop.clientId,
        ) ?: OIDCProviderEntity.create(issuer = prop.issuer, clientId = prop.clientId)

      val iconBytes = processIcon(prop.iconPath)

      provider.update(
        label = prop.label,
        icon = iconBytes,
        clientSecret = prop.clientSecret,
        overrideRedirectUri = prop.overrideRedirectUri,
        urlTokenEndpoint = prop.urlTokenEndpoint,
        urlDiscoveryEndpoint = prop.urlDiscoveryEndpoint,
      )

      oidcProviderRepository.persist(provider)
    }
  }

  private fun processIcon(iconPath: String?): ByteArray? {
    if (iconPath == null) return null

    val icon = Paths.get(iconPath)

    val sourcePath = Paths.get(flavorMateProperties.paths().providers()).resolve(icon)

    val targetPath =
      Files.createTempDirectory(UUID.randomUUID().toString())
        .resolve(FilenameUtils.removeExtension(icon.pathString) + MimeTypes.WEBP_EXTENSION)

    ImageUtils.scaleMagick(sourcePath, targetPath, ImageResolution.P256.resolution)

    return Files.readAllBytes(targetPath)
  }
}
