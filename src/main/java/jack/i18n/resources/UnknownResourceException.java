package jack.i18n.resources;

/**
 * This exception is thrown whenever a request resource could not be resolved.
 *
 * @author Guy Raz Nir
 * @since 2024/08/03
 */
public class UnknownResourceException extends ResourceBundleException {

    public UnknownResourceException(String message) {
        super(message);
    }

    public UnknownResourceException(String message, Throwable cause) {
        super(message, cause);
    }
}
