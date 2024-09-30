package jack.i18n.messages.formatters;

import java.time.ZoneId;
import java.util.Locale;
import java.util.Map;

/**
 * <p>An exception thrown whenever a {@link ContextAwareVariableFormatter} formatter validates a candidate variable
 * for formatting.
 * </p>
 * <p>The validation can either be invoked manually (via {@link ContextAwareVariableFormatter#validateVariable(Map)} or
 * when {@link ContextAwareVariableFormatter#format(Locale, ZoneId, Map)} is called.
 * </p>
 * The intention of this exception is to provide an addressable reason for the failure -- either the variable is not
 * defined in the given context or the type is not supported by the formatter.
 * In either case, the caller has the ability to address the problem.
 *
 * @author Guy Raz Nir
 * @since 2024/10/09
 */
public class VariableFormatErrorException extends FormatErrorException {

    /**
     * Type of error. Allows a caller to determine the source of the problem and probably take action.
     */
    private final VariableFormatErrorType errorType;

    /**
     * Class constructor.
     *
     * @param message   Error message.
     * @param errorType Type of error.
     */
    public VariableFormatErrorException(String message, VariableFormatErrorType errorType) {
        super(message);
        this.errorType = errorType;
    }

    /**
     * @return Type of error.
     */
    public VariableFormatErrorType getErrorType() {
        return errorType;
    }

    /**
     * Type of errors when formatting a variable from {@link ContextAwareVariableFormatter}.
     */
    public enum VariableFormatErrorType {
        VARIABLE_UNDEFINED,
        VARIABLE_TYPE_ERROR
    }
}
