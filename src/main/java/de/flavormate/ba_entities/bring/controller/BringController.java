package de.flavormate.ba_entities.bring.controller;

import de.flavormate.ba_entities.bring.service.BringService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@ConditionalOnProperty(prefix = "flavormate.features.bring", value = "enabled", havingValue = "true")
@Secured({"ROLE_USER", "ROLE_ANONYMOUS"})
@RestController
@RequiredArgsConstructor
@RequestMapping("/v2/bring")
public class BringController {

	private final BringService bringService;

	@GetMapping("/{id}/{serving}")
	public String getBring(@PathVariable Long id, @PathVariable("serving") Integer serving)
			throws Exception {
		return bringService.get(id, serving);
	}
}
