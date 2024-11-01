/* Licensed under AGPLv3 2024 */
package de.flavormate.ac_scripts;

import de.flavormate.aa_interfaces.scripts.AScript;
import de.flavormate.ad_configurations.flavormate.PathsConfig;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Slf4j
@Service
public class S02_InitFilePathScript implements AScript {

  private final PathsConfig pathsConfig;

  @Override
  public void run() throws Exception {
    log.info("Start path initialization");

    createPath(pathsConfig.backup());
    createPath(pathsConfig.content());
    createPath(pathsConfig.logs());

    log.info("Finished path initialization");
  }

  private void createPath(URL path) throws Exception {
    try {
      Path root = Paths.get(path.getPath());
      if (!Files.exists(root)) {
        Files.createDirectories(root);
        log.info("Path created: {}", path.getPath());
      } else {
        log.info("Path exists already: {}", path.getPath());
      }
    } catch (Exception e) {
      log.error("Could not initialize path: {}", path.getPath());
      throw e;
    }
  }
}
