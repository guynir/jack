package jack.strings;

/**
 * A visitor interface for visiting {@link Fragments}.
 *
 * @author Guy Raz Nir
 * @since 2024/10/01
 */
public interface FragmentsVisitor {

    /**
     * Called when the current fragment is a {@code TextFragment}.
     *
     * @param fragment Current fragment.
     */
    void textFragment(TextFragment fragment);

    /**
     * Called when the current fragment is a {@code TokenFragment}.
     *
     * @param fragment Current fragment.
     */
    void tokenFragment(TokenFragment fragment);
}
