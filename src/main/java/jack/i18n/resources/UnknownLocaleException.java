package jack.i18n.resources;

/**
 * This exception is thrown whenever a caller request a resource bundle for a locale which is unknown or does
 * not have a fallback.
 *
 * @author Guy Raz Nir
 * @since 2024/07/31
 */
public class UnknownLocaleException extends ResourceBundleException {

    public UnknownLocaleException(String message) {
        super(message);
    }

}
