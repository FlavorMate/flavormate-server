package de.flavormate.ba_entities.file.wrapper;

import de.flavormate.ba_entities.file.enums.FileCategory;
import de.flavormate.ba_entities.file.enums.FileType;

public record FileDraft(FileType type, FileCategory category, String content, Long owner) {

}
