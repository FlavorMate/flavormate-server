/* Licensed under AGPLv3 2024 */
package de.flavormate.ba_entities.openFoodFacts.service;

import de.flavormate.ad_configurations.flavormate.CommonConfig;
import de.flavormate.ad_configurations.flavormate.MailConfig;
import de.flavormate.ba_entities.nutrition.wrapper.NutritionDraft;
import de.flavormate.ba_entities.openFoodFacts.model.OpenFoodFactsProduct;
import de.flavormate.ba_entities.openFoodFacts.model.OpenFoodFactsResponse;
import java.util.Collections;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@ConditionalOnProperty({
  "flavormate.features.open-food-facts.enabled",
  "flavormate.features.recovery.enabled"
})
@Service
@RequiredArgsConstructor
public class OpenFoodFactsService {
  private final CommonConfig commonConfig;
  private final MailConfig mailConfig;

  public NutritionDraft fetchProduct(String barcode) {
    RestTemplate restTemplate = new RestTemplate();

    String uri =
        "https://world.openfoodfacts.org/api/v2/product/" + barcode + ".json?fields=nutriments";

    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    headers.add(
        "user-agent",
        "FlavorMate/" + commonConfig.getVersion().toString() + " (" + mailConfig.from() + ")");

    HttpEntity<String> entity = new HttpEntity<>(headers);

    var response = restTemplate.exchange(uri, HttpMethod.GET, entity, OpenFoodFactsResponse.class);

    var nutriments =
        Optional.ofNullable(response.getBody())
            .map(OpenFoodFactsResponse::product)
            .map(OpenFoodFactsProduct::nutriments)
            .orElse(null);

    if (nutriments == null) return null;

    return new NutritionDraft(
        barcode,
        nutriments.carbohydrates(),
        nutriments.energy_kcal(),
        nutriments.fat(),
        nutriments.saturatedFat(),
        nutriments.sugars(),
        nutriments.fiber(),
        nutriments.proteins(),
        nutriments.salt(),
        nutriments.sodium());
  }
}
