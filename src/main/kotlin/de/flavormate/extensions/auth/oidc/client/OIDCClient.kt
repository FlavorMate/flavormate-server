/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.extensions.auth.oidc.client

import com.fasterxml.jackson.databind.JsonNode
import de.flavormate.extensions.auth.oidc.dao.models.OIDCProviderEntity
import de.flavormate.extensions.auth.oidc.dto.models.OIDCExchangeForm
import de.flavormate.extensions.auth.oidc.utils.OIDCUtils
import de.flavormate.utils.JSONUtils.mapper
import jakarta.ws.rs.core.HttpHeaders
import org.apache.hc.client5.http.classic.methods.HttpGet
import org.apache.hc.client5.http.classic.methods.HttpPost
import org.apache.hc.client5.http.entity.UrlEncodedFormEntity
import org.apache.hc.client5.http.impl.classic.BasicHttpClientResponseHandler
import org.apache.hc.client5.http.impl.classic.HttpClients
import org.apache.hc.core5.http.message.BasicNameValuePair

object OIDCClient {

  fun fetchEndpoints(url: String): JsonNode {

    val cleanedUrl = OIDCUtils.cleanURL(url)

    val httpGet = HttpGet(cleanedUrl)

    val response =
      HttpClients.createDefault().use { client ->
        val response = client.execute(httpGet, BasicHttpClientResponseHandler())
        mapper.readTree(response)
      }

    return response
  }

  fun exchangeToken(provider: OIDCProviderEntity, form: OIDCExchangeForm): String {
    val httpPost = HttpPost(provider.urlTokenEndpoint)

    httpPost.setHeader(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded")

    val params =
      mutableListOf(
        BasicNameValuePair("client_id", provider.clientId),
        BasicNameValuePair("grant_type", "authorization_code"),
        BasicNameValuePair("code", form.code),
        BasicNameValuePair("code_verifier", form.codeVerifier),
        BasicNameValuePair("redirect_uri", form.redirectUri),
      )

    if (provider.clientSecret != null)
      params.add(BasicNameValuePair("client_secret", provider.clientSecret))

    httpPost.entity = UrlEncodedFormEntity(params)

    val response =
      HttpClients.createDefault().use { client ->
        val response = client.execute(httpPost, BasicHttpClientResponseHandler())
        mapper.readTree(response)
      }

    return response.get("id_token").asText()
  }
}
