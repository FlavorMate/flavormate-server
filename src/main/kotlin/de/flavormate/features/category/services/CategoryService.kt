/* Licensed under AGPLv3 2024 - 2025 */
package de.flavormate.features.category.services

import de.flavormate.shared.models.api.Pagination
import jakarta.enterprise.context.RequestScoped

@RequestScoped
class CategoryService(private val queryService: CategoryQueryService) {

  fun getCategories(language: String, pagination: Pagination) =
    queryService.getCategories(language, pagination)

  fun getCategory(id: String, language: String) = queryService.getCategory(id, language)

  fun getCategoryRecipes(id: String, pagination: Pagination) =
    queryService.getCategoryRecipes(id, pagination)

  fun getCategoriesSearch(query: String, language: String, pagination: Pagination) =
    queryService.getCategoriesSearch(query, language, pagination)
}
