package de.flavormate.ba_entities.backup.controller;

import com.fasterxml.jackson.databind.JsonNode;
import de.flavormate.ba_entities.backup.model.BackupType;
import de.flavormate.ba_entities.backup.service.BackupService;
import de.flavormate.ba_entities.recipe.wrapper.ScrapeResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;

@RestController
@RequestMapping("/v2/backup")
@RequiredArgsConstructor
@Secured({"ROLE_ADMIN"})
public class BackupController {

	private final BackupService backupService;

	@GetMapping("/")
	public void backupAll(@RequestParam BackupType type, @RequestParam String language, HttpServletResponse response) throws IOException {
		// Perform a backup operation for the given type and language, returning a file
		var file = backupService.backupAll(type, language);

		// Probe the content type of the file
		String contentType = Files.probeContentType(file);

		if (contentType == null) {
			contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
		}

		// Set the content type in the HTTP response
		response.setContentType(contentType);

		// Set the content length in the HTTP response
		response.setContentLengthLong(Files.size(file));

		// Set the Content-Disposition header to indicate an attachment with the filename
		response.setHeader(HttpHeaders.CONTENT_DISPOSITION, ContentDisposition.attachment()
				.filename(file.getFileName().toString(), StandardCharsets.UTF_8)
				.build()
				.toString());

		// Copy the file to the response's output stream
		Files.copy(file, response.getOutputStream());
	}

	@PostMapping("/")
	public List<ScrapeResponse> restoreAll(@RequestParam BackupType type, @RequestBody List<JsonNode> result) throws Exception {
		return backupService.restoreAll(type, result);
	}
}