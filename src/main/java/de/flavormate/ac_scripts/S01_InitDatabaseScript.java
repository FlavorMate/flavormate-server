package de.flavormate.ac_scripts;

import com.fasterxml.jackson.core.type.TypeReference;
import de.flavormate.aa_interfaces.models.LocalizationId;
import de.flavormate.aa_interfaces.scripts.AScript;
import de.flavormate.ba_entities.category.model.Category;
import de.flavormate.ba_entities.category.model.CategoryLocalization;
import de.flavormate.ba_entities.category.repository.CategoryLocalizationRepository;
import de.flavormate.ba_entities.category.repository.CategoryRepository;
import de.flavormate.ba_entities.categoryGroup.model.CategoryGroup;
import de.flavormate.ba_entities.categoryGroup.model.CategoryGroupLocalization;
import de.flavormate.ba_entities.categoryGroup.repository.CategoryGroupLocalizationRepository;
import de.flavormate.ba_entities.categoryGroup.repository.CategoryGroupRepository;
import de.flavormate.ba_entities.role.model.Role;
import de.flavormate.ba_entities.role.repository.RoleRepository;
import de.flavormate.utils.JSONUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Slf4j
@Service
public class S01_InitDatabaseScript implements AScript {

	private final RoleRepository roleRepository;
	private final CategoryGroupRepository categoryGroupRepository;
	private final CategoryRepository categoryRepository;
	private final CategoryLocalizationRepository categoryLocalizationRepository;
	private final CategoryGroupLocalizationRepository categoryGroupLocalizationRepository;

	@Value("classpath:initialization/categories.json")
	private Resource categoriesFile;
	@Value("classpath*:initialization/l10n/categories/*.json")
	private Resource[] categoryLocalizationsFiles;
	@Value("classpath:initialization/roles.json")
	private Resource rolesFile;
	@Value("classpath:initialization/category_groups.json")
	private Resource categoryGroupsFile;
	@Value("classpath*:initialization/l10n/categoryGroups/*.json")
	private Resource[] categoryGroupsLocalizationsFiles;


	public void run() throws Exception {
		log.info("Starting database initialization");

		initializeRoles();
		initializeCategoryGroups();
		initializeCategoryGroupLocalizations();
		initializeCategories();
		initializeCategoryLocalizations();

		log.info("Finished database initialization");
	}


	private void initializeRoles() throws Exception {
		try {
			if (roleRepository.count() > 0) {
				log.info("Skipping roles initialization");
				return;
			}

			log.info("Reading roles from JSON file");
			Role[] rolesArr = JSONUtils.mapper.readValue(rolesFile.getInputStream(), Role[].class);
			List<Role> roles = Arrays.asList(rolesArr);

			log.info("Found {} roles", roles.size());

			log.info("Saving roles into the database");
			roleRepository.saveAll(roles);

			log.info("Saved {} roles", roles.size());
		} catch (Exception e) {
			log.error("Roles could not be initialized");
			throw e;
		}
	}

	private void initializeCategoryGroups() throws Exception {
		try {
			if (categoryGroupRepository.count() > 0) {
				log.info("Skipping category group initialization");
				return;
			}

			log.info("Reading category groups from JSON file");
			CategoryGroup[] categoryGroupsArr = JSONUtils.mapper
					.readValue(categoryGroupsFile.getInputStream(), CategoryGroup[].class);
			List<CategoryGroup> categoryGroups = Arrays.asList(categoryGroupsArr);

			log.info("Found {} category groups", categoryGroups.size());

			log.info("Saving category groups into the database");
			categoryGroupRepository.saveAll(categoryGroups);

			log.info("Saved {} category groups", categoryGroups.size());
		} catch (Exception e) {
			log.error("Category groups could not be initialized");
			throw e;
		}
	}

	private void initializeCategories() throws Exception {
		try {
			if (categoryRepository.count() > 0) {
				log.info("Skipping category initialization");
				return;
			}

			log.info("Reading categories from JSON file");
			var typeReference = new TypeReference<List<Map<String, Object>>>() {
			};
			var categoryList =
					JSONUtils.mapper.readValue(categoriesFile.getInputStream(), typeReference);

			for (var category : categoryList) {
				var categoryGroupId = Long.parseLong(category.get("group_id").toString());
				var categoryGroup = categoryGroupRepository.findById(categoryGroupId).get();
				category.put("group", categoryGroup);
				category.put("version", 1);
				category.remove("group_id");
			}

			var categories = categoryList.stream()
					.map(map -> JSONUtils.mapper.convertValue(map, Category.class)).toList();


			log.info("Found {} categories", categories.size());
			log.info("Saving categories into the database");
			categoryRepository.saveAll(categories);

			log.info("Saved {} categories", categories.size());

		} catch (Exception e) {
			log.error("Categories could not be initialized");
			throw e;
		}
	}

	private void initializeCategoryLocalizations() throws Exception {
		try {
			if (categoryLocalizationRepository.count() > 0) {
				log.info("Skipping category localization initialization");
				return;
			}

			log.info("Reading category localizations from JSON file");
			for (var categoryLocalizationFile : categoryLocalizationsFiles) {
				var language = categoryLocalizationFile.getFilename().split("\\.")[0];
				var typeReference = new TypeReference<Map<Long, String>>() {
				};
				var localizations = JSONUtils.mapper.readValue(categoryLocalizationFile.getInputStream(), typeReference);

				log.info("Found category localizations for {}", language);

				for (var localization : localizations.entrySet()) {
					var category = categoryRepository.findById(localization.getKey()).get();
					var categoryLocalization = CategoryLocalization.builder().id(new LocalizationId(category.getId(), language)).category(category).value(localization.getValue()).build();
					categoryLocalizationRepository.save(categoryLocalization);
				}

				log.info("Saved {} category localizations for {}", localizations.size(), language);
			}
		} catch (Exception e) {
			log.error("Category localizations could not be initialized");
			throw e;
		}
	}

	private void initializeCategoryGroupLocalizations() throws Exception {
		try {
			if (categoryGroupLocalizationRepository.count() > 0) {
				log.info("Skipping category group localization initialization");
				return;
			}

			log.info("Reading category group localizations from JSON file");
			for (var categoryGroupLocalizationFile : categoryGroupsLocalizationsFiles) {
				var language = categoryGroupLocalizationFile.getFilename().split("\\.")[0];
				var typeReference = new TypeReference<Map<Long, String>>() {
				};
				var localizations = JSONUtils.mapper.readValue(categoryGroupLocalizationFile.getInputStream(), typeReference);

				log.info("Found category group localizations for {}", language);

				for (var localization : localizations.entrySet()) {
					var categoryGroup = categoryGroupRepository.findById(localization.getKey()).get();
					var categoryGroupLocalization = CategoryGroupLocalization.builder().id(new LocalizationId(categoryGroup.getId(), language)).categoryGroup(categoryGroup).value(localization.getValue()).build();
					categoryGroupLocalizationRepository.save(categoryGroupLocalization);
				}

				log.info("Saved {} category group localizations for {}", localizations.size(), language);
			}
		} catch (Exception e) {
			log.error("Category group localizations could not be initialized");
			throw e;
		}
	}
}
