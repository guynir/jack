package jack.collections;

/**
 * This exception indicates that a caller requested an object from {@link ReadOnlyMap} that could not be converted
 * to a desired type.
 *
 * @author Guy Raz Nir
 * @since 2021/12/09
 */
public class TypeMismatchException extends RuntimeException {

    /**
     * Class constructor.
     *
     * @param message Custom exception message to use.
     */
    public TypeMismatchException(String message) {
        super(message);
    }

    /**
     * Class constructor.
     *
     * @param message Custom exception message to use.
     * @param cause   Root cause (original exception).
     */
    public TypeMismatchException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Construct a new exception instance with a predefined message describing the desired (target) type and the actual
     * type found in the internal model.
     *
     * @param requestedType Desired type (the type the caller requested).
     * @param actualType    Actual type of object.
     */
    public TypeMismatchException(Class<?> requestedType, Class<?> actualType) {
        super("Type mismatch: Could not convert a value of type "
                + actualType.getSimpleName()
                + " to requested object of type "
                + actualType.getSimpleName());
    }

    /**
     * Construct a new exception instance with a predefined message describing the desired (target) type and the actual
     * type found in the internal model.
     *
     * @param requestedType Desired type (the type the caller requested).
     * @param actualType    Actual type of object.
     * @param cause         Root cause (original exception).
     */
    public TypeMismatchException(Class<?> requestedType, Class<?> actualType, Throwable cause) {
        super("Type mismatch: Could not convert a value of type "
                        + actualType.getSimpleName()
                        + " to requested object of type "
                        + actualType.getSimpleName(),
                cause);
    }
}
