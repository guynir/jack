package jack.i18n.messages.formatters;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * A collection of static utilities to support {@link Formatter} implementations.
 *
 * @author Guy Raz Nir
 * @since 2024/10/01
 */
public class FormatterHelper {

    /**
     * Helper method to validate that property is defined, and defined well.
     *
     * @param properties   Properties set.
     * @param propertyName Name of property to validate.
     * @param allowEmpty   {@code true} if property can be defined as an {@link String#isEmpty() empty} string (a string
     *                     with no contents).
     * @param allowBlank   {@code true} if property can be defined as {@link String#isBlank() blank} string.
     *                     A blank string is one that contains only white-space characters (i.e.: space, tabs,
     *                     newline, etc...).
     *                     This condition is evaluated only if <i>allowEmpty</i> does not fail.
     * @return {@code true} if property is valid, {@code false} if not.
     */
    public static boolean validatePropertyDefinition(Map<String, String> properties,
                                                     String propertyName,
                                                     boolean allowEmpty,
                                                     boolean allowBlank,
                                                     List<String> errors) {
        String value = properties.get(propertyName);
        String errorMessage = null;
        if (value == null) {
            errorMessage = "Missing or property: " + propertyName;
        } else if (value.isEmpty() && !allowEmpty) {
            errorMessage = "Property " + propertyName + " has no value.";
        } else if (value.isBlank() && !allowBlank) {
            errorMessage = "Property " + propertyName + " has no actual value.";
        }

        if (errorMessage != null) {
            errors.add(errorMessage);
        }

        return errorMessage == null;
    }

    /**
     * <p>Validate that a given raw properties set contains only allowed properties.
     * Properties which are unknown will cause an exception generation.
     * </p>
     * The comparison made is based on a trimmed lower-case version of property names (e.g.: property
     * <i>"  DecimalValues   "</i> is converted to <i></i>"decimalvalues"</i>).
     *
     * @param props         Raw properties set.
     * @param allowedValues Allowed properties.
     * @throws FormatErrorException If <i>props</i> contains properties which are not in <i>allowedValues</i>.
     */
    public static void restrictProperties(Map<String, String> props, String... allowedValues)
            throws FormatErrorException {
        restrictProperties(props, Set.of(allowedValues));
    }

    /**
     * <p>Validate that a given raw properties set contains only allowed properties.
     * Properties which are unknown will cause an exception generation.
     * </p>
     * The comparison made is based on a trimmed lower-case version of property names (e.g.: property
     * <i>"  DecimalValues   "</i> is converted to <i></i>"decimalvalues"</i>).
     *
     * @param props         Raw properties set.
     * @param allowedValues Set of allowed properties.
     * @throws FormatErrorException If <i>props</i> contains properties which are not in <i>allowedValues</i>.
     */
    public static void restrictProperties(Map<String, String> props, Collection<String> allowedValues)
            throws FormatErrorException {

        // Convert all allowed values to lowercase. Remove any essential leading/trailing spaces.
        Set<String> normalizedAllowedValues = allowedValues
                .stream()
                .filter(Objects::nonNull)
                .map(v -> v.toLowerCase().trim())
                .collect(Collectors.toSet());

        Set<String> unknownValues = props
                .keySet()
                .stream()
                .filter(prop -> !allowedValues.contains(prop.toLowerCase().trim()))
                .collect(Collectors.toSet());

        if (!unknownValues.isEmpty()) {
            throw new FormatErrorException("Unknown properties: " + String.join(", ", unknownValues));
        }
    }

    /**
     * <p>Attempt to extract a property and convert it into a target type.
     * </p>
     * <p>If the property does not exist, {@link Optional#empty()} is returned.
     * </p>
     * If a property does exist but contains either empty value (e.g.: empty or blank string) or has an invalid value
     * (one which cannot be converted to a boolean form), an exception is thrown.
     *
     * @param props        Properties source.
     * @param propertyName Name of property.
     * @param parser       A function that actually translates the value.
     *                     The function can expect to receive a non-empty, lower-case representation of the property's
     *                     value.
     * @return Result type or {@code null} if property is not defined.
     * @throws FormatErrorException If property contains invalid value.
     */
    public static <T> T parseValue(Map<String, String> props,
                                   String propertyName,
                                   Function<String, T> parser) throws FormatErrorException {
        return parseValue(props, propertyName, parser, null);
    }

    /**
     * <p>Attempt to extract a property and convert it into a target type.
     * </p>
     * <p>If the property does not exist, {@link Optional#empty()} is returned.
     * </p>
     * If a property does exist but contains either empty value (e.g.: empty or blank string) or has an invalid value
     * (one which cannot be converted to a boolean form), an exception is thrown.
     *
     * @param props         Properties source.
     * @param propertyName  Name of property.
     * @param parser        A function that actually translates the value.
     *                      The function can expect to receive a non-empty, lower-case representation of the property's
     *                      value.
     * @param fallbackValue Fallback value to use in case extracted property is {@code null} or does not exist.
     * @return Result type or {@code null} if property is not defined.
     * @throws FormatErrorException If property contains invalid value.
     */
    public static <T> T parseValue(Map<String, String> props,
                                   String propertyName,
                                   Function<String, T> parser,
                                   T fallbackValue) throws FormatErrorException {
        String value = props.get(propertyName);
        T result = fallbackValue;
        if (value != null) {
            value = value.trim().toLowerCase();

            // If property is defined, but has no value (empty string) -- raise exception.
            if (value.isEmpty()) {
                throw new FormatErrorException("Property '%s' is defined but is empty.".formatted(propertyName));
            }

            try {
                result = parser.apply(value);
            } catch (RuntimeException ex) {
                throw new FormatErrorException("Invalid property %s value: %s".formatted(propertyName, value), ex);
            }
        }

        return result;
    }

    /**
     * Parse a property value into numeric form.
     * Typically called by {@link #parseValue(Map, String, Function)}.
     *
     * @param value Value to convert. The value must be without spaces and non-empty.
     * @return Integer value.
     * @throws NumberFormatException If value could not be converted to integer.
     */
    public static Integer asInteger(String value) throws NumberFormatException {
        return Integer.parseInt(value);
    }

    /**
     * Convert a string to {@code boolean} value.
     * Typically called by {@link #parseValue(Map, String, Function)}.
     *
     * @param value Value to convert. Only two values are supported (case ignored): <i>true</i> and <i>false</i>.
     * @return Boolean value.
     * @throws RuntimeException If <i>value</i> contains non-boolean value.
     */
    public static Boolean asBoolean(String value) throws RuntimeException {
        value = value.toLowerCase().trim();
        if (Boolean.TRUE.toString().equals(value)) {
            return true;
        } else if (Boolean.FALSE.toString().equals(value)) {
            return false;
        } else {
            throw new RuntimeException("Non-boolean value: " + value);
        }
    }
}
