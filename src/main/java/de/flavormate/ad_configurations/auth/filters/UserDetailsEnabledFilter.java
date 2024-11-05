/* Licensed under AGPLv3 2024 */
package de.flavormate.ad_configurations.auth.filters;

import de.flavormate.ba_entities.account.repository.AccountRepository;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

@RequiredArgsConstructor
public class UserDetailsEnabledFilter implements Filter {
  private final AccountRepository repository;

  private final PathMatcher pathMatcher = new AntPathMatcher();

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    // Check for login request and skip if true
    if (pathMatcher.match("/api/auth/login", ((HttpServletRequest) request).getRequestURI())) {
      chain.doFilter(request, response);
      return;
    }

    // Get the authenticated user from SecurityContextHolder
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    // Check if principal is an instance of UserDetails and if not, skip further processing
    if (authentication == null
        || !(authentication.getPrincipal() instanceof UserDetails userDetails)) {
      chain.doFilter(request, response);
      return;
    }

    // Retrieve account information from repository
    var account = repository.findByUsername(userDetails.getUsername()).orElse(null);

    // Check if account exists and is valid
    if (account == null || !account.getValid()) {
      throw new DisabledException("User is not enabled");
    }

    // Allow access if account is valid
    chain.doFilter(request, response);
  }
}
