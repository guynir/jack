package jack.collections;

/**
 * A value object representing a pair or strings.
 *
 * @author Guy Raz Nir
 * @since 2022/01/18
 */
public class StringPair extends Pair<String, String> {

    /**
     * Class constructor. Construct a new pair with both values as {@code null}.
     */
    public StringPair() {
        super();
    }

    /**
     * Class constructor.
     *
     * @param first  Initial first value.
     * @param second Initial second value.
     */
    public StringPair(String first, String second) {
        super(first, second);
    }
}
