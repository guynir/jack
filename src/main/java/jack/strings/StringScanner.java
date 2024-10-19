package jack.strings;

import jack.utils.Asserts;

import java.util.LinkedList;
import java.util.List;

/**
 * A string scanner is a utility that helps search a <i>substring</i> within another string.<p>
 * </p>
 * This scanner supports several characteristics:
 * <ul>
 *    <li>
 *        If the scanner encounters a match of the <i>substring</i>, but the match is preceded with a well known
 *        <i>escape character</i>, the match is skipped and the scanner continues to the next match.
 *    </li>
 *    <li>
 *        This scanner maintains an <i>offset</i> state, which is the last location a match was found. Each search
 *        begins at the last location the previous search stopped.<p>
 *        The first search will start at offset #0.<p>
 *        If the last search reached the end of the string without finding a match, the <i>offset</i> state will
 *        be the length of the string.
 *    </li>
 * </ul>
 * <p></p>
 * This class provides a {@link #consumed()} flag which indicates if this scanner consumed the entire string
 * in context or not.
 *
 * @author Guy Raz Nir
 * @since 2024/08/06
 */
public class StringScanner {

    /**
     * Default escape character this implementation uses (unless alternative one is specified).
     */
    public static final char DEFAULT_ESCAPE_CHARACTER = '\\';

    /**
     * The string to scan.
     */
    private final String str;

    /**
     * Character to use as an <i>escape character</i> prefix.
     */
    private final char escapeCharacter;

    /**
     * Current offset of cursor (where to begin searching from).<p>
     * The search always begins one character after <i>offset</i>.<p>
     * The Initial value is '-1' (which indicates that scan has no began yet).
     */
    private int offset = -1;

    /**
     * Class constructor. Use {@link #DEFAULT_ESCAPE_CHARACTER default escaping character}.
     *
     * @param str String to scan.
     */
    public StringScanner(String str) {
        this(str, DEFAULT_ESCAPE_CHARACTER);
    }

    /**
     * Class constructor.
     *
     * @param str             String to scan.
     * @param escapeCharacter An escape character.
     */
    public StringScanner(String str, char escapeCharacter) throws IllegalArgumentException {
        Asserts.notNull(str, "Input string cannot be null.");
        this.str = str;
        this.escapeCharacter = escapeCharacter;
    }

    /**
     * <p>Search for a <i>substr</i> occurrence within a given string -- <i>str</i>. The search starts
     * at a given <i>offset</i>.
     * </p>
     * A <i>substr</i> which is prefixed by an escape character is skipped.
     *
     * @param str             String to search within.
     * @param substr          Substring to look for.
     * @param offset          Offset to start with. The offset starts at 0.
     * @param escapeCharacter An escape character.
     * @return Location of first occurrence (starting at <i>offset</i>) or -1 if no such substring is found.
     * @throws IllegalArgumentException If either <i>str</i> or <i>substr</i> are {@code null} or if <i>offset</i> is
     *                                  negative.
     */
    public static int findSubstring(String str, String substr, int offset, char escapeCharacter) {
        Asserts.notNull(str, "Input string cannot be null.");
        Asserts.notNull(substr, "Substring cannot be null.");
        Asserts.state(offset >= 0, "Offset cannot be negative value.");
        return search(str, substr, offset, escapeCharacter);
    }

    /**
     * <p>Search for a <i>substr</i> occurrence within a given string -- <i>str</i>. The search starts
     * at a given <i>offset</i>.
     * </p>
     * A <i>substr</i> which is prefixed by an {@link #DEFAULT_ESCAPE_CHARACTER escape character} is skipped.
     *
     * @param str    String to search within.
     * @param substr Substring to look for.
     * @param offset Offset to start with. The offset starts at 0.
     * @return Location of first occurrence (starting at <i>offset</i>) or -1 if no such substring is found.
     * @throws IllegalArgumentException If either <i>str</i> or <i>substr</i> are {@code null} or if <i>offset</i> is
     *                                  negative.
     */
    @SuppressWarnings("unused")
    public static int findSubstring(String str, String substr, int offset) {
        return findSubstring(str, substr, offset, DEFAULT_ESCAPE_CHARACTER);
    }

    /**
     * Search for the first non-escaped <i>substr</i> occurrence within a given string <i>str</i>.
     *
     * @param str    String to search within.
     * @param substr Substring to look for.
     * @return Location of first occurrence (starting at <i>offset</i>) or -1 if no such substring is found.
     * @throws IllegalArgumentException If either <i>str</i> or <i>substr</i> are {@code null} or if <i>offset</i> is
     *                                  negative.
     */
    public static int findSubstring(String str, String substr) {
        Asserts.notNull(str, "Input string cannot be null.");
        Asserts.notNull(substr, "Substring cannot be null.");
        return search(str, substr, 0, DEFAULT_ESCAPE_CHARACTER);
    }

