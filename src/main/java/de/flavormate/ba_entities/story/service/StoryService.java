package de.flavormate.ba_entities.story.service;

import com.fasterxml.jackson.databind.JsonNode;
import de.flavormate.aa_interfaces.services.BaseService;
import de.flavormate.aa_interfaces.services.ICRUDService;
import de.flavormate.aa_interfaces.services.IPageableService;
import de.flavormate.aa_interfaces.services.ISearchService;
import de.flavormate.ab_exeptions.exceptions.CustomException;
import de.flavormate.ab_exeptions.exceptions.NotFoundException;
import de.flavormate.ba_entities.story.model.Story;
import de.flavormate.ba_entities.story.repository.StoryRepository;
import de.flavormate.ba_entities.story.wrapper.StoryDraft;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StoryService extends BaseService implements ICRUDService<Story, StoryDraft>, IPageableService<Story>, ISearchService<Story> {
	private final StoryRepository repository;

	protected StoryService(StoryRepository repository) {
		this.repository = repository;
	}

	@Override
	public Story update(Long id, JsonNode json) throws CustomException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean deleteById(Long id) throws CustomException {
		throw new UnsupportedOperationException();
	}

	@Override
	public Story findById(Long id) throws CustomException {
		return repository.findById(id).orElseThrow(() -> new NotFoundException(Story.class));
	}

	@Override
	public List<Story> findAll() throws CustomException {
		return repository.findAll();
	}

	@Override
	public Page<Story> findBySearch(String searchTerm, Pageable pageable) {
		return repository.findBySearch(searchTerm, pageable);
	}

	@Override
	public Story create(StoryDraft object) throws CustomException {
		throw new UnsupportedOperationException();
	}

	@Override
	public Page<Story> findByPage(Pageable pageable) throws CustomException {
		return repository.findAll(pageable);
	}
}
