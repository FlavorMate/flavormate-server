package de.flavormate.aa_interfaces.services;

import de.flavormate.ab_exeptions.exceptions.CustomException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ISearchService<T> {
	Page<T> findBySearch(String searchTerm, Pageable pageable) throws CustomException;


}
