package jack.i18n.resources;

/**
 * This exception indicates an attempt to register a resource more than once.
 *
 * @author Guy Raz Nir
 * @since 2024/07/28
 */
public class ResourceAlreadyDefinedException extends ResourceBundleException {

    public ResourceAlreadyDefinedException(String message) {
        super(message);
    }
}
