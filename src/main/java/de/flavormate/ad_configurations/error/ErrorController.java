/* Licensed under AGPLv3 2024 */
package de.flavormate.ad_configurations.error;

import de.flavormate.ad_configurations.flavormate.CommonConfig;
import de.flavormate.bb_thymeleaf.Fragments;
import de.flavormate.bb_thymeleaf.MainPage;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.TemplateEngine;

@RestController
@RequiredArgsConstructor
public class ErrorController implements org.springframework.boot.web.servlet.error.ErrorController {

  private final TemplateEngine templateEngine;
  private final CommonConfig commonConfig;

  @RequestMapping("/error")
  public String handleError() {
    return new MainPage(templateEngine, commonConfig).process(Fragments.ERROR, null);
  }
}
