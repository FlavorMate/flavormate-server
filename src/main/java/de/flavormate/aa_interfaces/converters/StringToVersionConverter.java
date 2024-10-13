package de.flavormate.aa_interfaces.converters;

import de.flavormate.aa_interfaces.models.Version;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

/**
 * Converts a {@link String} to a {@link Version}.
 * <p>
 * This class is a Spring Component and implements the Converter interface,
 * providing a method to convert version strings in the format
 * "major.minor.patch+build" to Version objects.
 */
@Component
public class StringToVersionConverter implements Converter<String, Version> {

	/**
	 * Converts a {@link String} to a {@link Version}.
	 *
	 * @param source the version string in the format "major.minor.patch+build"; can be null
	 * @return a {@link Version} object if the source is valid; null if the source is null
	 */
	@Override
	public Version convert(@Nullable String source) {
		if (source == null)
			return null;

		return Version.parse(source);
	}


}
