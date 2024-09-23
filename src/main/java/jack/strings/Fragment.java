package jack.strings;

import java.util.Objects;
import java.util.StringJoiner;

/**
 * A text fragment representing part of a string.
 *
 * @author Guy Raz Nir
 * @since 2024/09/17
 */
public sealed class Fragment permits TextFragment, TokenFragment {

    /**
     * Text contents of this fragment.
     */
    public final String contents;

    /**
     * Fragment starting offset, inclusive.
     */
    public final int startOffset;

    /**
     * Fragment ending offset, inclusive.
     */
    public final int endOffset;

    public Fragment(String contents, int startOffset, int endOffset) {
        this.contents = contents;
        this.startOffset = startOffset;
        this.endOffset = endOffset;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Fragment fragment)) return false;
        return startOffset == fragment.startOffset && endOffset == fragment.endOffset && Objects.equals(contents, fragment.contents);
    }

    @Override
    public int hashCode() {
        return Objects.hash(contents, startOffset, endOffset);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Fragment.class.getSimpleName() + "[", "]")
                .add("contents='" + contents + "'")
                .add("startOffset=" + startOffset)
                .add("endOffset=" + endOffset)
                .toString();
    }
}
