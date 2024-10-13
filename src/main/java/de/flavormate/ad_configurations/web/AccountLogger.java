package de.flavormate.ad_configurations.web;

import de.flavormate.ab_exeptions.exceptions.NotFoundException;
import de.flavormate.ba_entities.account.model.Account;
import de.flavormate.ba_entities.account.repository.AccountRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.Instant;

@RequiredArgsConstructor
@Component
@Slf4j
public class AccountLogger implements HandlerInterceptor {

	private final AccountRepository accountRepository;

	@Override
	public boolean preHandle(@Nullable HttpServletRequest request, @Nullable HttpServletResponse response,
	                         @Nullable Object handler) {

		if (!(getAuthentication().getPrincipal() instanceof UserDetails))
			return true;

		try {
			var account = accountRepository.findByUsername(getUsername())
					.orElseThrow(() -> new NotFoundException(Account.class));
			account.setLastActivity(Instant.now());
			accountRepository.save(account);
		} catch (Exception e) {
			log.warn("AccountLogger: User {} could not be updated", getUsername());
		}
		return true;
	}


	private Authentication getAuthentication() {
		return SecurityContextHolder.getContext().getAuthentication();
	}

	private String getUsername() {
		if (getAuthentication().getPrincipal() instanceof UserDetails)
			return ((UserDetails) getAuthentication().getPrincipal()).getUsername();
		else
			return (String) getAuthentication().getPrincipal();
	}

}
