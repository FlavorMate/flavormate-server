/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.features.search.controllers

import de.flavormate.features.role.enums.RoleTypes
import de.flavormate.features.search.mappers.SearchDtoSearchEntityMapper
import de.flavormate.features.search.models.SearchDto
import de.flavormate.features.search.models.SearchFilter
import de.flavormate.features.search.repositories.SearchRepository
import de.flavormate.shared.models.api.PageableDto
import de.flavormate.shared.models.api.Pagination
import de.flavormate.shared.services.AuthorizationDetails
import jakarta.annotation.security.RolesAllowed
import jakarta.enterprise.context.RequestScoped
import jakarta.validation.constraints.NotNull
import jakarta.ws.rs.BeanParam
import jakarta.ws.rs.GET
import jakarta.ws.rs.Path
import jakarta.ws.rs.PathParam
import org.jboss.resteasy.reactive.RestQuery
import org.jboss.resteasy.reactive.Separator

@RequestScoped
@RolesAllowed(RoleTypes.USER_VALUE)
@Path("/v3/search")
class SearchController(
  private val searchRepository: SearchRepository,
  private val authorizationDetails: AuthorizationDetails,
) {

  @GET
  @Path("/{searchTerm}")
  fun search(
    @PathParam("searchTerm") searchTerm: String,
    @RestQuery @NotNull language: String,
    @RestQuery @NotNull @Separator(",") filter: List<SearchFilter>,
    @BeanParam pagination: Pagination,
  ): PageableDto<SearchDto> {
    val dataQuery =
      searchRepository.findByQuery(
        q = searchTerm,
        ownerId = authorizationDetails.getSelf().id,
        language = language,
        filter = filter,
      )

    return PageableDto.fromQuery(
      dataQuery = dataQuery,
      page = pagination.pageRequest,
      countQuery = null,
      mapper = { SearchDtoSearchEntityMapper.mapNotNullBasic(it) },
    )
  }
}
