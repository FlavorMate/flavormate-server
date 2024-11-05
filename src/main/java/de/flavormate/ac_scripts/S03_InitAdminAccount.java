/* Licensed under AGPLv3 2024 */
package de.flavormate.ac_scripts;

import de.flavormate.aa_interfaces.scripts.AScript;
import de.flavormate.ad_configurations.flavormate.AdminUserConfig;
import de.flavormate.ba_entities.account.repository.AccountRepository;
import de.flavormate.ba_entities.account.service.AccountService;
import de.flavormate.ba_entities.account.wrapper.AccountDraft;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Slf4j
@Service
public class S03_InitAdminAccount implements AScript {

  private final AdminUserConfig adminUserConfig;

  private final AccountRepository accountRepository;
  private final AccountService accountService;

  @Override
  public void run() throws Exception {

    if (accountRepository.count() > 0) {
      log.info("Skipping creating admin account");
      return;
    }

    try {
      var account =
          new AccountDraft(
              adminUserConfig.username(),
              adminUserConfig.displayName(),
              adminUserConfig.password(),
              adminUserConfig.mail());
      accountService.createAdmin(account);
      log.info("Admin account created");
    } catch (Exception e) {
      log.error("Admin account could not be created!");
      throw e;
    }
  }
}
