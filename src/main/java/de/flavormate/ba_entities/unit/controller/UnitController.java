/* Licensed under AGPLv3 2024 */
package de.flavormate.ba_entities.unit.controller;

import com.fasterxml.jackson.databind.JsonNode;
import de.flavormate.aa_interfaces.controllers.ICRUDController;
import de.flavormate.ab_exeptions.exceptions.CustomException;
import de.flavormate.ba_entities.unit.model.UnitConversion;
import de.flavormate.ba_entities.unit.model.UnitLocalized;
import de.flavormate.ba_entities.unit.service.UnitService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v2/units")
public class UnitController implements ICRUDController<UnitLocalized, Object> {
  private final UnitService unitService;

  @Override
  public UnitLocalized create(Object form) throws CustomException {
    throw new UnsupportedOperationException();
  }

  @Override
  public UnitLocalized update(Long id, JsonNode form) throws CustomException {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean deleteById(Long id) throws CustomException {
    throw new UnsupportedOperationException();
  }

  @Override
  public UnitLocalized findById(Long id) throws CustomException {
    return unitService.findById(id);
  }

  @Override
  public List<UnitLocalized> findAll() throws CustomException {
    return unitService.findAll();
  }

  @GetMapping("/conversions")
  public List<UnitConversion> getAllConversions() {
    return unitService.getAllConversions();
  }
}
