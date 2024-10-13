package de.flavormate.ba_entities.selfService.controller;

import de.flavormate.ab_exeptions.exceptions.CustomException;
import de.flavormate.ba_entities.account.wrapper.AccountDraft;
import de.flavormate.ba_entities.selfService.service.SelfServiceRegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * SelfServiceRegistrationController handles the self-service registration process.
 * This controller is activated based on the property "flavormate.features.registration.enabled"
 * and allows anonymous users to create accounts through a REST API endpoint.
 */
@ConditionalOnProperty(prefix = "flavormate.features.registration", value = "enabled", havingValue = "true")
@Secured({"ROLE_ANONYMOUS"})
@RestController
@RequiredArgsConstructor
@RequestMapping("/v2/self-service/registration")
public class SelfServiceRegistrationController {

	private final SelfServiceRegistrationService selfServiceRegistrationService;

	/**
	 * Creates a new account based on the provided account draft.
	 *
	 * @param form the draft object containing information for account creation.
	 * @return the created Account entity.
	 * @throws CustomException if an account with the given email or username already exists.
	 */
	@PostMapping("/")
	public boolean create(@RequestBody AccountDraft form) throws CustomException {
		return selfServiceRegistrationService.create(form);
	}
}
