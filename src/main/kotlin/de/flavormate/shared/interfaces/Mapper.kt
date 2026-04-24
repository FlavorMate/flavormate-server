/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.shared.interfaces

import de.flavormate.configuration.jackson.CustomObjectMapper
import de.flavormate.features.account.dao.models.AccountEntity

/**
 * Abstract base class for mapping data between two types.
 *
 * This class is designed to serve as a foundation for creating mappers that transform data of type
 * `E` into type `G`. Specific implementations of this mapper are required to define the logic for
 * data transformation by overriding the `mapBasic` method.
 *
 * @param FROM The source type of the mapping.
 * @param TO The target type of the mapping.
 */
abstract class BasicMapper<FROM, TO> {
  val objectMapper = CustomObjectMapper.instance

  abstract fun mapNotNullBasic(input: FROM): TO

  fun mapBasic(input: FROM?): TO? {
    if (input == null) return null
    return mapNotNullBasic(input)
  }
}

abstract class OwnedMapper<FROM, TO> {
  abstract fun mapNotNullOwned(input: FROM, account: AccountEntity): TO

  fun mapOwned(input: FROM?, account: AccountEntity): TO? {
    if (input == null) return null
    return mapNotNullOwned(input, account)
  }
}

abstract class L10nMapper<FROM, TO> {
  val objectMapper = CustomObjectMapper.instance

  abstract fun mapNotNullL10n(input: FROM, language: String): TO

  fun mapL10n(input: FROM?, language: String): TO? {
    if (input == null) return null
    return mapNotNullL10n(input, language)
  }
}
