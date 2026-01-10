/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.shared.services

import de.flavormate.configuration.properties.FlavorMateProperties
import de.flavormate.shared.resourceBundles.AppMessages
import io.quarkus.qute.Template
import io.quarkus.qute.TemplateInstance
import io.quarkus.qute.i18n.Localized
import io.quarkus.qute.i18n.MessageBundles
import io.vertx.ext.web.RoutingContext
import jakarta.enterprise.context.RequestScoped
import java.util.*

/**
 * A service responsible for handling template rendering and localization.
 *
 * This class provides methods to manage templates by populating them with data, including localized
 * content and configuration values. It also retrieves localized application messages based on the
 * user's preferred language.
 *
 * @param routingContext The routing context that provides details about the current HTTP request,
 *   including localization preferences.
 * @constructor Creates a new instance of the service, scoped to a specific request.
 */
@RequestScoped
class TemplateService(
  private val routingContext: RoutingContext,
  private val flavorMateProperties: FlavorMateProperties,
) {
  val backendUrl
    get() = flavorMateProperties.server().url()

  val messages: AppMessages
    get() = MessageBundles.get(AppMessages::class.java, Localized(locale.language))

  val locale: Locale
    get() = Locale.forLanguageTag(routingContext.preferredLanguage()?.tag() ?: "en")

  /**
   * Populates the given template with provided data, appends additional configuration values, and
   * sets the locale for rendering.
   *
   * @param template The template to be populated and rendered.
   * @param data A mutable map containing key-value pairs to be injected into the template. Defaults
   *   to an empty map.
   * @return A TemplateInstance configured with the provided data and locale for rendering.
   */
  fun handleTemplate(
    template: Template,
    data: MutableMap<String, Any?> = mutableMapOf(),
  ): TemplateInstance {
    data["backendUrl"] = backendUrl
    data["locale"] = locale
    return template.data(data).setLocale(locale)
  }

  /**
   * Retrieves a localized message based on the provided selector function.
   *
   * This function allows the caller to select and retrieve a specific localized string from the
   * application's message bundle by applying the given selector to the `AppMessages` instance.
   *
   * @param selector A function that takes an instance of `AppMessages` and returns a specific
   *   localized message as a string.
   * @return The localized message string based on the provided selector.
   */
  fun getMessage(selector: (AppMessages) -> String) = selector(messages)
}
