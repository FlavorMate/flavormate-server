package de.flavormate.ba_entities.account.service;

import com.fasterxml.jackson.databind.JsonNode;
import de.flavormate.aa_interfaces.services.BaseService;
import de.flavormate.aa_interfaces.services.ICRUDService;
import de.flavormate.ab_exeptions.exceptions.ConflictException;
import de.flavormate.ab_exeptions.exceptions.CustomException;
import de.flavormate.ab_exeptions.exceptions.NotFoundException;
import de.flavormate.ad_configurations.FlavorMateConfig;
import de.flavormate.ba_entities.account.model.Account;
import de.flavormate.ba_entities.account.repository.AccountRepository;
import de.flavormate.ba_entities.account.wrapper.AccountDraft;
import de.flavormate.ba_entities.account.wrapper.ChangePasswordForm;
import de.flavormate.ba_entities.account.wrapper.ForcePasswordForm;
import de.flavormate.ba_entities.author.model.Author;
import de.flavormate.ba_entities.author.repository.AuthorRepository;
import de.flavormate.ba_entities.book.repository.BookRepository;
import de.flavormate.ba_entities.email.model.PasswordRecoveryEMail;
import de.flavormate.ba_entities.email.service.EmailService2;
import de.flavormate.ba_entities.file.model.File;
import de.flavormate.ba_entities.recipe.enums.RecipeDiet;
import de.flavormate.ba_entities.role.repository.RoleRepository;
import de.flavormate.ba_entities.token.model.Token;
import de.flavormate.ba_entities.token.repository.TokenRepository;
import de.flavormate.utils.JSONUtils;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.AbstractMap;
import java.util.List;
import java.util.Map;

@Service
public class AccountService extends BaseService implements ICRUDService<Account, AccountDraft> {
	private final AccountRepository accountRepository;
	private final AuthorRepository authorRepository;
	private final BookRepository bookRepository;
	private final RoleRepository roleRepository;
	private final TokenRepository tokenRepository;
	private final EmailService2 emailService;
	private final PasswordEncoder passwordEncoder;

