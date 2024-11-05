/* Licensed under AGPLv3 2024 */
package de.flavormate.ac_scripts;

import de.flavormate.aa_interfaces.scripts.AScript;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Slf4j
@Service
public class R01_StartupRunner implements AScript {

  // Startup Services
  private final S01_InitDatabaseScript s01_initDB;
  private final S02_InitFilePathScript s02_initFilePath;
  private final S03_InitAdminAccount s03_initAdminAccount;

  // Migrations
  private final S11_MigrateIngredientsToV2 s11_migrate_ingredients_to_V2;

  // Runtime Services
  private final S21_HighlightGeneratorScript s21_highlightGenerator;

  // Cleaning Services
  private final S99_CleanScript s99_clean;

  @EventListener(ContextRefreshedEvent.class)
  @Override
  public void run() throws Exception {
    log.info("Running startup scripts");

    s01_initDB.run();
    s02_initFilePath.run();
    s03_initAdminAccount.run();

    s11_migrate_ingredients_to_V2.run();

    s21_highlightGenerator.run();

    s99_clean.run();

    log.info("Ran all startup scripts");
  }
}
