/* Licensed under AGPLv3 2024 - 2025 */
package de.flavormate.shared.interfaces

import de.flavormate.features.account.dao.models.AccountEntity
import de.flavormate.utils.JSONUtils

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
  val om = JSONUtils.mapper

  abstract fun mapNotNullBasic(input: FROM): TO

  fun mapBasic(input: FROM?): TO? {
    if (input == null) return null
    return mapNotNullBasic(input)
  }
}

/**
 * Abstract class that provides secure mapping functionality between an input entity of type `E` and
 * an output entity of type `G`. It extends the `MinimalMapper` class and introduces an additional
 * layer of security by considering roles such as "Owner" and "Admin" during the mapping process.
 *
 * @param FROM The type of the input entity.
 * @param TO The type of the output entity.
 */
abstract class SecureMapper<FROM, TO> {
  abstract fun mapNotNullSecure(input: FROM, isOwner: Boolean, isAdmin: Boolean): TO

  protected fun <T> isAccessible(input: T, isOwner: Boolean, isAdmin: Boolean): T? {
    if (!(isOwner || isAdmin)) return null
    return input
  }

  fun mapSecure(input: FROM?, isOwner: Boolean, isAdmin: Boolean): TO? {
    if (input == null) return null
    return mapNotNullSecure(input, isOwner, isAdmin)
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
  val om = JSONUtils.mapper

  abstract fun mapNotNullL10n(input: FROM, language: String): TO

  fun mapL10n(input: FROM?, language: String): TO? {
    if (input == null) return null
    return mapNotNullL10n(input, language)
  }
}
