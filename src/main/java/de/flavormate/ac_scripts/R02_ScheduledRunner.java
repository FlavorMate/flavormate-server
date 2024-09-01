package de.flavormate.ac_scripts;

import de.flavormate.aa_interfaces.scripts.AScript;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class R02_ScheduledRunner extends AScript {
	// Runtime Services
	@Autowired
	private S21_HighlightGeneratorScript s21_highlightGenerator;

	// Cleaning Services
	@Autowired
	private S99_CleanScript s99_clean;

	public R02_ScheduledRunner() {
		super("Scheduled Runner");
	}

	@Scheduled(cron = "0 0 0 * * *")
	@Override
	public void run() {
		log("Running scheduled scripts");

		s21_highlightGenerator.run();

		s99_clean.run();

		log("Ran all scheduled scripts");
	}
}
