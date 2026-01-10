/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.features.categoryGroup.dtos.models

import de.flavormate.features.category.dtos.models.CategoryDto

data class CategoryGroupDto(val id: String, val label: String, val categories: List<CategoryDto>)
