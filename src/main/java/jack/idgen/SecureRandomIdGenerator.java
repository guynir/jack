package jack.idgen;

import jack.utils.Asserts;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.HashSet;
import java.util.Set;

/**
 * Generate unique identifier using Java's internal {@code SecureRandom} implementation and current time (measured in
 * milliseconds) to increase security. This implementation has two core properties that define the strength and
 * representation form of the identifier:
 * <ul>
 * <li>
 * {@link SecureRandomIdGenerator#setSymbols(char[]) symbols} - Define table of characters that are used
 * for representing the generated identifier. Default configuration uses A-Z, a-z, 0-9 (62 characters).
 * </li>
 * <li>
 * {@link SecureRandomIdGenerator#setLength(int) length} - Define now many symbols should be the generated
 * value. Default is 30 symbols.
 * </li>
 * </ul>
 * The strength of the generated identifier is defined as {@code symbols ^ length}. The larger the
 * table of characters and the longer the generated key, the less likelihood to generate repeating tokens.
 *
 * @author Guy Raz Nir
 * @since 2021/10/20
 */
public class SecureRandomIdGenerator implements StringIdGenerator {

    /**
     * Implementation secure-random for generating identifiers.
     */
    private final SecureRandom secureRandom = createSecureRandom();

    /**
     * Length of generated identifier.
     */
    private int length;

    /**
     * List of symbols to use for generating string-based identifier.
     */
    private char[] symbols;

    /**
     * Default length of generated identifier.
     */
    public static final int DEFAULT_IDENTIFIER_LENGTH = 30;

    /**
     * Default set of symbols to use for generation.
     */
    @SuppressWarnings("SpellCheckingInspection")
    public static final String DEFAULT_SYMBOL_SET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    /**
     * Class constructor.
     * <p>
     * Creates a new security random generator using {@link #DEFAULT_IDENTIFIER_LENGTH default length} and
     * {@link #DEFAULT_SYMBOL_SET default symbols set}.
     */
    public SecureRandomIdGenerator() {
        this(DEFAULT_IDENTIFIER_LENGTH, DEFAULT_SYMBOL_SET);
    }

    /**
     * Class constructor.
     * <p>
     * Creates a new id generator with a given length and default
     * {@link SecureRandomIdGenerator#DEFAULT_SYMBOL_SET symbols set}.
     *
     * @param length Length of identifier to generate.
     * @throws IllegalArgumentException If <i>length</i> is less than 1.
     */
    public SecureRandomIdGenerator(int length) throws IllegalArgumentException {
        this(length, DEFAULT_SYMBOL_SET);
    }

    /**
     * Class constructor.
     * <p>
     * Creates a new id generator with {@link #DEFAULT_IDENTIFIER_LENGTH default length} and a given <i>symbols</i> set.
     *
     * @param symbols Symbols set to use.
     * @throws IllegalArgumentException If either <i>symbols</i> is {@code null}, empty or contain duplicates.
     */
    public SecureRandomIdGenerator(char[] symbols) throws IllegalArgumentException {
        this(DEFAULT_IDENTIFIER_LENGTH, symbols);
    }

    /**
     * Class constructor.
     *
     * @param length  Length of generated identifier.
     * @param symbols List of symbols to use.
     * @throws IllegalArgumentException If either <i>length</i> is less than 1 or <i>symbols</i> is {@code null},
     *                                  empty or contains duplicates.
     */
    public SecureRandomIdGenerator(int length, String symbols) throws IllegalArgumentException {
        this(length, symbols.toCharArray());
    }

    /**
     * Class constructor.
     *
     * @param length  Length of generated identifier.
     * @param symbols List of symbols to use.
     * @throws IllegalArgumentException If either <i>length</i> is less than 1 or <i>symbols</i> is {@code null},
     *                                  empty or contains duplicates.
     */
    public SecureRandomIdGenerator(int length, char[] symbols) throws IllegalArgumentException {
        //
        // Assert that 'length' property is well-defined.
        //
        if (length < 1) {
            throw new IllegalArgumentException("Invalid length: " + length + " (must be greater than 0).");
        }

        //
        // Assert that symbols is defined, not empty and does not contain duplicates.
        //
        Asserts.notNull(symbols, "Symbols cannot be null.");
        if (symbols.length == 0) {
            throw new IllegalArgumentException("Symbols cannot be empty.");
        }
        if (hasDuplicates(symbols)) {
            throw new IllegalArgumentException("Characters in symbols argument are not unique (some characters appears more than once).");
        }

        this.length = length;
        this.symbols = symbols;
    }

    @Override
    public String generate() {
        // Generate secure random.
        byte[] raw = secureRandom.generateSeed((int) Math.ceil(Math.log(symbols.length) * length + 1));

        // Convert byte-array to integer value for simpler string generation.
        BigInteger value = new BigInteger(1, raw);
        char[] result = new char[length];
        int index = 0;
        while (value.compareTo(BigInteger.ZERO) > 0 && index < length) {
            BigInteger[] split = value.divideAndRemainder(BigInteger.valueOf(symbols.length));
            result[index] = symbols[split[1].intValue()];
            value = split[0];
            index++;
        }

        // Make sure our generated identifier standard with the requested length.
        if (index != length) {
            throw new IllegalStateException(
                    String.format("Unexpected: Generated identifier length was shorter than expected (required: %d; actual: %d).",
                            index,
                            length));
        }

        return new String(result);
    }

    /**
     * Set the length of the generated identifier.
     *
     * @param length Length. Must be greater than 1.
     * @throws IllegalArgumentException If <i>length &lt;= 1</i>
     */
    public void setLength(int length) {
        Asserts.state(length > 1, "Length must be greater than 1.");
        this.length = length;
    }

    /**
     * Sets a new symbols set for identifier generation.
     *
     * @param symbols Set of symbols.
     * @throws IllegalArgumentException If <i>symbols</i> is either {@code null} or is too short.
     */
    public void setSymbols(char[] symbols) {
        Asserts.notNull(symbols, "Symbols list cannot be null.");
        Asserts.state(symbols.length > 1, "Symbols list must contain at least 2 symbols.");
        this.symbols = symbols;
    }

    /**
     * @return New secure random instantiated with random seed.
     */
    private static SecureRandom createSecureRandom() {
        BigInteger value = BigInteger.valueOf(System.currentTimeMillis());
        return new SecureRandom(value.toByteArray());
    }

    /**
     * Check if a given array of characters contains duplicates.
     *
     * @param chars Characters to examine.
     * @return {@code true} if array contain duplicates, {@code false} if all characters are unique.
     */
    private static boolean hasDuplicates(char[] chars) {
        Set<Character> set = new HashSet<>(chars.length);
        for (char c : chars) {
            set.add(c);
        }
        return chars.length != set.size();
    }

}