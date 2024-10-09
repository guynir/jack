package jack.strings;

/**
 * This fragment represents a token within a string.
 *
 * @author Guy Raz Nir
 * @since 2024/09/17
 */
public final class TokenFragment extends Fragment {

    /**
     * Class constructor.
     *
     * @param contents    Token contents (excluding token's prefix and suffix).
     * @param startOffset Starting offset, inclusive, of this fragment within the parent string.
     * @param endOffset   Ending offset, inclusive, of this fragment within the parent string.
     */
    public TokenFragment(String contents, int startOffset, int endOffset) {
        super(contents, startOffset, endOffset);
    }
}
