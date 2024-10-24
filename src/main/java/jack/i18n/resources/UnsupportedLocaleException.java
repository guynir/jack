package jack.i18n.resources;

/**
 * This exception indicates that a requested locale is not supported by this implementation.
 *
 * @author Guy Raz Nir
 * @since 2024/07/28
 */
public class UnsupportedLocaleException extends ResourceBundleException {

    public UnsupportedLocaleException(String message) {
        super(message);
    }

    public UnsupportedLocaleException(String message, Throwable cause) {
        super(message, cause);
    }
}
