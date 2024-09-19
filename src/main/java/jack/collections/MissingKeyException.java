package jack.collections;

/**
 * This exception indicates an attempt to fetch an object from a {@link ReadOnlyMap} that does not exist.
 *
 * @author Guy Raz Nir
 * @since 2021/12/09
 */
public class MissingKeyException extends RuntimeException {

    /**
     * Class constructor.
     *
     * @param message Error message.
     */
    public MissingKeyException(String message) {
        super(message);
    }
}
