/* Licensed under AGPLv3 2024 */
package de.flavormate.ad_configurations.auth;

import de.flavormate.ad_configurations.auth.filters.UserDetailsEnabledFilter;
import de.flavormate.ba_entities.account.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class CustomSecurityConfig {

  private final AccountRepository repository;

  @Bean
  public FilterRegistrationBean<UserDetailsEnabledFilter> userDetailsEnabledFilterBean() {
    FilterRegistrationBean<UserDetailsEnabledFilter> registrationBean =
        new FilterRegistrationBean<>();
    registrationBean.setFilter(new UserDetailsEnabledFilter(repository));
    registrationBean.setOrder(1); // Set the order of the filter in the chain
    registrationBean.addInitParameter("afterFilters", "UsernamePasswordAuthenticationFilter");
    return registrationBean;
  }
}
