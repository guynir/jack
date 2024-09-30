package jack.i18n.messages;

/**
 * This exception instance is thrown by {@link Message#compile} whenever a message compilation fails.
 *
 * @author Guy Raz Nir
 * @since 2024/09/30
 */
public class MessageCompilationException extends MessageException {

    /**
     * {@inheritDoc}
     */
    public MessageCompilationException(String message) {
        super(message);
    }

    /**
     * {@inheritDoc}
     */
    public MessageCompilationException(String message, Throwable cause) {
        super(message, cause);
    }

}
