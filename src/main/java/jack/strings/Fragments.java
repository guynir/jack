package jack.strings;

import jack.utils.Asserts;

import java.util.List;

/**
 * A {@code Fragments} represents a collection of text and token fragments taken from a given string.
 *
 * @param text      A text block.
 * @param fragments List of fragments representing a <i>string</i>.
 * @author Guy Raz Nir
 * @since 2024/09/17
 */
public record Fragments(String text, List<Fragment> fragments) {

    public void visit(FragmentsVisitor visitor) {
        Asserts.notNull(visitor, "Visitor cannot be null.");

        fragments.forEach(fragment -> {
            if (fragment instanceof TextFragment f) {
                visitor.textFragment(f);
            } else if (fragment instanceof TokenFragment f) {
                visitor.tokenFragment(f);
            } else {
                throw new IllegalStateException("Unsupported fragment type: " + fragment.getClass().getSimpleName());
            }
        });
    }
}
