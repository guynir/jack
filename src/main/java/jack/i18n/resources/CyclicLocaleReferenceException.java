package jack.i18n.resources;

/**
 * This exception thrown whenever a cyclic reference detected within locales. For example, if <i>en_US</i> reference
 * <i>en_CA</i> as a parent and <i>en_CA</i> reference <i>en_US</i> as a parent - this exception is thrown to indicate
 * a failure in locale hierarchy.
 *
 * @author Guy Raz Nir
 * @since 2024/10/26
 */
public class CyclicLocaleReferenceException extends ResourceBundleException {

    public CyclicLocaleReferenceException(String message) {
        super(message);
    }
}
