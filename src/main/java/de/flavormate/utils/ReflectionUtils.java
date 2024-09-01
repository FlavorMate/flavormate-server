package de.flavormate.utils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class ReflectionUtils {


	/**
	 * Retrieves the type parameter class for the generic type parameter T from a given class.
	 *
	 * <p>
	 * This method assumes that the type parameter T is declared for the given class's generic
	 * superclass. It uses reflection to obtain the actual type parameter class for T at runtime.
	 *
	 * @param clazz The class from which to retrieve the type parameter class for T.
	 * @param <T>   The generic type parameter for which to retrieve the class.
	 * @return The {@code Class} object representing the type parameter class for T.
	 * @throws IllegalArgumentException If the type parameter information is not available or if the
	 *                                  provided class does not have a generic superclass with a type
	 *                                  parameter. @SuppressWarnings("unchecked") This method uses unchecked casting, as the
	 *                                  type information is obtained through reflection.
	 */
	@SuppressWarnings("unchecked")
	public static <T> Class<T> getTypeParameterTFromClass(Class<?> clazz) {
		Type type = clazz.getGenericSuperclass();

		if (!(type instanceof ParameterizedType parameterizedType)) {
			throw new IllegalArgumentException(
					"Type parameter information not available for the provided class.");
		}

		Type[] typeArguments = parameterizedType.getActualTypeArguments();

		if (typeArguments.length == 0) {
			throw new IllegalArgumentException(
					"No type parameters found for the provided class's generic superclass.");
		}

		return (Class<T>) typeArguments[0];
	}
}