    /**
     * Split a given string based on a given <i>splitter</i>.
     * If a <i>splitter</i> is preceded by an escape character ({@link #DEFAULT_ESCAPE_CHARACTER}),
     * the occurrence is skipped.
     *
     * @param str      String to split.
     * @param splitter Expression to split by.
     * @return Array of strings parsed from <i>str</i>. If <i>str</i> is empty, an empty array is returned.
     * @throws IllegalArgumentException If either <>str</> or <i>splitter</i> are {@code null} or if <i>splitter</i>
     *                                  is empty.
     */
    public static String[] split(String str, String splitter) throws IllegalArgumentException {
        return split(str, splitter, DEFAULT_ESCAPE_CHARACTER);
    }

    /**
     * <p>Split a given string based on a given <i>splitter</i>.
     * If a <i>splitter</i> is preceded by an <i>escapeCharacter</i>, the occurrence is skipped.
     * </p>
     * <p>For example, given the string <i>Red;Green;Blue</i> and the splitter <i>;</i>, this call will generate the
     * output: {@code { "Red", "Green", "Blue" }}.</p>
     * If a splitter is preceded by an escape character, such as <i>/</i>, the splitting string is skipped.
     * For example, <i>Red\;Green;Blue</i> with a splitter <i>;</i> and an escape character <i>\</i>, the produced
     * output will be: {@code { "Red\;Green", "Blue" }}.
     * </p>
     *
     * @param str             String to split.
     * @param splitter        Expression to split by.
     * @param escapeCharacter An escape character.
     * @return Array of strings parsed from <i>str</i>. If <i>str</i> is empty, an empty array is returned.
     * @throws IllegalArgumentException If either <>str</> or <i>splitter</i> are {@code null} or if <i>splitter</i>
     *                                  is empty.
     */
    public static String[] split(String str, String splitter, char escapeCharacter) throws IllegalArgumentException {
        Asserts.notNull(str, "Input string cannot be null.");
        Asserts.notEmpty(splitter, "Splitter cannot be null or empty string.");

        if (str.isEmpty()) {
            return new String[0];
        }

        List<String> result = new LinkedList<>();
        int offset = 0;
        int nextOffset = 0;
        while (nextOffset < str.length()) {
            nextOffset = search(str, splitter, offset, escapeCharacter);
            if (nextOffset == -1) {
                nextOffset = str.length();
            }
            result.add(str.substring(offset, nextOffset));
            offset = nextOffset + splitter.length();
        }

        return result.toArray(new String[0]);
    }

    /**
     * <p>Search for a <i>substr</i> occurrence within a given string -- <i>str</i>. The search starts
     * at a given <i>offset</i>.
     * </p>
     * <p>A <i>substr</i> which is prefixed by an escape character is skipped.
     * </p>
     * <p>NOTE: This function does not validate input variable. If either arguments contains invalid values (e.g.:
     * <i>str</i> or <i>substr</i> are {@code null} or <i>offset</i> is negative) - a platform runtime exception
     * may be thrown (such as {@code IndexOutOfBoundsException} or {@code NullPointerException}).
     * </p>
     * Caller must verify input values.
     *
     * @param str             String to search within.
     * @param substr          Substring to look for.
     * @param offset          Offset to start with. The offset starts at 0.
     * @param escapeCharacter An escape character.
     * @return Location of first occurrence (starting at <i>offset</i>) or -1 if no such substring is found.
     */
    private static int search(String str, String substr, int offset, char escapeCharacter) {
        int result = -1;
        while (offset + substr.length() - 1 < str.length()) {
            result = offset = str.indexOf(substr, offset);
            if (offset > 0 && str.charAt(offset - 1) == escapeCharacter) {
                // We found a match, but it is disqualified, as the match is prefixed by an escape character.
                offset++;
            } else {
                break;
            }
        }

        return result;
    }

    /**
     * Find the next <i>substr</i> is our string.
     *
     * @param substr Substring to search for.
     * @return Offset of substring (0-based offset) or -1 if substring not found.
     */
    public int find(String substr) {
        if (consumed()) {
            return -1;
        }

        int index = findSubstring(str, substr, offset + 1, escapeCharacter);

        // If the substring is not found -- set the offset to the end of the string and return -1
        // to indicate the substring is not found.
        offset = index == -1 ? str.length() - 1 : index;

        return index;
    }

    /**
     * Provide indication if this finder has been fully consumed. See the class description for more details.
     *
     * @return {@code true} if scanner is consumed, {@code false} if not.
     */
    public boolean consumed() {
        return offset + 1 == str.length();
    }
}
