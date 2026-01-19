/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.extensions.auth.oidc.dao.models

import de.flavormate.core.encryption.converter.EncryptionConverter
import de.flavormate.extensions.auth.oidc.utils.OIDCUtils
import io.quarkus.hibernate.orm.panache.kotlin.PanacheEntityBase
import jakarta.persistence.*
import java.util.*
import org.hibernate.annotations.JdbcTypeCode

@Entity
@Table(name = "v3__ext__auth__oidc_provider")
class OIDCProviderEntity : PanacheEntityBase {

  @Id var id: String = UUID.randomUUID().toString()

  lateinit var issuer: String

  @Column(name = "client_id") lateinit var clientId: String

  @Convert(converter = EncryptionConverter::class)
  @Column(name = "client_secret")
  var clientSecret: String? = null

  lateinit var label: String

  @JdbcTypeCode(java.sql.Types.BINARY)
  @Column(columnDefinition = "bytea")
  var icon: ByteArray? = null

  @Column(name = "override_redirect_uri") var overrideRedirectUri: Boolean = false

  var enabled: Boolean = true

  @Column(name = "url_discovery_endpoint") lateinit var urlDiscoveryFullEndpoint: String

  @Column(name = "url_token_endpoint") lateinit var urlTokenEndpoint: String

  val urlDiscoveryBasicEndpoint: String
    get() = urlDiscoveryFullEndpoint.removeSuffix(OIDCUtils.OPENID_SUFFIX)

  fun update(
    clientSecret: String?,
    label: String,
    icon: ByteArray?,
    overrideRedirectUri: Boolean,
    urlDiscoveryEndpoint: String,
    urlTokenEndpoint: String,
  ) {
    this.issuer = issuer
    this.clientId = clientId
    this.clientSecret = clientSecret
    this.label = label
    this.icon = icon
    this.overrideRedirectUri = overrideRedirectUri
    this.enabled = true
    this.urlDiscoveryFullEndpoint = OIDCUtils.cleanURL(urlDiscoveryEndpoint)
    this.urlTokenEndpoint = urlTokenEndpoint
  }

  companion object {

    fun create(issuer: String, clientId: String) =
      OIDCProviderEntity().apply {
        this.issuer = issuer
        this.clientId = clientId
      }
  }
}
