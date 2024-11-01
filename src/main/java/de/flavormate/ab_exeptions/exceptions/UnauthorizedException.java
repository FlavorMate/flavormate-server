/* Licensed under AGPLv3 2024 */
package de.flavormate.ab_exeptions.exceptions;

import org.springframework.http.HttpStatus;

public class UnauthorizedException extends CustomException {

  private static final HttpStatus STATUS = HttpStatus.UNAUTHORIZED;

  public UnauthorizedException(Class<?> identifier) {
    super(identifier, STATUS);

    getResponse()
        .put("message", String.format("%s is not authorized!", identifier.getSimpleName()));
  }
}
