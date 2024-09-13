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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class S01_InitDatabaseScript extends AScript {

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

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private CategoryGroupRepository categoryGroupRepository;

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private CategoryLocalizationRepository categoryLocalizationRepository;

	@Autowired
	private CategoryGroupLocalizationRepository categoryGroupLocalizationRepository;

	public S01_InitDatabaseScript() {
		super("Initialize Database");
	}

	public void run() {
		log("Starting database initialization");

		initializeRoles();

		initializeCategoryGroups();
		initializeCategoryGroupLocalizations();
		initializeCategories();
		initializeCategoryLocalizations();

		log("Finished database initialization");
	}


	private Boolean initializeRoles() {
		try {
			if (roleRepository.count() > 0) {
				log("Skipping roles initialization");
				return true;
			}

			log("Reading roles from JSON file");
			Role[] rolesArr = JSONUtils.mapper.readValue(rolesFile.getInputStream(), Role[].class);
			List<Role> roles = Arrays.asList(rolesArr);

			log("Found {} roles", roles.size());

			log("Saving roles into the database");
			roleRepository.saveAll(roles);

			log("Saved {} roles", roles.size());
			return true;
		} catch (Exception e) {
			warning("Roles could not be initialized");
			return false;
		}
	}

	private Boolean initializeCategoryGroups() {
		try {
			if (categoryGroupRepository.count() > 0) {
				log("Skipping category group initialization");
				return true;
			}

			log("Reading category groups from JSON file");
			CategoryGroup[] categoryGroupsArr = JSONUtils.mapper
					.readValue(categoryGroupsFile.getInputStream(), CategoryGroup[].class);
			List<CategoryGroup> categoryGroups = Arrays.asList(categoryGroupsArr);

			log("Found {} category groups", categoryGroups.size());

			log("Saving category groups into the database");
			categoryGroupRepository.saveAll(categoryGroups);

			log("Saved {} category groups", categoryGroups.size());
			return true;
		} catch (Exception e) {
			warning("Category groups could not be initialized");
			return false;
		}
	}

	private Boolean initializeCategories() {
		try {
			if (categoryRepository.count() > 0) {
				log("Skipping category initialization");
				return true;
			}

			log("Reading categories from JSON file");
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


			log("Found {} categories", categories.size());
			log("Saving categories into the database");
			categoryRepository.saveAll(categories);

			log("Saved {} categories", categories.size());
			return true;
		} catch (Exception e) {
			warning("Categories could not be initialized");
			return false;
		}
	}

	private Boolean initializeCategoryLocalizations() {
		try {
			if (categoryLocalizationRepository.count() > 0) {
				log("Skipping category localization initialization");
				return true;
			}

			log("Reading category localizations from JSON file");
			for (var categoryLocalizationFile : categoryLocalizationsFiles) {
				var language = categoryLocalizationFile.getFilename().split("\\.")[0];
				var typeReference = new TypeReference<Map<Long, String>>() {
				};
				var localizations = JSONUtils.mapper.readValue(categoryLocalizationFile.getInputStream(), typeReference);

				log("Found category localizations for {}", language);

				for (var localization : localizations.entrySet()) {
					var category = categoryRepository.findById(localization.getKey()).get();
					var categoryLocalization = CategoryLocalization.builder().id(new LocalizationId(category.getId(), language)).category(category).value(localization.getValue()).build();
					categoryLocalizationRepository.save(categoryLocalization);
				}

				log("Saved {} category localizations for {}", localizations.size(), language);
			}
			return true;
		} catch (Exception e) {
			warning("Category localizations could not be initialized");
			return false;
		}
	}

	private Boolean initializeCategoryGroupLocalizations() {
		try {
			if (categoryGroupLocalizationRepository.count() > 0) {
				log("Skipping category group localization initialization");
				return true;
			}

			log("Reading category group localizations from JSON file");
			for (var categoryGroupLocalizationFile : categoryGroupsLocalizationsFiles) {
				var language = categoryGroupLocalizationFile.getFilename().split("\\.")[0];
				var typeReference = new TypeReference<Map<Long, String>>() {
				};
				var localizations = JSONUtils.mapper.readValue(categoryGroupLocalizationFile.getInputStream(), typeReference);

				log("Found category group localizations for {}", language);

				for (var localization : localizations.entrySet()) {
					var categoryGroup = categoryGroupRepository.findById(localization.getKey()).get();
					var categoryGroupLocalization = CategoryGroupLocalization.builder().id(new LocalizationId(categoryGroup.getId(), language)).categoryGroup(categoryGroup).value(localization.getValue()).build();
					categoryGroupLocalizationRepository.save(categoryGroupLocalization);
				}

				log("Saved {} category group localizations for {}", localizations.size(), language);
			}
			return true;
		} catch (Exception e) {
			warning("Category group localizations could not be initialized");
			return false;
		}
	}
}
