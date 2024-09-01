package de.flavormate.aa_interfaces.services;

import com.fasterxml.jackson.databind.JsonNode;
import de.flavormate.ab_exeptions.exceptions.CustomException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ICRUDService<T, F> {
	@Transactional
	T create(F form) throws CustomException;

	@Transactional
	T update(Long id, JsonNode form) throws CustomException;

	@Transactional
	boolean deleteById(Long id) throws CustomException;

	T findById(Long id) throws CustomException;

	List<T> findAll() throws CustomException;
}
