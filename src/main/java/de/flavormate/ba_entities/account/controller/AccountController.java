/* Licensed under AGPLv3 2024 */
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
import java.security.Principal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@Secured({"ROLE_USER"})
@RestController
@RequiredArgsConstructor
@RequestMapping("/v2/accounts")
public class AccountController implements ICRUDController<Account, AccountDraft> {

  private final AccountService accountService;

  @Secured({"ROLE_ADMIN"})
  @Override
  public Account create(@RequestBody AccountDraft form) throws CustomException {
    return accountService.create(form);
  }

  @Override
  public Account update(Long id, JsonNode form) throws CustomException {
    return accountService.update(id, form);
  }

  @Secured({"ROLE_ADMIN"})
  @Override
  public boolean deleteById(Long id) throws CustomException {
    return accountService.deleteById(id);
  }

  @Override
  public Account findById(Long id) throws CustomException {
    return accountService.findById(id);
  }

  @Secured("ROLE_ADMIN")
  @Override
  public List<Account> findAll() throws CustomException {
    return accountService.findAll();
  }

  @GetMapping("/info")
  public Account getInfo(Principal principal) throws NotFoundException {
    return accountService.getInfo(principal);
  }

  @Secured({"ROLE_ADMIN"})
  @PutMapping("/{id}/password/force")
  public Boolean forcePassword(@PathVariable Long id, @RequestBody ForcePasswordForm form)
      throws Exception {
    return accountService.forcePassword(id, form);
  }

  @PutMapping("/{id}/password")
  public Boolean changePassword(
      @PathVariable Long id, @RequestBody ChangePasswordForm form, Principal principal)
      throws NotFoundException, ConflictException {
    return accountService.updatePassword(form, principal);
  }
}
