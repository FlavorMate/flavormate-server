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
import java.io.File
import org.jboss.resteasy.reactive.RestForm
import org.jboss.resteasy.reactive.RestPath

@RequestScoped
@RolesAllowed(RoleTypes.USER_VALUE)
@Path("/v3/import-export")
class IEController(private val service: IEService) {

  @GET
  @Path("/importers")
  fun getAvailableImporters(): List<IEPluginMetadata> = service.getAvailableImporters()

  @GET
  @Path("/exporters")
  fun getAvailableExporters(): List<IEPluginMetadata> = service.getAvailableExporters()

  @POST
  @Path("/import/{pluginId}")
  fun import(
    @RestPath @NotBlank pluginId: String,
    @RestForm("file") files: List<File>?,
    @RestForm("url") urls: List<String>?,
  ): List<String> = service.import(pluginId = pluginId, files = files, urls = urls)

  @POST
  @Produces("application/zip")
  @Path("/export/single/{pluginId}")
  fun exportSingle(
    @RestPath @NotBlank pluginId: String,
    @RestForm("recipe") recipe: String,
  ): Response = service.exportSingle(pluginId = pluginId, recipeId = recipe)

  @POST
  @Produces("application/zip")
  @Path("/export/multiple/{pluginId}")
  fun exportMultiple(
    @RestPath @NotBlank pluginId: String,
    @RestForm("recipe") recipes: List<String>,
  ): Response = service.exportMultiple(pluginId = pluginId, recipeIds = recipes)
}
