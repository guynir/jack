package jack.i18n.messages.formatters;

import jack.utils.Asserts;

import java.time.ZoneId;
import java.util.Locale;
import java.util.Set;

/**
 * <p>A {@code Formatter} formats a given value based on preconfigured properties.
 * For example, a numeric formatter would convert a decimal value into string representation, using given locale to
 * decide which thousands and decimal separators are to use.<br>
 * A currency formatter will format the value and prepend or append currency mark.
 * </p>
 * All formatters are thread-safe and can be used in concurrent environment.
 *
 * @author Guy Raz Nir
 * @since 2024/10/01
 */
public abstract class Formatter {

    /**
     * List of supported types.
     */
    protected final Set<Class<?>> supportedTypes;

    /**
     * Class constructor.
     *
     * @param supportedTypes List of supported types.
     */
    protected Formatter(Class<?>... supportedTypes) {
        this.supportedTypes = Set.of(supportedTypes);
    }

    /**
     * Check if a given type is supported by this formatter.
     *
     * @param clazz Class to test.
     * @return {@code true} if the type is supported, {@code false} if not.
     */
    public boolean supports(Class<?> clazz) {
        return supportedTypes.contains(clazz);
    }

    /**
     * @return A set of supported types by this formatter.
     */
    public Set<Class<?>> supportedTypes() {
        return supportedTypes;
    }

    /**
     * Format <i>value</i> to match given <i>locale</i> and preconfigured properties.
     *
     * @param locale Locale to use for formatting/rendering.
     * @param zoneId Identifier of zone for formatting or adjusting values such as date and time.
     * @param value  Value to format.
     * @return String representing <i>value</i>.
     * @throws IllegalArgumentException If either <i>locale</i> is {@code null} or if <i>value</i> is {@code null}
     *                                  and no {@link #defaultValue() default value} provided.
     * @throws FormatErrorException     If <i>value</i> is not of supported type or value could not be formatted due to
     *                                  other restrictions.
     */
    public final String format(Locale locale, ZoneId zoneId, Object value) throws IllegalArgumentException, FormatErrorException {
        Asserts.notNull(locale, "Locale cannot be null.");
        Asserts.notNull(zoneId, "ZoneId cannot be null.");
        if (value == null) {
            return defaultValue();
        }

        if (!supportedTypes.contains(value.getClass())) {
            throw new FormatErrorException(value.getClass().getSimpleName() + " is not supported by this formatter.");
        }

        return formatValue(locale, zoneId, value);
    }

    /**
     * Provide default value when {@link #format(Locale, ZoneId, Object)} is passed {@code null} as the <i>value</i>.
     * By default, (if not overridden by descended class), this method will raise {@code IllegalArgumentException}.
     *
     * @return Default value to use.
     * @throws IllegalArgumentException If no default provided.
     */
    protected String defaultValue() throws IllegalArgumentException {
        throw new IllegalArgumentException("Value cannot be null.");
    }

    /**
     * The actual implementation that formats/renders the value into string format. All inputs are gurantieid to be
     * non-{@code null} and <i>value</i> type is one of the supported types of this formatter.
     *
     * @param locale Locale to use for formatting/rendering.
     * @param zoneId Identifier of zone for formatting or adjusting values such as date and time.
     * @param value  Value to convert.
     * @return String representation of <i>value</i>.
     * @throws FormatErrorException If value could not be converted.
     */
    protected abstract String formatValue(Locale locale, ZoneId zoneId, Object value) throws FormatErrorException;
}
