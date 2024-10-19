package de.flavormate.bb_thymeleaf;

import de.flavormate.ad_configurations.flavormate.CommonConfig;
import org.springframework.context.i18n.LocaleContextHolder;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Map;

public class MainPage {
	private final TemplateEngine templateEngine;
	private final Context context;

	public MainPage(TemplateEngine templateEngine, CommonConfig commonConfig) {
		this.context = new Context(LocaleContextHolder.getLocale());
		this.templateEngine = templateEngine;

		context.setVariable("frontendUrl", commonConfig.getFrontendUrl());
		context.setVariable("backendUrl", commonConfig.getBackendUrl());
	}

	public String process(Fragments fragment, Map<String, Object> args) {
		if (args != null)
			context.setVariables(args);

		context.setVariable("content", fragment.getFile());
		return templateEngine.process("layouts/main.html", context);
	}
}
