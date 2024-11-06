/* Licensed under AGPLv3 2024 */
package de.flavormate.ad_configurations.auth.jwt;

import de.flavormate.ba_entities.account.model.Account;
import de.flavormate.ba_entities.account.repository.AccountRepository;
import de.flavormate.ba_entities.role.model.Role;
import eu.fraho.spring.securityJwt.base.dto.JwtUser;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

/**
 * A custom implementation of the {@link UserDetailsService} interface that retrieves user
 * information from the database and maps it to a {@link UserDetails} object for authentication
 * purposes.
 */
@RequiredArgsConstructor
@Component
public class CustomUserDetailsService implements UserDetailsService {

  private final AccountRepository accountRepository;

  /**
   * Loads user information for the specified username from the database and returns a {@link
   * UserDetails} object representing the user.
   *
   * @param username The username of the user to be loaded.
   * @return A `UserDetails` object representing the user
   */
  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

    Account account =
        accountRepository
            .findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("Account not found"));

    JwtUser user = new JwtUser();
    user.setUsername(account.getUsername());
    user.setPassword(account.getPassword());
    user.setAuthorities(getAuthorities(account.getRoles()));
    user.setEnabled(account.getValid());
    user.setAccountNonExpired(true);
    user.setAccountNonLocked(true);
    user.setApiAccessAllowed(true);
    user.setCredentialsNonExpired(true);
    return user;
  }

  private List<GrantedAuthority> getAuthorities(List<Role> roles) {
    var authorieties =
        roles.stream()
            .map(role -> (GrantedAuthority) new SimpleGrantedAuthority(role.getLabel()))
            .toList();
    return authorieties;
  }
}
