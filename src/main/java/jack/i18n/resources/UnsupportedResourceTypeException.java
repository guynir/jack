package jack.i18n.resources;

/**
 * This exception is thrown if there is an attempt to register a resource of type that is not supported.<p>
 * </p>
 *
 * @author Guy Raz Nir
 * @since 2024/07/28
 */
public class UnsupportedResourceTypeException extends ResourceBundleException {

    public UnsupportedResourceTypeException(String message) {
        super(message);
    }
}
