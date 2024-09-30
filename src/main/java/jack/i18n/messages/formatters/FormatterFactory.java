package jack.i18n.messages.formatters;

import java.util.Map;

/**
 * <p>A formatter factory creates a new formatter, preconfigured will all custom properties for format a given value.
 * </p>
 *
 * @author Guy Raz Nir
 * @since 2024/10/01
 */
public abstract class FormatterFactory<T extends Formatter> {

    /**
     * Class constructor.
     */
    protected FormatterFactory() {
    }

    /**
     * <p>Construct a new {@code Formatter} without and preliminary configuration. If the formatter does require
     * configuration than {@link IllegalArgumentException} is thrown.
     * </p>
     * This function is a shorthand for {@code createFactory(null)}.
     *
     * @return New {@code Formatter}.
     * @throws IllegalArgumentException If formatter requires properties.
     */
    public T createFormatter() throws IllegalArgumentException {
        return createFormatter(null);
    }

    /**
     * <p>Construct a new {@code Formatter} configured with given <i>properties</i>.
     * </p>
     * One of the core functionalities of this call is to parse and validate <i>properties</i>. If any of the following
     * constraining are violated, {@code FormatErrorException} is thrown:
     * <ul>
     *     <li>Mandatory properties are missing.</li>
     *     <li>A Property is eiter empty (defined but does not contain any value) or contains an invalid value.</li>
     *     <li><i>properties</i> set contains unknown properties.</li>
     * </ul>
     *
     * @param properties Optional properties.
     * @return New {@code Formatter}.
     * @throws IllegalArgumentException If formatter requires properties and <i>properties</i> is {@code null}.
     * @throws FormatErrorException     If <i>properties</i> constrains are violated.
     */
    public abstract T createFormatter(Map<String, String> properties) throws IllegalArgumentException, FormatErrorException;
}
