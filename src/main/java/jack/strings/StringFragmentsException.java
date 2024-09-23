package jack.strings;

/**
 * This exception is thrown when parsing of a string to fragments fails.
 *
 * @author Guy Raz Nir
 * @since 2024/08/10
 */
public class StringFragmentsException extends RuntimeException {

    /**
     * Offset of error.
     */
    private final int offset;

    /**
     * Class constructor.
     *
     * @param message Error message.
     */
    public StringFragmentsException(String message) {
        this(message, -1);
    }

    /**
     * Class constructor.
     *
     * @param message Error message.
     * @param offset  Character offset of failure.
     */
    public StringFragmentsException(String message, int offset) {
        super(message);
        this.offset = offset;
    }

    /**
     * @return An offset within a string that generated the exception. May be <i>-1</i> if no such offset exists.
     */
    public int getOffset() {
        return offset;
    }

    public boolean hasOffset() {
        return offset != -1;
    }
}
