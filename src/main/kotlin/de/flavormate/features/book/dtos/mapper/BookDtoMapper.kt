/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.features.book.dtos.mapper

import de.flavormate.features.account.dtos.mappers.AccountPreviewDtoMapper
import de.flavormate.features.book.daos.models.BookEntity
import de.flavormate.features.book.dtos.models.BookDto
import de.flavormate.features.recipe.dtos.mappers.RecipeFileDtoPreviewMapper
import de.flavormate.shared.interfaces.BasicMapper

object BookDtoMapper : BasicMapper<BookEntity, BookDto>() {
  override fun mapNotNullBasic(input: BookEntity) =
    BookDto(
      id = input.id,
      version = input.version,
      createdOn = input.createdOn,
      lastModifiedOn = input.lastModifiedOn,
      ownedBy = AccountPreviewDtoMapper.mapNotNullBasic(input.ownedBy),
      label = input.label,
      visible = input.visible,
      cover = input.coverRecipe?.coverFile?.let(RecipeFileDtoPreviewMapper::mapNotNullBasic),
      recipeCount = input.recipes.count(),
      subscriberCount = input.subscriber.count(),
    )
}
