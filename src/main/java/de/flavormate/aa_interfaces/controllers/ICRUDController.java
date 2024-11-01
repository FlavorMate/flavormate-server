/* Licensed under AGPLv3 2024 */
package de.flavormate.aa_interfaces.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import de.flavormate.ab_exeptions.exceptions.CustomException;
import java.util.List;
import org.springframework.web.bind.annotation.*;

public interface ICRUDController<T, F> {
  @PostMapping("/")
  T create(@RequestBody F form) throws CustomException;

  @PutMapping("/{id}")
  T update(@PathVariable Long id, @RequestBody JsonNode form) throws CustomException;

  @DeleteMapping("/{id}")
  boolean deleteById(@PathVariable Long id) throws CustomException;

  @GetMapping("/{id}")
  T findById(@PathVariable Long id) throws CustomException;

  @GetMapping("/")
  List<T> findAll() throws CustomException;
}
