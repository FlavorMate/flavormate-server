/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.extensions.importExport.controllers

import de.flavormate.extensions.importExport.models.IEPluginMetadata
import de.flavormate.extensions.importExport.services.IEService
import de.flavormate.features.role.enums.RoleTypes
import jakarta.annotation.security.RolesAllowed
import jakarta.enterprise.context.RequestScoped
import jakarta.validation.constraints.NotBlank
import jakarta.ws.rs.GET
import jakarta.ws.rs.POST
import jakarta.ws.rs.Path
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.Response
import org.jboss.resteasy.reactive.RestForm
import org.jboss.resteasy.reactive.RestPath

@RequestScoped
@RolesAllowed(RoleTypes.EXPORT_VALUE)
@Path("/v3/import-export/export")
class IEExportController(private val service: IEService) {

  @GET
  @Path("/")
  fun getAvailableExporters(): List<IEPluginMetadata> = service.getAvailableExporters()

  @POST
  @Produces("application/zip")
  @Path("/{pluginId}")
  fun exportMultiple(
    @RestPath @NotBlank pluginId: String,
    @RestForm("recipe") recipes: List<String>,
  ): Response = service.export(pluginId = pluginId, recipeIds = recipes)
}
