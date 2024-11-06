/* Licensed under AGPLv3 2024 */
package de.flavormate.ab_exeptions.exceptions;

import org.springframework.http.HttpStatus;

public class NotAcceptableException extends CustomException {

  private static final HttpStatus STATUS = HttpStatus.NOT_ACCEPTABLE;

  public NotAcceptableException(Class<?> identifier) {
    super(identifier, STATUS);

    getResponse()
        .put("message", String.format("%s is not acceptable!", identifier.getSimpleName()));
  }
}