	public AccountService(AccountRepository accountRepository, AuthorRepository authorRepository, BookRepository bookRepository, RoleRepository roleRepository, TokenRepository tokenRepository, EmailService2 emailService, PasswordEncoder passwordEncoder) {
		this.accountRepository = accountRepository;
		this.authorRepository = authorRepository;
		this.bookRepository = bookRepository;
		this.roleRepository = roleRepository;
		this.tokenRepository = tokenRepository;
		this.emailService = emailService;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public Account create(AccountDraft newAccount) throws CustomException {
		Boolean existsByMail = accountRepository.existsByMail(newAccount.mail());
		Boolean existsByUsername = accountRepository.existsByUsername(newAccount.username());

		if (existsByMail || existsByUsername) {
			throw new ConflictException(Account.class);
		}

		var role = roleRepository.findByLabel("ROLE_USER").get();
		var password = passwordEncoder.encode(newAccount.password());
		var account = Account.builder().displayName(newAccount.displayName())
				.mail(newAccount.mail()).password(password).roles(List.of(role))
				.username(newAccount.username()).build();

		account = accountRepository.save(account);

		var author = Author.builder().account(account).build();

		authorRepository.save(author);

		return account;
	}

	@Transactional
	public void createAdmin(AccountDraft newAccount) throws CustomException {
		var adminRole = roleRepository.findByLabel("ROLE_ADMIN").get();

		var account = create(newAccount);
		account.setValid(true);
		account.getRoles().add(adminRole);
		accountRepository.save(account);
	}

	@Override
	public Account update(Long id, JsonNode json) throws CustomException {
		var account = this.findById(id);

		var principal = accountRepository.findByUsername(getPrincipal().getUsername()).get();

		if (!principal.hasRole("ROLE_ADMIN") && !principal.getUsername().equals(account.getUsername()))
			throw new NotFoundException(Account.class);

		if (json.has("avatar")) {
			var avatar = JSONUtils.parseObject(json.get("avatar"), File.class);
			account.setAvatar(avatar);
		}

		if (json.has("diet")) {
			var diet = JSONUtils.parseObject(json.get("diet"), RecipeDiet.class);
			account.setDiet(diet);
		}

		if (json.has("displayName")) {
			var displayName = JSONUtils.parseObject(json.get("displayName"), String.class);
			account.setDisplayName(displayName);
		}

		if (json.has("mail")) {
			var mail = JSONUtils.parseObject(json.get("mail"), String.class);
			account.setMail(mail);
		}

		if (principal.hasRole("ROLE_ADMIN") && json.has("valid")) {
			var valid = JSONUtils.parseObject(json.get("valid"), Boolean.class);
			account.setValid(valid);
		}

		return accountRepository.save(account);
	}

	@Override
	@Transactional(rollbackOn = {CustomException.class})
	public boolean deleteById(Long id) throws CustomException {
		try {
			var author = authorRepository.findByAccountId(id)
					.orElseThrow(() -> new NotFoundException(Author.class));


			for (var book : author.getBooks()) {
				book.getRecipes().clear();
				bookRepository.save(book);
			}

			author.getBooks().clear();
			author.getRecipes().clear();
			authorRepository.save(author);

			authorRepository.delete(author);

			accountRepository.deleteById(id);
		} catch (Exception e) {
			throw new ConflictException(Account.class);
		}

		return true;
	}

	@Override
	public Account findById(Long id) throws CustomException {
		return accountRepository.findById(id).orElseThrow(() -> new NotFoundException(Account.class));
	}

	@Override
	public List<Account> findAll() throws CustomException {
		return accountRepository.findAll();
	}

	public Account getInfo(Principal principal) throws NotFoundException {
		var account = accountRepository.findByUsername(principal.getName())
				.orElseThrow(() -> new NotFoundException(Account.class));

		account.setPassword("");

		return account;
	}

	public Boolean forcePassword(Long id, ForcePasswordForm form) throws CustomException {
		var account = findById(id);

		account.setPassword(passwordEncoder.encode(form.password()));

		accountRepository.save(account);

		return true;
	}

	public Boolean updatePassword(ChangePasswordForm form, Principal principal)
			throws NotFoundException, ConflictException {
		var account = accountRepository.findByUsername(principal.getName())
				.orElseThrow(() -> new NotFoundException(Account.class));

		var matching = passwordEncoder.matches(form.oldPassword(), account.getPassword());

		if (!matching)
			throw new ConflictException(Account.class);

		account.setPassword(passwordEncoder.encode(form.newPassword()));

		accountRepository.save(account);

		return true;
	}

	public Boolean resetPassword(String mail) throws NotFoundException, MessagingException {
		var account = accountRepository.findByMail(mail)
				.orElseThrow(() -> new NotFoundException(Account.class));

		Token token = tokenRepository.save(Token.PasswordToken(account));

		Map<String, Object> map =
				Map.ofEntries(new AbstractMap.SimpleEntry<>("username", account.getUsername()),
						new AbstractMap.SimpleEntry<>("name", account.getDisplayName()),
						new AbstractMap.SimpleEntry<>("token", token.getToken()),
						new AbstractMap.SimpleEntry<>("baseUrl", FlavorMateConfig.getFrontendUrl())

				);

		emailService.sendMail(new PasswordRecoveryEMail(account.getMail(), map));

		return true;
	}

	public Boolean resetPasswordConfirm(String tokenId, ForcePasswordForm form)
			throws NotFoundException {
		Token token = tokenRepository.findByToken(tokenId)
				.orElseThrow(() -> new NotFoundException(Token.class));

		Account account = accountRepository.findById(token.getOwner().getId())
				.orElseThrow(() -> new NotFoundException(Account.class));

		account.setPassword(passwordEncoder.encode(form.password()));

		accountRepository.save(account);

		tokenRepository.deleteById(token.getId());

		return true;
	}


}
