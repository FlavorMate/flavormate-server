package de.flavormate.ba_entities.file.service;

import com.fasterxml.jackson.databind.JsonNode;
import de.flavormate.aa_interfaces.services.BaseService;
import de.flavormate.aa_interfaces.services.ICRUDService;
import de.flavormate.ab_exeptions.exceptions.ConflictException;
import de.flavormate.ab_exeptions.exceptions.CustomException;
import de.flavormate.ab_exeptions.exceptions.NotFoundException;
import de.flavormate.ba_entities.account.model.Account;
import de.flavormate.ba_entities.account.repository.AccountRepository;
import de.flavormate.ba_entities.file.model.File;
import de.flavormate.ba_entities.file.repository.FileRepository;
import de.flavormate.ba_entities.file.wrapper.FileDraft;
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

	@Value("${flavorMate.files}")
	private URL ROOT;

	protected FileService(FileRepository repository, AccountRepository accountRepository) {
		this.repository = repository;
		this.accountRepository = accountRepository;
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
			var owner = accountRepository.findByUsername(getPrincipal().getUsername()).orElseThrow(() -> new NotFoundException(Account.class));
			var file = repository.findById(id, owner.getId()).orElseThrow(() -> new NotFoundException(File.class));

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
