/* Licensed under AGPLv3 2024 */
package de.flavormate.aa_interfaces.services;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public class BaseService {
  protected Authentication getAuthentication() {
    return SecurityContextHolder.getContext().getAuthentication();
  }

  protected UserDetails getPrincipal() {
    return (UserDetails) getAuthentication().getPrincipal();
  }
}
