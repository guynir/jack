package jack.strings;

import jack.utils.Asserts;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * This builder helps construct a list of tokens.
 *
 * @author Guy Raz Nir
 * @since 2024/08/10
 */
public class FragmentsBuilder {

    /**
     * Next offset to append a part.
     */
    private int offset;

    /**
     * Length of token's prefix.
     */
    private final int lengthOfTokenPrefix;

    /**
     * Length of token's suffix.
     */
    private final int lengthOfTokenSuffix;

    /**
     * List of parsed parts accumulated.
     */
    private final List<Fragment> tokens = new LinkedList<>();

    /**
     * Creates a new builder configured based on the default tokenizer.
     *
     * @return A new builder.
     */
    public static FragmentsBuilder newBuilder() {
        return newBuilder(StringFragmentator.getDefault());
    }

    /**
     * Creates a new builder configured based on a given tokenizer. Configuration relates to token parsing, mainly,
     * which are a token's prefix and suffix strings.
     *
     * @param tokenizer Tokenizer to configure this builder upon.
     * @return A new builder.
     */
    public static FragmentsBuilder newBuilder(StringFragmentator tokenizer) {
        Asserts.notNull(tokenizer, "Text tokenizer cannot be null.");
        return new FragmentsBuilder(tokenizer.getTokenPrefix().length(), tokenizer.getTokenSuffix().length());
    }

    /**
     * Class constructor.
     */
    private FragmentsBuilder(int lengthOfTokenPrefix, int lengthOfTokenSuffix) {
        this.lengthOfTokenPrefix = lengthOfTokenPrefix;
        this.lengthOfTokenSuffix = lengthOfTokenSuffix;
    }

    /**
     * Add simple text block.
     *
     * @param contents Text to add.
     * @return This builder.
     * @throws IllegalArgumentException If <i>contents</i> are {@code null} or empty.
     */
    public FragmentsBuilder addText(String contents) throws IllegalArgumentException {
        return appendInternal(contents, false);
    }

    /**
     * Add token block.
     *
     * @param contents The contents of token; includes the token's prefix and suffix (e.g., <i>${....}</i>).
     * @return This builder.
     * @throws IllegalArgumentException If <i>contents</i> are {@code null} or empty.
     */
    public FragmentsBuilder addToken(String contents) throws IllegalArgumentException {
        return appendInternal(contents, true);
    }

    /**
     * @return A copy of parts' list built by this builder.
     */
    public List<Fragment> toList() {
        return new ArrayList<>(tokens);
    }

    /**
     * Append a text or a token part to this builder. If the appended part is a token, its prefix and suffix are
     * removed.
     *
     * @param contents Contents of part.
     * @param isToken  {@code true} if this is a token, {@code false} if it is a simple text.
     * @return This instance.
     * @throws IllegalArgumentException If <i>contents</i> is {@code null} or empty.
     */
    protected FragmentsBuilder appendInternal(String contents, boolean isToken) throws IllegalArgumentException {
        Asserts.notEmpty(contents, "Contents cannot be empty.");
        int endOffset = offset + contents.length() - 1;

        Fragment fragment;
        if (isToken) {
            // In the case of token, remove the token's prefix and suffix.
            contents = contents.substring(lengthOfTokenPrefix, contents.length() - lengthOfTokenSuffix);
            fragment = new TokenFragment(contents, offset, endOffset);
        } else {
            fragment = new TextFragment(contents, offset, endOffset);
        }

        tokens.add(fragment);

        this.offset = endOffset + 1;
        return this;
    }


}
