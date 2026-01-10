/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.extensions.registration.controllers

import de.flavormate.extensions.registration.models.RegistrationForm
import de.flavormate.extensions.registration.services.RegistrationService
import de.flavormate.features.role.enums.RoleTypes
import io.quarkus.arc.lookup.LookupIfProperty
import jakarta.annotation.security.RolesAllowed
import jakarta.enterprise.context.RequestScoped
import jakarta.ws.rs.GET
import jakarta.ws.rs.POST
import jakarta.ws.rs.Path
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType
import org.jboss.resteasy.reactive.RestPath

@LookupIfProperty(name = "flavormate.features.registration.enabled", stringValue = "true")
@RequestScoped
@Path("/v3/registration")
class RegistrationController(val service: RegistrationService) {

  @Path("/") @POST fun register(form: RegistrationForm) = service.register(form)

  @RolesAllowed(RoleTypes.VERIFY_VALUE)
  @Path("/verify/{token}")
  @Produces(MediaType.TEXT_HTML)
  @GET
  fun verifyAccount(@RestPath token: String) = service.verifyAccount()
}
