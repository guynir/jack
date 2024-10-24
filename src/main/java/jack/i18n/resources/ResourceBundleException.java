package jack.i18n.resources;

/**
 * This exception serves as the parent exception for all message resources related operations.
 *
 * @author Guy Raz Nir
 * @since 2024/07/29
 */
public class ResourceBundleException extends RuntimeException {

    public ResourceBundleException() {
    }

    public ResourceBundleException(String message) {
        super(message);
    }

    public ResourceBundleException(String message, Throwable cause) {
        super(message, cause);
    }

    public ResourceBundleException(Throwable cause) {
        super(cause);
    }

    public ResourceBundleException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
