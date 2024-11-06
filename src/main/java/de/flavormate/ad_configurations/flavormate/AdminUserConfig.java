/* Licensed under AGPLv3 2024 */
package de.flavormate.ad_configurations.flavormate;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "flavormate.admin-user")
public record AdminUserConfig(String username, String displayName, String mail, String password) {}
