package jack.i18n.messages;

import java.util.List;

/**
 * This exception is thrown whenever an error detected during {@link Message#render messsage rendering} stage.
 *
 * @author Guy Raz Nir
 * @since 2024/09/30
 */
public class MessageRenderingException extends MessageException {

    /**
     * {@inheritDoc}
     */
    public MessageRenderingException(String message) {
        super(message);
    }

    /**
     * {@inheritDoc}
     */
    public MessageRenderingException(String message, List<String> errors) {
        super(message, errors);
    }

    /**
     * {@inheritDoc}
     */
    public MessageRenderingException(String message, Throwable cause) {
        super(message, cause);
    }
}
