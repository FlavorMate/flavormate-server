package de.flavormate.ba_entities.selfService.controller;

import de.flavormate.ab_exeptions.exceptions.NotFoundException;
import de.flavormate.ba_entities.account.wrapper.ForcePasswordForm;
import de.flavormate.ba_entities.selfService.service.SelfServiceRecoveryService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@ConditionalOnProperty(prefix = "flavormate.features.recovery", value = "enabled", havingValue = "true")
@Secured({"ROLE_ANONYMOUS"})
@RestController
@RequiredArgsConstructor
@RequestMapping("/v2/self-service/recovery")
public class SelfServiceRecoveryController {

	private final SelfServiceRecoveryService selfServiceRecoveryService;

	@PutMapping("/{mail}/password/reset")
	public boolean resetPassword(@PathVariable String mail)
			throws NotFoundException, MessagingException {
		return selfServiceRecoveryService.resetPassword(mail);
	}

	@GetMapping("/password/reset/{token}")
	public String resetPasswordPage(@PathVariable String token) {
		return selfServiceRecoveryService.resetPasswordPage(token);
	}

	@PostMapping(path = "/password/reset/{token}",
			consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
	public String resetPasswordConfirm(@PathVariable String token,
	                                   ForcePasswordForm form) throws NotFoundException {
		return selfServiceRecoveryService.resetPasswordConfirm(token, form);
	}
}
