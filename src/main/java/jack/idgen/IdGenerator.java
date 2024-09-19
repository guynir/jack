package jack.idgen;

/**
 * An {@code IdGenerator} produces a unique identifier for each call of {@link #generate()}. The actual type (e.g.:
 * {@code java.lang.String}, {@code java.lang.Long}, ....) and uniqueness assurance are details of the underlying
 * implementation.
 *
 * @author Guy Raz Nir
 * @since 2024/09/11
 */
public interface IdGenerator<T> {

    /**
     * @return Unique identifier for each call.
     */
    T generate();

}