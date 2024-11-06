/* Licensed under AGPLv3 2024 */
package de.flavormate.ba_entities.file.service;

import com.fasterxml.jackson.databind.JsonNode;
import de.flavormate.aa_interfaces.services.BaseService;
import de.flavormate.aa_interfaces.services.ICRUDService;
import de.flavormate.ab_exeptions.exceptions.ConflictException;
import de.flavormate.ab_exeptions.exceptions.CustomException;
import de.flavormate.ab_exeptions.exceptions.NotFoundException;
import de.flavormate.ab_exeptions.exceptions.UnauthorizedException;
import de.flavormate.ad_configurations.flavormate.PathsConfig;
import de.flavormate.ba_entities.account.model.Account;
import de.flavormate.ba_entities.account.repository.AccountRepository;
import de.flavormate.ba_entities.file.enums.FileCategory;
import de.flavormate.ba_entities.file.model.File;
import de.flavormate.ba_entities.file.repository.FileRepository;
import de.flavormate.ba_entities.file.wrapper.FileDraft;
import de.flavormate.ba_entities.recipe.model.Recipe;
import de.flavormate.ba_entities.recipe.repository.RecipeRepository;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class FileService extends BaseService implements ICRUDService<File, FileDraft> {

  private final AccountRepository accountRepository;
  private final FileRepository fileRepository;
  private final PathsConfig pathsConfig;
  private final RecipeRepository recipeRepository;

  @Override
  public File create(FileDraft body) throws CustomException {
    try {
      var file =
          File.builder().category(body.category()).owner(body.owner()).type(body.type()).build();

      file = fileRepository.save(file);

      var content = body.content().split(",")[1];

      var path = Paths.get(pathsConfig.content().getPath(), file.getPath());

      Files.createDirectories(path.getParent());

      byte[] data = Base64.decodeBase64(content);
      try (OutputStream stream = new FileOutputStream(path.toFile())) {
        stream.write(data);
      }

      return file;
    } catch (Exception e) {
      throw new ConflictException(File.class);
    }
  }

  @Override
  public boolean deleteById(Long id) throws CustomException {
    try {
      var account =
          accountRepository
              .findByUsername(getPrincipal().getUsername())
              .orElseThrow(() -> new NotFoundException(Account.class));
      var file = fileRepository.findById(id).orElseThrow(() -> new NotFoundException(File.class));

      var authorized = false;
      if (account.hasRole("ROLE_ADMIN")) {
        authorized = true;
      } else {
        authorized =
            switch (file.getCategory()) {
              case FileCategory.ACCOUNT -> file.getOwner().equals(account.getId());
              case FileCategory.AUTHOR -> false;
              case FileCategory.RECIPE -> {
                var recipe =
                    recipeRepository
                        .findById(file.getOwner())
                        .orElseThrow(() -> new NotFoundException(Recipe.class));
                yield account.getId().equals(recipe.getAuthor().getAccount().getId());
              }
            };
      }

      if (!authorized) {
        throw new UnauthorizedException(File.class);
      }

      var deleted =
          Files.deleteIfExists(Paths.get(pathsConfig.content().getPath(), file.getPath()));

      if (deleted) {
        fileRepository.deleteById(id);
      }

      return deleted;
    } catch (Exception e) {
      throw new ConflictException(File.class);
    }
  }

  @Override
  public File findById(Long id) throws CustomException {
    return fileRepository.findById(id).orElseThrow(() -> new NotFoundException(File.class));
  }

  @Override
  public List<File> findAll() throws CustomException {
    return fileRepository.findAll();
  }

  @Override
  public File update(Long id, JsonNode json) throws CustomException {
    throw new UnsupportedOperationException();
  }
}
