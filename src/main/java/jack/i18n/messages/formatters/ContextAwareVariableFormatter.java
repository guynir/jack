package jack.i18n.messages.formatters;

import jack.utils.Asserts;

import java.time.ZoneId;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>A context-aware variable formatter is an adaptor that extracts a variable from a given context and format it
 * based on pre-associated {@link Formatter}.
 * </p>
 * This adapter also provides a validation facility to check whether the requested variable exists in the given context
 * and if it is format-able.
 *
 * @author Guy Raz Nir
 * @since 2024/10/09
 */
public class ContextAwareVariableFormatter {

    /**
     * Formatter to use.
     */
    private final Formatter formatter;

    /**
     * Name of variable to extract from given context.
     */
    private final String variableName;

    /**
     * Class constructor.
     */
    public ContextAwareVariableFormatter(Formatter formatter, String variableName) throws IllegalArgumentException {
        Asserts.notNull(formatter, "Formatter cannot be null.");
        Asserts.notEmpty(variableName, "Variable name cannot be null or empty.");
        this.formatter = formatter;
        this.variableName = variableName;
    }

    /**
     * Validates that the associated variable exists in the given <i>context</i> and its type is supported by the
     * associated formatter.
     *
     * @param context Context to extract variable from.
     * @throws IllegalArgumentException     If <i>context</i> is {@code null}.
     * @throws VariableFormatErrorException If either variable does not exist in the <i>context</i> or its type is
     *                                      not supported by the formatter.
     */
    public void validateVariable(Map<String, Object> context) throws IllegalArgumentException, VariableFormatErrorException {
        Asserts.notNull(context, "Context cannot be null.");
        Object value = context.get(variableName);
        if (value == null) {
            throw new VariableFormatErrorException("Variable '" + variableName + "' does not exist in context.",
                    VariableFormatErrorException.VariableFormatErrorType.VARIABLE_UNDEFINED);
        }

        if (!formatter.supports(value.getClass())) {
            String types = formatter.supportedTypes().stream().map(Class::getSimpleName).collect(Collectors.joining(", "));
            String message = "Variable '%s' type (%s) is not supported by the formatter (%s). Supported types are: %s."
                    .formatted(variableName,
                            value.getClass().getSimpleName(),
                            formatter.getClass().getSimpleName(),
                            types);
            throw new VariableFormatErrorException(message,
                    VariableFormatErrorException.VariableFormatErrorType.VARIABLE_TYPE_ERROR);
        }
    }

    /**
     * Extract variable from given <i>context</i> and attempt to format it.
     *
     * @param locale  Locale to use for formatting.
     * @param zoneId  Zone to use for formatting.
     * @param context Context to extract variable from.
     * @return Formatted string of the variable.
     * @throws IllegalArgumentException If either arguments are {@code null}.
     * @throws FormatErrorException     If variable could not be formatted.
     */
    public String format(Locale locale, ZoneId zoneId, Map<String, Object> context) throws IllegalArgumentException, FormatErrorException {
        // Validates variable existence and type.
        // Will handle null-assertion for 'context'.
        validateVariable(context);

        // Format variable.
        // Will handle null-assertion for locale and zoneId.
        // Should not generate an exception, as validation was successful.
        return formatter.format(locale, zoneId, context.get(variableName));
    }
}
