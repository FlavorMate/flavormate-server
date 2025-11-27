/* Licensed under AGPLv3 2024 - 2025 */
package de.flavormate.features.recipeDraft.services

import de.flavormate.exceptions.FForbiddenException
import de.flavormate.exceptions.FNotFoundException
import de.flavormate.extensions.openFoodFacts.dao.models.OFFProductEntity
import de.flavormate.extensions.openFoodFacts.repositories.OFFProductRepository
import de.flavormate.features.category.repositories.CategoryRepository
import de.flavormate.features.recipe.daos.mappers.ServingEntityServingDraftEntityMapper
import de.flavormate.features.recipe.daos.mappers.ingredient.IngredientGroupEntityIngredientGroupDraftEntityMapper
import de.flavormate.features.recipe.daos.mappers.instruction.InstructionGroupEntityInstructionGroupDraftEntityMapper
import de.flavormate.features.recipe.daos.models.RecipeEntity
import de.flavormate.features.recipe.daos.models.RecipeFileEntity
import de.flavormate.features.recipe.repositories.RecipeFileRepository
import de.flavormate.features.recipe.repositories.RecipeRepository
import de.flavormate.features.recipeDraft.daos.models.RecipeDraftEntity
import de.flavormate.features.recipeDraft.daos.models.RecipeDraftIngredientGroupItemNutritionEntity
import de.flavormate.features.recipeDraft.daos.models.ingredients.RecipeDraftIngredientGroupEntity
import de.flavormate.features.recipeDraft.daos.models.ingredients.RecipeDraftIngredientGroupItemEntity
import de.flavormate.features.recipeDraft.daos.models.instructions.RecipeDraftInstructionGroupEntity
import de.flavormate.features.recipeDraft.daos.models.instructions.RecipeDraftInstructionGroupItemEntity
import de.flavormate.features.recipeDraft.dtos.models.update.*
import de.flavormate.features.recipeDraft.repositories.*
import de.flavormate.features.tag.daos.models.TagEntity
import de.flavormate.features.tag.repositories.TagRepository
import de.flavormate.features.unit.repositories.UnitLocalizedRepository
import de.flavormate.shared.enums.FilePath
import de.flavormate.shared.extensions.mapToSet
import de.flavormate.shared.extensions.toKebabCase
import de.flavormate.shared.extensions.trimToNull
import de.flavormate.shared.services.AuthorizationDetails
import de.flavormate.shared.services.FileService
import de.flavormate.shared.services.TransactionService
import de.flavormate.utils.ValidatorUtils
import jakarta.enterprise.context.RequestScoped
import kotlin.jvm.optionals.getOrNull

