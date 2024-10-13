package de.flavormate.ac_scripts;

import de.flavormate.aa_interfaces.scripts.AScript;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Slf4j
@Service
public class R02_ScheduledRunner implements AScript {

	// Runtime Services
	private final S21_HighlightGeneratorScript s21_highlightGenerator;

	// Cleaning Services
	private final S99_CleanScript s99_clean;


	@Scheduled(cron = "0 0 0 * * *")
	@Override
	public void run() throws Exception {
		log.info("Running scheduled scripts");

		s21_highlightGenerator.run();

		s99_clean.run();

		log.info("Ran all scheduled scripts");
	}
}
