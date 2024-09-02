package de.flavormate.ac_scripts;

import de.flavormate.aa_interfaces.scripts.AScript;
import de.flavormate.ba_entities.account.repository.AccountRepository;
import de.flavormate.ba_entities.account.service.AccountService;
import de.flavormate.ba_entities.account.wrapper.AccountDraft;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class S03_InitAdminAccount extends AScript {

	@Autowired
	private AccountRepository accountRepository;
	@Autowired
	private AccountService accountService;

	@Value("${flavorMate.admin.username}")
	private String username;

	@Value("${flavorMate.admin.displayname}")
	private String displayname;

	@Value("${flavorMate.admin.mail}")
	private String mail;

	@Value("${flavorMate.admin.password}")
	private String password;

	public S03_InitAdminAccount() {
		super("Initialize Admin Account");
	}

	@Override
	public void run() {

		if (accountRepository.count() > 0) {
			log("Skipping creating admin account");
			return;
		}

		try {
			var account = new AccountDraft(username, displayname, password, mail);
			accountService.createAdmin(account);
			log("Admin account created");
		} catch (Exception e) {
			warning("Admin account could not be created!");
		}
	}
}
