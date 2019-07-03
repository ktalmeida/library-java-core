package org.coldis.library.helper;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Reflection helper.
 */
public class ReflectionHelper {

	/**
	 * Logger.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(ReflectionHelper.class);

	/**
	 * If a method is a getter.
	 *
	 * @param  methodName Method name.
	 * @return            If a method is a getter.
	 */
	public static Boolean isGetter(final String methodName) {
		return (methodName.startsWith("get") || methodName.startsWith("is"));
	}

	/**
	 * If a method is a getter.
	 *
	 * @param  methodName Method name.
	 * @param  parameters Parameters (or parameters classes) list.
	 * @return            If a method is a getter.
	 */
	public static Boolean isGetter(final String methodName, final Object[] parameters) {
		return ((parameters == null) || (parameters.length == 0)) && ReflectionHelper.isGetter(methodName);
	}

	/**
	 * Gets the original attribute name from a getter.
	 *
	 * @param  getterName Getter name.
	 * @return            The original attribute name.
	 */
	public static String getAttributeName(final String getterName) {
		// If it is a boolean getter.
		final Boolean booleanGetter = getterName.startsWith("is");
		// Returns attribute name.
		return getterName.substring(booleanGetter ? 2 : 3, booleanGetter ? 3 : 4).toLowerCase()
				+ getterName.substring(booleanGetter ? 3 : 4);
	}

	/**
	 * Gets the getter name from an attribute name.
	 *
	 * @param  attributeName Attribute name.
	 * @return               The getter name from an attribute name.
	 */
	public static String getGetterName(final String attributeName) {
		return "get" + attributeName.substring(0, 1).toUpperCase() + attributeName.substring(1);
	}

	/**
	 * Gets the setter name from an attribute name.
	 *
	 * @param  attributeName Attribute name.
	 * @return               The setter name from an attribute name.
	 */
	public static String getSetterName(final String attributeName) {
		return "set" + attributeName.substring(0, 1).toUpperCase() + attributeName.substring(1);
	}

	/**
	 * Replace an object attribute.
	 *
	 * @param object        Object.
	 * @param attributeName Attribute name.
	 * @param newValue      New attribute value.
	 */
	public static void setAttribute(final Object object, final String attributeName, final Object newValue) {
		// If the object and attribute name are given.
		if ((object != null) && (attributeName != null)) {
			// Splits the attributes.
			final String[] attributePath = attributeName.split("\\.");
			// Current attribute path value.
			Object attributePathValue = object;
			// For each attribute in the path.
			for (Integer attributePathPartIndex = 0; attributePathPartIndex < attributePath.length; attributePathPartIndex++) {
				// Gets the attribute path part.
				final String attributePathPart = attributePath[attributePathPartIndex];
				// If the attribute value path is still valid.
				if (attributePathValue != null) {
					// If it is the last attribute.
					if ((attributePathPartIndex + 1) == attributePath.length) {
						// Tries to set the attribute value.
						try {
							FieldUtils.writeField(
									FieldUtils.getField(attributePathValue.getClass(), attributePathPart, true),
									attributePathValue, newValue, true);
						}
						// If the method cannot be found.
						catch (final Exception exception) {
							// Logs it.
							ReflectionHelper.LOGGER.error("Attribute value cannot be updated.", exception);
						}
					}
					// If it is not the last attribute.
					else {
						// Tries to get the next path value.
						try {
							attributePathValue = FieldUtils.readField(
									FieldUtils.getField(attributePathValue.getClass(), attributePathPart, true),
									attributePathValue, true);
						}
						// If the attribute path cannot be retrieved.
						catch (final Exception exception) {
							// Logs it.
							ReflectionHelper.LOGGER.error("Attribute path part value cannot be retrieved.", exception);
						}
					}
				}
			}
		}
	}

}
