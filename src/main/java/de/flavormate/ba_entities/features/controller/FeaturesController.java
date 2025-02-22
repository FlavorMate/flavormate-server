/* Licensed under AGPLv3 2024 */
package de.flavormate.ba_entities.features.controller;

import de.flavormate.ba_entities.features.model.FeatureResponse;
import de.flavormate.ba_entities.features.service.FeaturesService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Secured({"ROLE_ANONYMOUS", "ROLE_USER"})
@RestController
@RequiredArgsConstructor
@RequestMapping("/v2/features")
public class FeaturesController {

  private final FeaturesService featuresService;

  @GetMapping("/")
  public FeatureResponse getFeatures() {
    return featuresService.getFeaturesConfig();
  }
}
