package de.flavormate.ac_scripts;

import de.flavormate.aa_interfaces.scripts.AScript;
import de.flavormate.ad_configurations.FlavorMateConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Service
public class R01_StartupRunner extends AScript {
	@Autowired
	Environment env;

	@Autowired
	private FlavorMateConfig flavorMateConfig;

	// Startup Services
	@Autowired
	private S01_InitDatabaseScript s01_initDB;

	@Autowired
	private S02_InitFilePathScript s02_initFilePath;

	@Autowired
	private S03_InitAdminAccount s03_initAdminAccount;

	// Runtime Services
	@Autowired
	private S21_HighlightGeneratorScript s21_highlightGenerator;

	// Cleaning Services
	@Autowired
	private S99_CleanScript s99_clean;

	public R01_StartupRunner() {
		super("Startup Runner");
	}

	@EventListener(ContextRefreshedEvent.class)
	@Override
	public void run() {
		log("Running startup scripts");
		flavorMateConfig.init();

		s01_initDB.run();
		s02_initFilePath.run();
		s03_initAdminAccount.run();

		s21_highlightGenerator.run();

		s99_clean.run();

		log("Ran all startup scripts");
	}


}
