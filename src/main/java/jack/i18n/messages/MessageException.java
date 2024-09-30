package jack.i18n.messages;

import java.util.Collections;
import java.util.List;

/**
 * Base exception for all messages-related exceptions.
 *
 * @author Guy Raz Nir
 * @since 2024/09/30
 */
public class MessageException extends RuntimeException {

    /**
     * Optional list of errors. If no specific error available, this value is {@code null}.
     */
    private List<String> errors = null;

    /**
     * Class constructor.
     *
     * @param message Error message.
     */
    public MessageException(String message) {
        super(message);
    }

    /**
     * Class constructor.
     *
     * @param message Error message.
     * @param errors  List of errors. May be {@code null}.
     */
    public MessageException(String message, List<String> errors) {
        super(message);
        this.errors = Collections.unmodifiableList(errors);
    }

    /**
     * {@inheritDoc}
     */
    public MessageException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * @return List of errors (an immutable list). May be {@code null} if no specific errors are available.
     */
    public List<String> getErrors() {
        return errors;
    }
}
