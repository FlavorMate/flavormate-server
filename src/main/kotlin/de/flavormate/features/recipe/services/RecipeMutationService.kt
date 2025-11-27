/* Licensed under AGPLv3 2024 - 2025 */
package de.flavormate.features.recipe.services

import de.flavormate.exceptions.FForbiddenException
import de.flavormate.exceptions.FNotFoundException
import de.flavormate.exceptions.FUnauthorizedException
import de.flavormate.features.account.repositories.AccountRepository
import de.flavormate.features.category.repositories.CategoryRepository
import de.flavormate.features.recipe.dtos.models.RecipeTransferDto
import de.flavormate.features.recipe.repositories.RecipeRepository
import de.flavormate.features.recipeDraft.daos.mapper.RecipeDraftEntityRecipeEntityMapper
import de.flavormate.features.recipeDraft.daos.models.RecipeDraftFileEntity
import de.flavormate.features.recipeDraft.repositories.RecipeDraftFileRepository
import de.flavormate.features.recipeDraft.repositories.RecipeDraftRepository
import de.flavormate.shared.enums.FilePath
import de.flavormate.shared.services.AuthorizationDetails
import de.flavormate.shared.services.FileService
import de.flavormate.shared.services.TransactionService
import jakarta.enterprise.context.RequestScoped

@RequestScoped
class RecipeMutationService(
  private val accountRepository: AccountRepository,
  private val authorizationDetails: AuthorizationDetails,
  private val recipeDraftRepository: RecipeDraftRepository,
  private val recipeDraftFileRepository: RecipeDraftFileRepository,
  private val recipeRepository: RecipeRepository,
  private val recipeFileService: FileService,
  private val transactionService: TransactionService,
  private val categoryRepository: CategoryRepository,
) {

  fun delete(id: String): Boolean {
    transactionService.initialize()

    val recipe =
      recipeRepository.findById(id) ?: throw FNotFoundException(message = "Recipe not found!")

    if (!authorizationDetails.isAdminOrOwner(recipe))
      throw FUnauthorizedException(message = "You are not allowed to delete this recipe!")

    recipe.files.forEach {
      transactionService.pendingOperations.add {
        recipeFileService.deleteFolder(FilePath.Recipe, it.id)
      }
    }

    return recipeRepository.deleteById(id)
  }

  fun transferRecipe(recipeId: String, ownerId: String) {
    val recipe =
      recipeRepository.findById(recipeId) ?: throw FNotFoundException(message = "Recipe not found!")

    if (!authorizationDetails.isAdmin())
      throw FUnauthorizedException(message = "You are not allowed to transfer this recipe!")

    val account =
      accountRepository.findById(ownerId)
        ?: throw FNotFoundException(message = "Account not found!")

    recipe
      .apply {
        this.ownedBy = account
        this.ownedById = account.id
      }
      .also { recipeRepository.persist(it) }
  }

  // Returns the created draft id
  fun createDraft(id: String): String {

    val recipe =
      recipeRepository.findById(id) ?: throw FNotFoundException(message = "Recipe not found!")

    if (!authorizationDetails.isAdminOrOwner(recipe))
      throw FForbiddenException(message = "You are not allowed to create a draft for this recipe!")

    val draftEntity = recipeDraftRepository.findByOriginId(id)

    if (draftEntity != null) return draftEntity.id

    val self = authorizationDetails.getSelf()

    val entity =
      RecipeDraftEntityRecipeEntityMapper.mapNotNullOwned(input = recipe, account = self).also {
        recipeDraftRepository.persist(it)
      }

    recipe.categories.forEach {
      it.recipeDrafts.add(entity)
      categoryRepository.persist(it)
    }

    for (recipeFile in recipe.files) {
      val entityFile =
        RecipeDraftFileEntity.create(account = self, recipeDraft = entity)
          .apply {
            this.mimeType = recipeFile.mimeType
            this.originId = recipeFile.id
          }
          .also { recipeDraftFileRepository.persist(it) }

      recipeFileService.copyFolder(
        FilePath.Recipe,
        recipeFile.id,
        FilePath.RecipeDraft,
        entityFile.id,
      )
    }

    return entity.id
  }

  fun putRecipesIdTransfer(id: String, form: RecipeTransferDto) {
    val recipe =
      recipeRepository.findById(id) ?: throw FNotFoundException(message = "Recipe not found!")

    if (!authorizationDetails.isAdmin())
      throw FForbiddenException(message = "You are not allowed to transfer this recipe!")

    val account =
      accountRepository.findById(form.newOwner)
        ?: throw FNotFoundException(message = "Account not found!")

    recipe.ownedBy = account
    recipe.ownedById = account.id

    recipeRepository.persist(recipe)
  }
}
