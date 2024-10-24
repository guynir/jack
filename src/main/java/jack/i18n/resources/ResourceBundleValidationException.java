package jack.i18n.resources;

import java.util.List;

/**
 * This exception is thrown during the construction of a resource bundle pack if there are any errors found
 * in the data (such as missing resource or using a reserved key).
 *
 * @author Guy Raz Nir
 * @since 2024/07/29
 */
public class ResourceBundleValidationException extends ResourceBundleException {

    /**
     * Holds list of errors.
     */
    private final List<String> errors;

    /**
     * Class constructor.
     *
     * @param message Error message.
     * @param errors  List of errors found.
     */
    public ResourceBundleValidationException(String message, List<String> errors) {
        super(message);
        this.errors = errors;
    }

    /**
     * @return List of error messages.
     */
    public List<String> getErrors() {
        return errors;
    }
}
