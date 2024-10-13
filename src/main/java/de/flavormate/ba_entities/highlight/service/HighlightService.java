package de.flavormate.ba_entities.highlight.service;

import de.flavormate.aa_interfaces.services.BaseService;
import de.flavormate.ba_entities.highlight.model.Highlight;
import de.flavormate.ba_entities.highlight.repository.HighlightRepository;
import de.flavormate.ba_entities.recipe.enums.RecipeDiet;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class HighlightService extends BaseService {
	private final HighlightRepository highlightRepository;


	public Page<Highlight> findByPageAndDiet(Pageable pageable, RecipeDiet filter) {
		return highlightRepository.findAllByDiet(filter, pageable);
	}
}
