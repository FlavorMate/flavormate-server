/* Licensed under AGPLv3 2024 */
package de.flavormate.aa_interfaces.controllers;

import de.flavormate.ab_exeptions.exceptions.CustomException;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

public interface IPageableL10nController<T> {
  @GetMapping("/list")
  Page<T> findByPage(
      @RequestParam String language,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "6") int size,
      @RequestParam(defaultValue = "id") String sortBy,
      @RequestParam(defaultValue = "DESC") String sortDirection)
      throws CustomException;
}
