package jack.strings;

import jack.utils.Asserts;

import java.util.LinkedList;
import java.util.List;

/**
 * Parse a given text into <i>fragments</i> where each fragment is either a simple text or a token (typically a
 * placeholder).<p>
 * </p>
 * A fragment is identified by a predefined prefix and suffix. By default, a token takes the form of <i>@{....}</i>,
 * however, the prefix and suffix (i.e.: <i>${</i> and <i>}</i>) can be defined during class construction.<p>
 * </p>
 * As an example, the following text:
 * <pre>
 *     Hello Mr. ${name}, pleased to meet you.
 * </pre>
 * is parsed as:
 * [TEXT: Hello Mr. ][TOKEN: name][TEXT:, pleased to meet you.]<p>
 * </p>
 * Note: A token part is provided without its prefix and suffix (e.g., <i>${name}</i> is returned as <i>name</i>).
 *
 * @author Guy Raz Nir
 * @since 2024/08/07
 */
public class StringFragmentator {

    // Fragmentator

    /**
     * A token prefix, marks a beginning of a token; e.g., <i>${</i>.
     */
    private final String tokenPrefix;

    /**
     * A token suffix, marks a ending of a token; e.g., <i>}</i>.
     */
    private final String tokenSuffix;

    /**
     * Default token prefix.
     */
    private static final String DEFAULT_TOKEN_PREFIX = "${";

    /**
     * Default toke suffix.
     */
    private static final String DEFAULT_TONE_SUFFIX = "}";

    /**
     * A singleton of a default parser, which will probably be used a lot.
     */
    private static final StringFragmentator DEFAULT_PARSER = new StringFragmentator();

    /**
     * Class constructor. Initialize a parser using default token prefix suffix, e.g., <i>${.....}</i>.
     */
    public StringFragmentator() {
        this(DEFAULT_TOKEN_PREFIX, DEFAULT_TONE_SUFFIX);
    }

    /**
     * Class constructor.
     *
     * @param tokenPrefix A token prefix.
     * @param tokenSuffix A token suffix.
     * @throws IllegalArgumentException If either arguments are {@code null}.
     */
    public StringFragmentator(String tokenPrefix, String tokenSuffix) throws IllegalArgumentException {
        Asserts.notEmpty(tokenPrefix, "Token prefix cannot be null or empty.");
        Asserts.notEmpty(tokenSuffix, "Token suffix cannot be null or empty.");
        this.tokenPrefix = tokenPrefix;
        this.tokenSuffix = tokenSuffix;
    }

    public static Fragments parse(String text) throws IllegalArgumentException {
        return DEFAULT_PARSER.parsePattern(text);
    }

    /**
     * @return A singleton instance of the default text tokenizer.
     */
    public static StringFragmentator getDefault() {
        return DEFAULT_PARSER;
    }

    /**
     * Parse a given text into <i>parsed parts</i>.
     *
     * @param text Text to parse.
     * @return List of <i>parsed parts</i> representing the text.
     */
    public Fragments parsePattern(String text) throws IllegalArgumentException {
        Asserts.notNull(text, "Text cannot be null.");
        List<Fragment> fragments = new LinkedList<>();

        if (!text.isEmpty()) {
            int currentOffset = 0;
            StringScanner finder = new StringScanner(text);

            while (!finder.consumed()) {
                // Search for the next occurrence of token prefix.
                int tokenStartIndex = finder.find(tokenPrefix);

                // If we find a token prefix, the text block ends at that beginning.
                // If we do not find a token prefix -- we'll consume the string to its end.
                int textBlockEndOffset = tokenStartIndex > -1 ? tokenStartIndex : text.length();

                // If the current text block is not empty, create a new text fragment.
                if (textBlockEndOffset > currentOffset) {
                    fragments.add(new TextFragment(
                            text.substring(currentOffset, textBlockEndOffset),
                            currentOffset,
                            textBlockEndOffset));
                }

                //
                // If we detected a token prefix, search for the token's suffix.
                //
                if (tokenStartIndex > -1) {
                    int tokenEndIndex = finder.find(tokenSuffix);
                    if (tokenEndIndex == -1) {
                        // Error !!! Opening token prefix without closing suffix.
                        throw new StringFragmentsException("Opening brackets '{' at offset "
                                + tokenStartIndex
                                + " without a matching closing brackets '}'."
                                , tokenStartIndex);
                    }
                    fragments.add(new TokenFragment(
                            text.substring(tokenStartIndex + tokenPrefix.length(),
                                    tokenEndIndex),
                            tokenStartIndex,
                            tokenEndIndex));
                    currentOffset = tokenEndIndex + 1;
                }
            }
        }
        return new Fragments(text, fragments);
    }

    public String getTokenPrefix() {
        return tokenPrefix;
    }

    public String getTokenSuffix() {
        return tokenSuffix;
    }
}


