/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.extensions.importExport.plugins.flavormate.services

import de.flavormate.exceptions.FBadRequestException
import de.flavormate.exceptions.FInternalErrorException
import de.flavormate.extensions.importExport.interfaces.IEPlugin
import de.flavormate.extensions.importExport.models.IEPluginContext
import de.flavormate.extensions.importExport.models.IEPluginMetadata
import de.flavormate.extensions.importExport.models.ieRecipe.IERecipe
import de.flavormate.extensions.importExport.models.ieRecipeDraft.IERecipeDraft
import de.flavormate.extensions.importExport.models.inputSource.FileInputSource
import de.flavormate.extensions.importExport.models.inputSource.IEImportType
import de.flavormate.extensions.importExport.models.inputSource.IEInputSource
import de.flavormate.extensions.importExport.models.inputSource.UrlInputSource
import de.flavormate.extensions.importExport.plugins.flavormate.models.IEFlavorMateRecipe
import de.flavormate.extensions.importExport.plugins.flavormate.services.exporter.IEFlavorMateExporter
import de.flavormate.extensions.importExport.plugins.flavormate.services.importer.IEPluginFlavorMateDownloader
import de.flavormate.extensions.importExport.plugins.flavormate.services.importer.IEPluginFlavorMateImporter
import de.flavormate.shared.enums.Language
import de.flavormate.shared.services.DownloadService
import de.flavormate.utils.ZipUtils
import jakarta.enterprise.context.ApplicationScoped
import java.nio.file.Files
import java.nio.file.Path
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.io.path.*
import kotlin.streams.asSequence

@ApplicationScoped
class IEPluginFlavormate(private val downloadService: DownloadService) : IEPlugin {
  override val metadata =
    IEPluginMetadata(
      id = "flavormate",
      name = mapOf(Language.EN to "FlavorMate Plugin", Language.DE to "FlavorMate Plugin"),
      version = "1.0.0",
      author = "FlavorMate",
      import = listOf(IEImportType.FileImport, IEImportType.UrlImport),
      importMimeTypes = listOf("application/zip"),
      importExtensions = listOf("zip"),
      importShortDescription =
        mapOf(
          Language.EN to "Import FlavorMate Backups",
          Language.DE to "FlavorMate Sicherungen importieren",
        ),
      importLongDescription =
        mapOf(
          Language.EN to "Import FlavorMate backups from files or URLs.",
          Language.DE to "FlavorMate Sicherungen aus Dateien oder URLs importieren.",
        ),
      export = true,
      exportShortDescription =
        mapOf(
          Language.EN to "Create FlavorMate backups",
          Language.DE to "FlavorMate Sicherungen erstellen",
        ),
      exportLongDescription =
        mapOf(
          Language.EN to "Export recipes by creating FlavorMate backups",
          Language.DE to "Rezepte als FlavorMate Sicherung exportieren.",
        ),
    )

  override fun import(
    inputs: List<IEInputSource>,
    workDirectory: Path,
    context: IEPluginContext,
  ): List<IERecipeDraft> {
    val downloader = IEPluginFlavorMateDownloader(context, downloadService)

    return inputs.flatMap { input ->
      if (metadata.import.none { it.isImportSupported(input) }) {
        throw FBadRequestException(
          message = "Unsupported import type ${input::class.simpleName} for ${metadata.name}"
        )
      }
      val zipFile =
        when (input) {
          is FileInputSource -> {
            input.file.toPath()
          }

          is UrlInputSource -> {
            downloader.download(input.name)
              ?: throw FInternalErrorException("Download failed for ${input.name}")
          }
        }

      handleZipFile(zipFile, workDirectory, context)
    }
  }

  private fun handleZipFile(
    zipFile: Path,
    workDirectory: Path,
    context: IEPluginContext,
  ): List<IERecipeDraft> {
    val importer = IEPluginFlavorMateImporter()

    val zipContent = workDirectory.resolve("extracted")

    ZipUtils.unzipDir(zipFile, zipContent)

    val rootDirectory =
      Files.find(
          zipContent,
          3,
          { path, attrs ->
            attrs.isRegularFile && path.name.equals("recipe.json", ignoreCase = true)
          },
        )
        .use { stream -> stream.asSequence().firstOrNull()?.parent?.parent }
        ?: throw FBadRequestException("No recipes found in zip file")

    if (!rootDirectory.normalize().startsWith(zipContent.normalize())) {
      throw FBadRequestException("Invalid zip file structure: path traversal detected")
    }

    val recipeFolders = rootDirectory.listDirectoryEntries()

    return recipeFolders.mapNotNull { recipeFolder ->
      val imageFolder = recipeFolder.resolve("files")
      val imageFiles =
        imageFolder
          .takeIf { it.exists() && it.isDirectory() }
          ?.listDirectoryEntries()
          ?.filter { it.exists() }
          ?.map { it.toFile() }

      val recipeFile = recipeFolder.resolve("recipe.json")

      if (!recipeFile.exists()) return@mapNotNull null

      val recipe =
        context.objectMapper.readValue(recipeFile.toFile(), IEFlavorMateRecipe::class.java)

      importer.import(recipe, imageFiles)
    }
  }

  /**
   * Creates a .zip file containing the exported recipes.
   *
   * export.zip
   * - Recipe 1/
   * - - recipe.json
   * - - files/
   * - - - {UUID}.webp
   * - - - {UUID}.webp
   */
  override fun export(inputs: List<IERecipe>, workDirectory: Path, context: IEPluginContext): Path {
    val exporter = IEFlavorMateExporter()

    val zipContent = workDirectory.resolve("zipContent").createDirectories()

    for (input in inputs) {
      runCatching {
        val recipeFolder = zipContent.resolve("${input.label} - (${input.id})")
        val fileFolder = recipeFolder.resolve("files")

        val recipeFile = recipeFolder.resolve("recipe.json")

        val recipe = exporter.export(input)

        context.objectMapper.writeValue(recipeFile.createParentDirectories().toFile(), recipe)

        for (image in input.files) {
          val imageFile = fileFolder.resolve(image.parentFile.name + "." + image.extension)

          image.copyTo(imageFile.createParentDirectories().toFile())
        }
      }
    }

    val zipFile =
      workDirectory.resolve(
        "Export ${
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"))
                }.zip"
      )

    ZipUtils.zipFile(sourceDir = zipContent, zipFile = zipFile)

    return zipFile
  }
}
