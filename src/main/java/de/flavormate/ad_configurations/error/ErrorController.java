package de.flavormate.ad_configurations.error;

import de.flavormate.ad_configurations.flavormate.CommonConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
public class ErrorController implements org.springframework.boot.web.servlet.error.ErrorController {

	private final CommonConfig commonConfig;

	/**
	 * Handles error requests by populating the model with necessary URLs.
	 *
	 * @param model the model to which the frontend and backend URLs will be added
	 * @return the view name to be rendered, in this case, "error"
	 */
	@RequestMapping("/error")
	public String handleError(Model model) {
		model.addAttribute("frontendUrl", commonConfig.frontendUrl());
		model.addAttribute("backendUrl", commonConfig.backendUrl());
		return "error";
	}
}
