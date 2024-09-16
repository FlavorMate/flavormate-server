package de.flavormate.ba_entities.file.service;

import com.fasterxml.jackson.databind.JsonNode;
import de.flavormate.aa_interfaces.services.BaseService;
import de.flavormate.aa_interfaces.services.ICRUDService;
import de.flavormate.ab_exeptions.exceptions.ConflictException;
import de.flavormate.ab_exeptions.exceptions.CustomException;
import de.flavormate.ab_exeptions.exceptions.NotFoundException;
import de.flavormate.ab_exeptions.exceptions.UnauthorizedException;
import de.flavormate.ba_entities.account.model.Account;
import de.flavormate.ba_entities.account.repository.AccountRepository;
import de.flavormate.ba_entities.file.enums.FileCategory;
import de.flavormate.ba_entities.file.model.File;
import de.flavormate.ba_entities.file.repository.FileRepository;
import de.flavormate.ba_entities.file.wrapper.FileDraft;
import de.flavormate.ba_entities.recipe.model.Recipe;
import de.flavormate.ba_entities.recipe.repository.RecipeRepository;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@Service
public class FileService extends BaseService implements ICRUDService<File, FileDraft> {

	private final FileRepository repository;
	private final AccountRepository accountRepository;
	private final RecipeRepository recipeRepository;

	@Value("${flavorMate.files}")
	private URL ROOT;

	protected FileService(FileRepository repository, AccountRepository accountRepository, RecipeRepository recipeRepository) {
		this.repository = repository;
		this.accountRepository = accountRepository;
		this.recipeRepository = recipeRepository;
	}

	@Override
	public File create(FileDraft body) throws CustomException {
		try {
			var file = File.builder().category(body.category()).owner(body.owner())
					.type(body.type()).build();

			file = repository.save(file);

			var content = body.content().split(",")[1];

			var path = Paths.get(ROOT.getPath(), file.getPath());

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
			var account = accountRepository.findByUsername(getPrincipal().getUsername()).orElseThrow(() -> new NotFoundException(Account.class));
			var file = repository.findById(id).orElseThrow(() -> new NotFoundException(File.class));

			var authorized = false;
			if (account.hasRole("ROLE_ADMIN")) {
				authorized = true;
			} else {
				authorized = switch (file.getCategory()) {
					case FileCategory.ACCOUNT -> file.getOwner().equals(account.getId());
					case FileCategory.AUTHOR -> false;
					case FileCategory.RECIPE -> {
						var recipe = recipeRepository.findById(file.getOwner()).orElseThrow(() -> new NotFoundException(Recipe.class));
						yield account.getId().equals(recipe.getAuthor().getAccount().getId());
					}
				};
			}

			if (!authorized) {
				throw new UnauthorizedException(File.class);
			}


			var deleted = Files.deleteIfExists(Paths.get(ROOT.getPath(), file.getPath()));

			if (deleted) {
				repository.deleteById(id);
			}

			return deleted;
		} catch (Exception e) {
			throw new ConflictException(File.class);
		}
	}

	@Override
	public File findById(Long id) throws CustomException {
		return repository.findById(id).orElseThrow(() -> new NotFoundException(File.class));
	}

	@Override
	public List<File> findAll() throws CustomException {
		return repository.findAll();
	}


	@Override
	public File update(Long id, JsonNode json) throws CustomException {
		throw new UnsupportedOperationException();
	}
}
