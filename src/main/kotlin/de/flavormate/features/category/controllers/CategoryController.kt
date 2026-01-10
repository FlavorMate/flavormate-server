/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.features.category.controllers

import de.flavormate.features.category.services.CategoryService
import de.flavormate.features.role.enums.RoleTypes
import de.flavormate.shared.models.api.Pagination
import jakarta.annotation.security.RolesAllowed
import jakarta.enterprise.context.RequestScoped
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.ws.rs.BeanParam
import jakarta.ws.rs.GET
import jakarta.ws.rs.Path
import org.jboss.resteasy.reactive.RestPath
import org.jboss.resteasy.reactive.RestQuery

@RequestScoped
@RolesAllowed(RoleTypes.USER_VALUE)
@Path("/v3/categories")
class CategoryController(private val service: CategoryService) {
  @GET
  fun getCategories(@RestQuery @NotBlank language: String, @BeanParam pagination: Pagination) =
    service.getCategories(language = language, pagination = pagination)

  @GET
  @Path("/search")
  fun getCategoriesSearch(
    @RestQuery @NotBlank query: String,
    @RestQuery @NotNull language: String,
    @BeanParam pagination: Pagination,
  ) = service.getCategoriesSearch(query = query, language = language, pagination = pagination)

  // ID
  @GET
  @Path("/{id}")
  fun getCategory(@RestPath @NotBlank id: String, @RestQuery @NotBlank language: String) =
    service.getCategory(id = id, language = language)

  @GET
  @Path("/{id}/recipes")
  fun getCategoryRecipes(@RestPath @NotBlank id: String, @BeanParam pagination: Pagination) =
    service.getCategoryRecipes(id, pagination)
}
