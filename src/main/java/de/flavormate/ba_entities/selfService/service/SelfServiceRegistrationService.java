package de.flavormate.ba_entities.selfService.service;

import de.flavormate.ab_exeptions.exceptions.ConflictException;
import de.flavormate.ab_exeptions.exceptions.CustomException;
import de.flavormate.ba_entities.account.model.Account;
import de.flavormate.ba_entities.account.repository.AccountRepository;
import de.flavormate.ba_entities.account.wrapper.AccountDraft;
import de.flavormate.ba_entities.author.model.Author;
import de.flavormate.ba_entities.author.repository.AuthorRepository;
import de.flavormate.ba_entities.role.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * Service enabling self-service registration. When the feature toggle defined by the property
 * "flavormate.features.registration.enabled" is set to true, this service will be active.
 * It handles the logic of user account creation, including checks for existing email or username,
 * password encoding, and role assignment.
 * <p>
 * Utilizes the following repositories:
 * - AccountRepository: For checking existence and saving Account entities.
 * - AuthorRepository: For saving new Author entities associated with the created Account.
 * - RoleRepository: For fetching user roles.
 * <p>
 * This service is also dependent on a password encoder to hash user passwords.
 * <p>
 * The create method throws CustomException on error conditions such as conflict when the email
 * or username already exists.
 * <p>
 * Dependencies:
 * - AccountRepository accountRepository
 * - AuthorRepository authorRepository
 * - RoleRepository roleRepository
 * - PasswordEncoder passwordEncoder
 */
@ConditionalOnProperty(prefix = "flavormate.features.registration", value = "enabled", havingValue = "true")
@Service
@RequiredArgsConstructor
public class SelfServiceRegistrationService {

	private final AccountRepository accountRepository;
	private final AuthorRepository authorRepository;
	private final RoleRepository roleRepository;
	private final PasswordEncoder passwordEncoder;

	/**
	 * Creates a new account based on the provided account draft.
	 * The method checks if an account with the same email or username already exists and
	 * throws a ConflictException if true. It then encodes the password, assigns a default role,
	 * and saves the new account. Additionally, an associated author entity is created and saved.
	 *
	 * @param form the draft object containing information for account creation.
	 * @return the created Account entity.
	 * @throws CustomException if an account with the given email or username already exists.
	 */
	public boolean create(@RequestBody AccountDraft form) throws CustomException {
		var existsByMail = accountRepository.existsByMail(form.mail());
		var existsByUsername = accountRepository.existsByUsername(form.username());

		if (existsByMail || existsByUsername) {
			throw new ConflictException(Account.class);
		}

		var role = roleRepository.findByLabel("ROLE_USER").get();
		var password = passwordEncoder.encode(form.password());
		var account = Account.builder().displayName(form.displayName())
				.mail(form.mail()).password(password).roles(List.of(role))
				.username(form.username()).build();

		account = accountRepository.save(account);

		var author = Author.builder().account(account).build();

		authorRepository.save(author);

		return true;
	}
}
