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
import java.io.File
import org.jboss.resteasy.reactive.RestForm
import org.jboss.resteasy.reactive.RestPath

@RequestScoped
@RolesAllowed(RoleTypes.IMPORT_VALUE)
@Path("/v3/import-export/import")
class IEImportController(private val service: IEService) {

  @GET
  @Path("/")
  fun getAvailableImporters(): List<IEPluginMetadata> = service.getAvailableImporters()

  @POST
  @Path("/{pluginId}")
  fun import(
    @RestPath @NotBlank pluginId: String,
    @RestForm("file") files: List<File>?,
    @RestForm("url") urls: List<String>?,
  ): List<String> = service.import(pluginId = pluginId, files = files, urls = urls)
}
