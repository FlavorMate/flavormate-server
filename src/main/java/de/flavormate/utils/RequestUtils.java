package de.flavormate.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

/**
 * Utility class for handling common operations related to request parameters.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RequestUtils {
	private static final List<String> NOT_CASE_SENSITIVE = List.of("createdOn", "lastModifiedOn");

	/**
	 * Converts request parameters to a Spring Data {@link Pageable} object.
	 *
	 * @param page          The page number (0-indexed).
	 * @param size          The size of the page. If -1, the result will be unpaged.
	 * @param sortBy        The property to sort by.
	 * @param sortDirection The direction of sorting, either "ASC" or "DESC".
	 * @return A {@link Pageable} object representing the pagination and sorting information.
	 */
	public static Pageable convertPageable(Integer page, Integer size, String sortBy,
	                                       String sortDirection) {

		var order = Sort.Order.by(sortBy).with(Sort.Direction.fromString(sortDirection));

		if (!NOT_CASE_SENSITIVE.contains(sortBy)) {
			order = order.ignoreCase();
		}

		var sort = Sort.by(order);

		return size == -1 ? Pageable.unpaged() : PageRequest.of(page, size, sort);
	}
}
