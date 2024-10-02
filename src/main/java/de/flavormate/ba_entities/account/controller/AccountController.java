package de.flavormate.ba_entities.account.controller;

import com.fasterxml.jackson.databind.JsonNode;
import de.flavormate.aa_interfaces.controllers.ICRUDController;
import de.flavormate.ab_exeptions.exceptions.ConflictException;
import de.flavormate.ab_exeptions.exceptions.CustomException;
import de.flavormate.ab_exeptions.exceptions.NotFoundException;
import de.flavormate.ba_entities.account.model.Account;
import de.flavormate.ba_entities.account.service.AccountService;
import de.flavormate.ba_entities.account.wrapper.AccountDraft;
import de.flavormate.ba_entities.account.wrapper.ChangePasswordForm;
import de.flavormate.ba_entities.account.wrapper.ForcePasswordForm;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Secured({"ROLE_USER"})
@RestController
@RequestMapping("/v2/accounts")
public class AccountController implements ICRUDController<Account, AccountDraft> {

	private final AccountService service;

	protected AccountController(AccountService service) {
		this.service = service;
	}

	@Secured({"ROLE_ADMIN"})
	@Override
	public Account create(@RequestBody AccountDraft form) throws CustomException {
		return service.create(form);
	}

	@Override
	public Account update(Long id, JsonNode form) throws CustomException {
		return service.update(id, form);
	}

	@Secured({"ROLE_ADMIN"})
	@Override
	public boolean deleteById(Long id) throws CustomException {
		return service.deleteById(id);
	}

	@Override
	public Account findById(Long id) throws CustomException {
		return service.findById(id);
	}

	@Secured("ROLE_ADMIN")
	@Override
	public List<Account> findAll() throws CustomException {
		return service.findAll();
	}

	@GetMapping("/info")
	public Account getInfo(Principal principal) throws NotFoundException {
		return service.getInfo(principal);
	}

	@Secured({"ROLE_ADMIN"})
	@PutMapping("/{id}/password/force")
	public Boolean forcePassword(@PathVariable Long id, @RequestBody ForcePasswordForm form)
			throws Exception {
		return service.forcePassword(id, form);
	}

	@PutMapping("/{id}/password")
	public Boolean changePassword(@PathVariable Long id, @RequestBody ChangePasswordForm form,
	                              Principal principal) throws NotFoundException, ConflictException {
		return service.updatePassword(form, principal);
	}
}
