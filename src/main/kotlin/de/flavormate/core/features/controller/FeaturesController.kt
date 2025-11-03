/* Licensed under AGPLv3 2024 - 2025 */
package de.flavormate.core.features.controller

import de.flavormate.core.features.services.FeaturesService
import jakarta.enterprise.context.RequestScoped
import jakarta.ws.rs.GET
import jakarta.ws.rs.Path

@Path("/v3/features")
@RequestScoped
class FeaturesController(val service: FeaturesService) {

  @Path("/") @GET fun getEnabledFeatures() = service.getEnabledFeatures()
}
