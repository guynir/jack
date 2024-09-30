package jack.utils;

import com.sun.nio.sctp.IllegalReceiveException;

/**
 * A collection of object and collection assertion methods to value objects states.
 *
 * @author Guy Raz Nir
 * @since 2024/09/11
 */
public class Asserts {

    /**
     * Assert that a given <i>obj</i> is not {@code null}.
     *
     * @param obj     Object to assert.
     * @param message Error message.
     * @throws IllegalReceiveException If <i>obj</i> is {@code null}.
     */
    public static void notNull(Object obj, String message) throws IllegalReceiveException {
        if (obj == null) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Assert that a given <i>state</i> is {@code true}.
     *
     * @param state   State to assert.
     * @param message Error message.
     * @throws IllegalReceiveException If <i>state</i> is {@code false}.
     */
    public static void state(boolean state, String message) throws IllegalReceiveException {
        if (!state) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Assert that a given string is neither {@code null} nor {@link String#isEmpty()} (zero-length string).
     *
     * @param str     String to test.
     * @param message Error message.
     * @throws IllegalReceiveException If either <i>str</i> is {@code null} or empty.
     */
    public static void notEmpty(String str, String message) throws IllegalReceiveException {
        if (str == null || str.isEmpty()) {
            throw new IllegalArgumentException(message);
        }
    }


}
