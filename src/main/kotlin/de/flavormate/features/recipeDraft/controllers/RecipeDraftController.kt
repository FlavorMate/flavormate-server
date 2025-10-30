/* Licensed under AGPLv3 2024 - 2025 */
package de.flavormate.features.recipeDraft.controllers

import de.flavormate.features.recipeDraft.dtos.models.update.RecipeDraftUpdateDto
import de.flavormate.features.recipeDraft.services.RecipeDraftService
import de.flavormate.features.recipeDraft.services.file.RecipeDraftFileService
import de.flavormate.shared.enums.ImageWideResolution
import de.flavormate.shared.models.api.Pagination
import de.flavormate.utils.MimeTypes
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.ws.rs.*
import java.io.File
import org.jboss.resteasy.reactive.RestForm
import org.jboss.resteasy.reactive.RestPath
import org.jboss.resteasy.reactive.RestQuery

@Path("/v3/recipe-drafts")
class RecipeDraftController(
  private val service: RecipeDraftService,
  private val fileService: RecipeDraftFileService,
) {

  @GET
  fun getRecipeDrafts(@BeanParam pagination: Pagination) =
    service.getRecipeDrafts(pagination = pagination)

  @POST fun postRecipeDrafts() = service.postRecipeDrafts()

  @GET
  @Path("/{id}")
  fun getRecipeDraft(@RestPath @NotBlank id: String, @RestQuery @NotBlank language: String) =
    service.getRecipeDraftsId(id = id, language = language)

  @DELETE
  @Path("/{id}")
  fun deleteRecipeDraft(@RestPath id: String) = service.deleteRecipeDraftsId(id = id)

  @PUT
  @Path("/{id}")
  fun putRecipeDraftsId(@RestPath @NotBlank id: String, @NotNull form: RecipeDraftUpdateDto) =
    service.putRecipeDraftsId(id = id, form = form)

  // Turns a draft into a recipe
  @POST
  @Path("/{id}")
  fun postRecipeDraftsId(@RestPath @NotBlank id: String) = service.postRecipeDraftsId(id = id)

  // ID - File
  @GET
  @Path("/{id}/files")
  fun getRecipeDraftsIdFiles(@RestPath @NotBlank id: String, @BeanParam pagination: Pagination) =
    fileService.getRecipeDraftsIdFiles(id = id, pagination = pagination)

  @GET
  @Produces(MimeTypes.WEBP_MIME)
  @Path("/{id}/files/{file}")
  fun getRecipeDraftsIdFilesFile(
    @RestPath @NotBlank id: String,
    @RestPath @NotBlank file: String,
    @RestQuery @NotNull resolution: ImageWideResolution,
  ) = fileService.getRecipeDraftsIdFilesFile(id = id, file = file, resolution = resolution)

  @DELETE
  @Path("/{id}/files/{file}")
  fun deleteRecipeDraftsIdFilesFile(
    @RestPath @NotBlank id: String,
    @RestPath @NotBlank file: String,
  ) = fileService.deleteRecipeDraftsIdFilesFile(id = id, file = file)

  @POST
  @Path("/{id}/files")
  fun createRecipeDraftsIdFilesFile(@RestPath @NotBlank id: String, @RestForm @NotNull file: File) =
    fileService.createRecipeDraftsIdFilesFile(id = id, file = file)
}
