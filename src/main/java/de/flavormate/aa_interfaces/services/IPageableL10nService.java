package de.flavormate.aa_interfaces.services;

import de.flavormate.ab_exeptions.exceptions.CustomException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IPageableL10nService<T> {
	Page<T> findByPage(String language, Pageable pageable) throws CustomException;
}
