package jack.i18n.messages.formatters;

import jack.utils.Asserts;

import java.time.ZoneId;
import java.util.Locale;
import java.util.Map;

/**
 * <p>A context-aware variable dynamic-formatter is an adaptor that extracts a variable from a given context and format
 * it based on one of predefined {@link Formatter}s.
 * </p>
 * This adapter also provides a validation facility to check whether the requested variable exists in the given context
 * and if it is having an available formatter based on its type.
 *
 * @author Guy Raz Nir
 * @since 2024/10/09
 */
public class ContextAwareVariableDynamicFormatter {

    /**
     * Formatter to use.
     */
    private final Map<Class<?>, Formatter> formatters;

    /**
     * Name of variable to extract from given context.
     */
    private final String variableName;

    /**
     * Class constructor.
     */
    public ContextAwareVariableDynamicFormatter(Map<Class<?>, Formatter> formatters, String variableName)
            throws IllegalArgumentException {
        Asserts.notNull(formatters, "Formatters map cannot be null.");
        Asserts.notEmpty(variableName, "Variable name cannot be null or empty.");
        this.formatters = formatters;
        this.variableName = variableName;
    }

    /**
     * Validates that the associated variable exists in the given <i>context</i> and its type has a supported formatter.
     *
     * @param context Context to extract variable from.
     * @throws IllegalArgumentException     If <i>context</i> is {@code null}.
     * @throws VariableFormatErrorException If either variable does not exist in the <i>context</i> or its type is
     *                                      not supported by any formatter.
     */
    public void validateVariable(Map<String, Object> context) throws IllegalArgumentException, VariableFormatErrorException {
        Asserts.notNull(context, "Context cannot be null.");
        Object variable = getVariable(context);
        getFormatter(variable.getClass());
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

        Object variable = getVariable(context);
        Formatter formatter = getFormatter(variable.getClass());

        // Format variable.
        // Will handle null-assertion for locale and zoneId.
        // Should not generate an exception, as validation was successful.
        return formatter.format(locale, zoneId, context.get(variableName));
    }

    /**
     * Extract a variable from context.
     *
     * @param context Context to extract variable from.
     * @return Variable's value. This value is never {@code null}.
     * @throws VariableFormatErrorException If variable does not exist in given <i>context</i>.
     */
    protected Object getVariable(Map<String, Object> context) throws VariableFormatErrorException {
        Object value = context.get(variableName);
        if (value == null) {
            throw new VariableFormatErrorException("Variable '" + variableName + "' does not exist in context.",
                    VariableFormatErrorException.VariableFormatErrorType.VARIABLE_UNDEFINED);
        }

        return value;
    }

    /**
     * Lookup a formatter for a given type. If a formatter does not exist, an exception is thrown.
     *
     * @param clazz Class to look a formatter for.
     * @return Formatter.
     * @throws VariableFormatErrorException If no formatter could be found for given class.
     */
    protected Formatter getFormatter(Class<?> clazz) throws VariableFormatErrorException {
        Formatter formatter = formatters.get(clazz);
        if (formatter == null) {
            String message = "Variable '%s' of type %s does not have an associated formatter."
                    .formatted(variableName, clazz.getSimpleName());
            throw new VariableFormatErrorException(message,
                    VariableFormatErrorException.VariableFormatErrorType.VARIABLE_TYPE_ERROR);
        }

        return formatter;
    }
}
