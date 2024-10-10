package jack.i18n.messages.formatters;

import jack.i18n.messages.MessageException;

/**
 * <p>This exception indicates a failure related to {@link Formatter#format(java.util.Locale, java.time.ZoneId, Object)}
 * operation.
 * </p>
 * The exception is typically thrown whenever the request has invalid properties set or the type of value is not
 * {@link Formatter#supports(Class) supported} by the formatter.
 *
 * @author Guy Raz Nir
 * @since 2024/09/30
 */
public class FormatErrorException extends MessageException {

    /**
     * {@inheritDoc}
     */
    public FormatErrorException(String message) {
        super(message);
    }

    public FormatErrorException(String message, Throwable cause) {
        super(message, cause);
    }
}
