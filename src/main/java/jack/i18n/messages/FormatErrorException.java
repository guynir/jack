package jack.i18n.messages;

/**
 * This exception indicates a failure to format a given value based on given criteria.
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
}
