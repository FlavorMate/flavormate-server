package de.flavormate.ad_configurations.auth;

import de.flavormate.ad_configurations.auth.filters.UserDetailsEnabledFilter;
import de.flavormate.ba_entities.account.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CustomSecurityConfig {

	@Autowired
	private AccountRepository repository;

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
