/* Licensed under AGPLv3 2024 - 2025 */
package de.flavormate.configuration.jackson

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import io.quarkus.arc.All
import io.quarkus.jackson.ObjectMapperCustomizer
import jakarta.inject.Singleton
import jakarta.ws.rs.Produces

/**
 * A utility class to manage the creation and configuration of a shared singleton instance of
 * [ObjectMapper]. This class provides central control of [ObjectMapper] customization to ensure
 * consistency across the application. It ensures efficient and thread-safe instantiation of the
 * [ObjectMapper] and allows customization using [ObjectMapperCustomizer] implementations.
 *
 * The class is designed to work with dependency injection frameworks and provides a `@Produces`
 * method to supply a properly configured and customized [ObjectMapper] instance.
 */
object CustomObjectMapper {
  /**
   * Singleton instance of the [ObjectMapper] that is lazily initialized in a thread-safe manner.
   * The configuration and creation of the ObjectMapper are encapsulated within the companion
   * object.
   *
   * The lazily initialized instance ensures efficient resource usage and guarantees a single
   * instance throughout the application's lifecycle.
   */
  val instance: ObjectMapper by lazy(LazyThreadSafetyMode.SYNCHRONIZED) { createObjectMapper() }

  private fun createObjectMapper(): ObjectMapper {
    val om = ObjectMapper().findAndRegisterModules()

    om.setDefaultPropertyInclusion(JsonInclude.Include.NON_NULL)

    om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    om.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
    om.configure(SerializationFeature.WRITE_DURATIONS_AS_TIMESTAMPS, false)

    return om
  }

  // Replaces the CDI producer for ObjectMapper built into Quarkus
  @Singleton
  @Produces
  fun objectMapper(@All customizers: MutableList<ObjectMapperCustomizer>): ObjectMapper {
    val mapper = instance // Custom `ObjectMapper`

    // Apply all ObjectMapperCustomizer beans (incl. Quarkus)
    for (customizer in customizers) {
      customizer.customize(mapper)
    }

    return mapper
  }
}
