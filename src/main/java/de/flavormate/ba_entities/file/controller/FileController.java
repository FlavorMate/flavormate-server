package de.flavormate.ba_entities.file.controller;

import com.fasterxml.jackson.databind.JsonNode;
import de.flavormate.aa_interfaces.controllers.ICRUDController;
import de.flavormate.ab_exeptions.exceptions.CustomException;
import de.flavormate.ba_entities.file.model.File;
import de.flavormate.ba_entities.file.service.FileService;
import de.flavormate.ba_entities.file.wrapper.FileDraft;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v2/files")
public class FileController implements ICRUDController<File, FileDraft> {
	private final FileService fileService;

	@Override
	public File create(FileDraft form) throws CustomException {
		return fileService.create(form);
	}

	@Override
	public File update(Long id, JsonNode form) throws CustomException {
		return fileService.update(id, form);
	}

	@Override
	public boolean deleteById(Long id) throws CustomException {
		return fileService.deleteById(id);
	}

	@Override
	public File findById(Long id) throws CustomException {
		return fileService.findById(id);
	}

	@Secured({"ROLE_ADMIN"})
	@Override
	public List<File> findAll() throws CustomException {
		return fileService.findAll();
	}
}
