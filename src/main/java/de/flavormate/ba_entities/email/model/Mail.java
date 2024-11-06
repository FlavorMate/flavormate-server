/* Licensed under AGPLv3 2024 */
package de.flavormate.ba_entities.email.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public abstract class Mail {
  private final String to;
  private final String subject;
}
