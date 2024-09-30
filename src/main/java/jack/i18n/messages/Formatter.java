package jack.i18n.messages;

import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * <p>A variable formatter is used for formatting a given type of value into a string representation.
 * </p>
 * <p>For example, a currency formatter is one that takes a decimal value (e.g.: 12.3456564) and formats it
 * to a US dollar representation with 2 decimal places, e.g.: $12.34).
 * </p>
 * A formatter supports introspection, one that informs the caller which types are supported by this formatter.
 *
 * @author Guy Raz Nir
 * @since 2024/09/30
 */
public abstract class Formatter {

    /**
     * List of supported types.
     */
    private final Set<Class<?>> supportedTypes;

    /**
     * Class constructor.
     *
     * @param supportedTypes List of supported types this formatter can handle.
     */
    protected Formatter(Set<Class<?>> supportedTypes) {
        this.supportedTypes = supportedTypes;
    }

    /**
     * Test if a given type is supported by this formatter.
     *
     * @param type Type to check.
     * @return {@code true} if <i>type</i> is supported by this formatter, {@code false} if not.
     */
    public boolean supports(Class<?> type) {
        return supportedTypes.contains(type);
    }

    /**
     * Formats a given <i>value</i> into a string format.
     *
     * @param locale Locale to use for formatting.
     * @param value  Value to format.
     * @return String representation of the formatted value.
     * @throws IllegalArgumentException If either arguments are {@code null} or <i>value</i> is not of
     *                                  supported {@link #supports types}.
     */
    public abstract String format(Locale locale, Map<String, String> props, Object value) throws IllegalArgumentException;
}