@RequestScoped
class RecipeDraftMutationService(
  private val authorizationDetails: AuthorizationDetails,
  private val categoryRepository: CategoryRepository,
  private val fileService: FileService,
  private val offRepository: OFFProductRepository,
  private val recipeRepository: RecipeRepository,
  private val recipeFileRepository: RecipeFileRepository,
  private val recipeDraftRepository: RecipeDraftRepository,
  private val tagRepository: TagRepository,
  private val transactionService: TransactionService,
  private val unitLocalizedRepository: UnitLocalizedRepository,
  private val fileDraftRepository: RecipeDraftFileRepository,
  private val recipeDraftIngredientGroupRepository: RecipeDraftIngredientGroupRepository,
  private val recipeDraftIngredientGroupItemRepository: RecipeDraftIngredientGroupItemRepository,
  private val recipeDraftInstructionGroupRepository: RecipeDraftInstructionGroupRepository,
  private val recipeDraftInstructionGroupItemRepository: RecipeDraftInstructionGroupItemRepository,
) {

  fun delete(id: String): Boolean {
    transactionService.initialize()

    val draft =
      recipeDraftRepository.findById(id)
        ?: throw FNotFoundException(message = "Recipe draft not found!")

    if (!authorizationDetails.isAdminOrOwner(draft))
      throw FForbiddenException(message = "You are not allowed to delete this recipe draft!")

    for (file in draft.files) {
      transactionService.pendingOperations.add {
        fileService.deleteFolder(FilePath.RecipeDraft, file.id)
      }
      fileDraftRepository.deleteById(file.id)
    }

    return recipeDraftRepository.deleteById(id)
  }

  // Returns the created draft id
  fun initializeDraft(): String {
    val self = authorizationDetails.getSelf()
    val draft = RecipeDraftEntity.create(account = self).also { recipeDraftRepository.persist(it) }

    return draft.id
  }

  fun updateDraft(id: String, form: RecipeDraftUpdateDto) {
    val entity =
      recipeDraftRepository.findById(id)
        ?: throw FNotFoundException(message = "Recipe draft not found!")

    if (!authorizationDetails.isAdminOrOwner(entity))
      throw FForbiddenException(message = "You are not allowed to update this recipe draft!")

    if (form.cookTime != null) {
      entity.cookTime = form.cookTime
    }

    if (form.course != null) {
      entity.course = form.course
    }

    if (form.description != null) {
      entity.description = form.description.getOrNull().trimToNull()
    }

    if (form.diet != null) {
      entity.diet = form.diet
    }

    if (form.label != null) {
      entity.label = form.label.getOrNull()?.trimToNull()
    }

    if (form.prepTime != null) {
      entity.prepTime = form.prepTime
    }

    if (form.restTime != null) {
      entity.restTime = form.restTime
    }

    if (form.serving != null) {
      updateServing(entity, form.serving)
    }

    if (form.instructionGroups != null) {
      updateInstructionGroups(entity, form.instructionGroups)
    }

    if (form.ingredientGroups != null) {
      updateIngredientGroups(entity, form.ingredientGroups)
    }

    if (form.categories != null) {
      val removedCategories = entity.categories.filter { !form.categories.contains(it.id) }
      removedCategories.forEach { it.recipeDrafts.remove(entity) }

      val addedCategoriesIds =
        form.categories.filter { !entity.categories.map { c -> c.id }.contains(it) }

      val addedCategories = categoryRepository.findByIdsIn(addedCategoriesIds)
      addedCategories.forEach { it.recipeDrafts.add(entity) }
    }

    if (form.tags != null) {
      entity.tags =
        form.tags.map(String::toKebabCase).mapNotNull(String::trimToNull).toMutableList()
    }

    if (form.url != null) {
      entity.url = form.url.getOrNull()?.trimToNull()
    }

    recipeDraftRepository.persist(entity)
  }

  private fun updateServing(entity: RecipeDraftEntity, form: RecipeDraftServingUpdateDto) {
    if (form.amount != null) {
      entity.serving.amount = form.amount.getOrNull()
    }

    if (form.label != null) {
      entity.serving.label = form.label.getOrNull()?.trimToNull()
    }
  }

  private fun updateIngredientGroups(
    entity: RecipeDraftEntity,
    forms: List<RecipeDraftIngredientGroupUpdateDto>,
  ) {
    val missingGroups = mutableListOf<RecipeDraftIngredientGroupUpdateDto>()
    for (form in forms) {
      val target = entity.ingredientGroups.find { it.id == form.id }

      if (target == null) {
        missingGroups.add(form)
        continue
      }

      if (form.delete == true) {
        entity.ingredientGroups.remove(target)
        continue
      }

      if (form.label != null) {
        target.label = form.label.getOrNull()?.trimToNull()
      }

      if (form.index != null) {
        target.index = form.index
      }

      if (form.ingredients != null) {
        updateIngredientGroupsItem(entity = target, forms = form.ingredients)
      }
    }

    for (form in missingGroups) {
      RecipeDraftIngredientGroupEntity.create(
          label = form.label?.getOrNull()?.trimToNull(),
          index = entity.instructionGroups.size,
          ingredients = listOf(),
          recipe = entity,
          id =
            form.id
              .takeIf { ValidatorUtils.validateUUID4(it) }
              ?.takeUnless { recipeDraftIngredientGroupRepository.existsById(id = it) },
        )
        .also { entity.ingredientGroups.add(it) }
    }
  }

  fun updateIngredientGroupsItem(
    entity: RecipeDraftIngredientGroupEntity,
    forms: List<RecipeDraftIngredientGroupItemUpdateDto>,
  ) {
    val missingInstructions = mutableListOf<RecipeDraftIngredientGroupItemUpdateDto>()
    for (form in forms) {
      val target = entity.ingredients.find { it.id == form.id }

      if (target == null) {
        missingInstructions.add(form)
        continue
      }

      if (form.delete == true) {
        entity.ingredients.remove(target)
        continue
      }

      if (form.index != null) {
        target.index = form.index
      }

      if (form.amount != null) {
        target.amount = form.amount.getOrNull()
      }

      if (form.unit != null) {
        target.unit = form.unit.getOrNull()?.let { unitLocalizedRepository.findById(it) }
      }

      if (form.label != null) {
        target.label = form.label.getOrNull()?.trimToNull()
      }

      if (form.nutrition != null) {
        updateIngredientGroupsItemNutrition(target.nutrition, form.nutrition)
      }
    }

    for (form in missingInstructions) {
      RecipeDraftIngredientGroupItemEntity.create(
          label = form.label?.getOrNull()?.trimToNull(),
          index = entity.ingredients.size,
          group = entity,
          id =
            form.id
              .takeIf { ValidatorUtils.validateUUID4(it) }
              ?.takeUnless { recipeDraftIngredientGroupItemRepository.existsById(id = it) },
        )
        .also { entity.ingredients.add(it) }
    }
  }

  private fun updateIngredientGroupsItemNutrition(
    entity: RecipeDraftIngredientGroupItemNutritionEntity,
    form: RecipeDraftIngredientGroupItemNutritionUpdateDto,
  ) {
    entity.openFoodFactsId = form.openFoodFactsId
    entity.carbohydrates = form.carbohydrates
    entity.energyKcal = form.energyKcal
    entity.fat = form.fat
    entity.fiber = form.fiber
    entity.proteins = form.proteins
    entity.salt = form.salt
    entity.saturatedFat = form.saturatedFat
    entity.sodium = form.sodium
    entity.sugars = form.sugars
  }

  private fun updateInstructionGroups(
    entity: RecipeDraftEntity,
    forms: List<RecipeDraftInstructionGroupUpdateDto>,
  ) {
    val missingGroups = mutableListOf<RecipeDraftInstructionGroupUpdateDto>()
    for (form in forms) {
      val target = entity.instructionGroups.find { it.id == form.id }

      if (target == null) {
        missingGroups.add(form)
        continue
      }

      if (form.delete == true) {
        entity.instructionGroups.remove(target)
        continue
      }

      if (form.label != null) {
        target.label = form.label.getOrNull()?.trimToNull()
      }

      if (form.index != null) {
        target.index = form.index
      }

      if (form.instructions != null) {
        updateInstructionGroupsItem(entity = target, forms = form.instructions)
      }
    }

    for (form in missingGroups) {
      RecipeDraftInstructionGroupEntity.create(
          label = form.label?.getOrNull()?.trimToNull(),
          index = entity.instructionGroups.size,
          instructions = listOf(),
          recipe = entity,
          id =
            form.id
              .takeIf { ValidatorUtils.validateUUID4(it) }
              ?.takeUnless { recipeDraftInstructionGroupRepository.existsById(id = it) },
        )
        .also { entity.instructionGroups.add(it) }
    }
  }

  fun updateInstructionGroupsItem(
    entity: RecipeDraftInstructionGroupEntity,
    forms: List<RecipeDraftInstructionGroupItemUpdateDto>,
  ) {
    val missingInstructions = mutableListOf<RecipeDraftInstructionGroupItemUpdateDto>()
    for (form in forms) {
      val target = entity.instructions.find { it.id == form.id }

      if (target == null) {
        missingInstructions.add(form)
        continue
      }

      if (form.delete == true) {
        entity.instructions.remove(target)
        continue
      }

      if (form.label != null) {
        target.label = form.label.getOrNull()?.trimToNull()
      }

      if (form.index != null) {
        target.index = form.index
      }
    }

    for (form in missingInstructions) {
      RecipeDraftInstructionGroupItemEntity.create(
          label = form.label?.getOrNull()?.trimToNull(),
          index = entity.instructions.size,
          group = entity,
          id =
            form.id
              .takeIf { ValidatorUtils.validateUUID4(it) }
              ?.takeUnless { recipeDraftInstructionGroupItemRepository.existsById(id = it) },
        )
        .also {
          println(it.id)
          entity.instructions.add(it)
        }
    }
  }

  // returns the created recipe id
  fun createOrUpdateRecipe(id: String): String {
    transactionService.initialize()

    val draftEntity =
      recipeDraftRepository.findById(id)
        ?: throw FNotFoundException(message = "Recipe draft not found!")

    if (!authorizationDetails.isAdminOrOwner(draftEntity))
      throw FForbiddenException(message = "You are not allowed to create a recipe from this draft!")

    val recipe =
      if (draftEntity.originId == null) {
        RecipeEntity.create(authorizationDetails.getSelf())
      } else {
        recipeRepository.findById(draftEntity.originId!!)
          ?: throw FNotFoundException(message = "Recipe not found!")
      }

    saveRecipe(draftEntity, recipe, transactionService.pendingOperations)

    recipeDraftRepository.deleteById(id)

    return recipe.id
  }

  private fun saveRecipe(
    draft: RecipeDraftEntity,
    recipe: RecipeEntity,
    fileOperations: MutableList<() -> Unit>,
  ) {
    // Create OFF entries for used OFF ids used in this recipe if they don't exist yet
    draft.ingredientGroups
      .asSequence()
      .flatMap { group -> group.ingredients.asSequence() }
      .mapNotNull { ingredient -> ingredient.nutrition.openFoodFactsId }
      .filter { offProductId -> !offRepository.existsById(offProductId) }
      .forEach { offProductId -> offRepository.persist(OFFProductEntity.new(offProductId)) }

    offRepository.flush()

    // Apply fields to the recipe directly
    recipe.apply {
      this.label = draft.label!!
      this.description = draft.description
      this.prepTime = draft.prepTime
      this.cookTime = draft.cookTime
      this.restTime = draft.restTime
      this.serving = ServingEntityServingDraftEntityMapper.mapNotNullBasic(draft.serving)
      this.course = draft.course!!
      this.diet = draft.diet!!
      this.url = draft.url
    }

    // Don't apply instruction groups and ingredient groups directly
    // because Hibernate needs to keep track of relation changes by
    // manually modifying the lists
    val instructionGroups =
      draft.instructionGroups.mapToSet { group ->
        InstructionGroupEntityInstructionGroupDraftEntityMapper.mapNotNullBasic(group).also {
          it.recipe = recipe
        }
      }!!

    recipe.instructionGroups.clear()
    recipe.instructionGroups.addAll(instructionGroups)

    val ingredientGroups =
      draft.ingredientGroups.mapToSet { group ->
        IngredientGroupEntityIngredientGroupDraftEntityMapper.mapNotNullBasic(group).also {
          it.recipe = recipe
        }
      }!!

    recipe.ingredientGroups.clear()
    recipe.ingredientGroups.addAll(ingredientGroups)

    // persist recipe so it can be used in the following relations
    recipeRepository.persist(recipe)

    // delete files that were removed
    val removedFiles =
      recipe.files.filter { recipeFile ->
        draft.files.none { draftFile -> draftFile.originId == recipeFile.id }
      }

    for (file in removedFiles) {
      recipe.files.remove(file)
      recipeFileRepository.deleteById(file.id)
      fileOperations.add { fileService.deleteFolder(FilePath.Recipe, file.id) }
    }

    // add new files
    val addedFiles =
      draft.files.filter { draftFile ->
        recipe.files.none { recipeFile -> recipeFile.id == draftFile.originId }
      }

    for (file in addedFiles) {
      val fileEntity =
        RecipeFileEntity.create(authorizationDetails.getSelf(), recipe).also {
          recipeFileRepository.persist(it)
        }

      recipe.files.add(fileEntity)
      fileOperations.add {
        fileService.copyFolder(FilePath.RecipeDraft, file.id, FilePath.Recipe, fileEntity.id)
        fileService.deleteFolder(FilePath.RecipeDraft, file.id)
      }
    }

    // Applying the new cover
    recipe.setCoverFile()

    // removed deleted categories
    val removedCategories =
      recipe.categories.filter { recipeCategory ->
        draft.categories.none { draftCategory -> draftCategory.id == recipeCategory.id }
      }

    for (category in removedCategories) {
      recipe.categories.remove(category)
      category.recipes.remove(recipe)
      category.setCoverRecipe()
      categoryRepository.persist(category)
    }

    // adds new categories
    val addedCategories =
      draft.categories.filter { draftCategory ->
        recipe.categories.none { recipeCategory -> draftCategory.id == recipeCategory.id }
      }

    for (category in addedCategories) {
      categoryRepository.findById(category.id)?.also {
        recipe.categories.add(it)
        it.recipes.add(recipe)
        it.setCoverRecipe()
        categoryRepository.persist(it)
      }
    }

    // removes deleted tags
    val removedTags =
      recipe.tags.filter { recipeTag ->
        draft.tags.none { draftTag -> draftTag.equals(recipeTag.label, true) }
      }

    for (tag in removedTags) {
      recipe.tags.remove(tag)
      tag.recipes.removeIf { it.id == recipe.id }
      tag.setCoverRecipe()
      tagRepository.persist(tag)
    }

    // adds new tags
    val addedTags =
      draft.tags.filter { draftTag ->
        recipe.tags.none { recipeTag -> draftTag.equals(recipeTag.label, true) }
      }

    for (tag in addedTags) {
      val label = tag.lowercase().trim()
      val tagEntity =
        tagRepository.findByLabel(label)
          ?: TagEntity.create(label).also { tagRepository.persist(it) }
      recipe.tags.add(tagEntity)
      tagEntity.recipes.add(recipe)
      tagEntity.setCoverRecipe()
      tagRepository.persist(tagEntity)
    }

    recipeRepository.persist(recipe)
  }
}
