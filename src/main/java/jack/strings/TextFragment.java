package jack.strings;

/**
 * A fragment represents a simple within a string.
 *
 * @author Guy Raz Nir
 * @since 2024/09/17
 */
public final class TextFragment extends Fragment {

    /**
     * Class constructor.
     *
     * @param contents    Text contents of this fragment.
     * @param startOffset Starting offset, inclusive, of this fragment within the parent string.
     * @param endOffset   Ending offset, inclusive, of this fragment within the parent string.
     */
    public TextFragment(String contents, int startOffset, int endOffset) {
        super(contents, startOffset, endOffset);
    }
}