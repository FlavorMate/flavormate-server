package de.flavormate.ad_configurations.error;

import de.flavormate.ad_configurations.FlavorMateConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Controller
@RequiredArgsConstructor
public class ErrorController implements org.springframework.boot.web.servlet.error.ErrorController {
	private final SpringTemplateEngine templateEngine;
	private final FlavorMateConfig flavorMateConfig;

	@RequestMapping("/error")
	public String handleError(Model model) {
		model.addAttribute("baseUrl", FlavorMateConfig.getFrontendUrl());
		return "error";
	}
}
