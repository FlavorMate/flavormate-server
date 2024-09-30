package de.flavormate.ad_configurations.error;

import de.flavormate.ad_configurations.FlavorMateConfig;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ErrorController implements org.springframework.boot.web.servlet.error.ErrorController {

	/**
	 * Handles error requests by populating the model with necessary URLs.
	 *
	 * @param model the model to which the frontend and backend URLs will be added
	 * @return the view name to be rendered, in this case, "error"
	 */
	@RequestMapping("/error")
	public String handleError(Model model) {
		model.addAttribute("frontendUrl", FlavorMateConfig.getFrontendUrl());
		model.addAttribute("backendUrl", FlavorMateConfig.getBackendUrl());
		return "error";
	}
}
