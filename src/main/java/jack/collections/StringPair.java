package jack.collections;

/**
 * A value object representing a pair or strings.
 *
 * @author Guy Raz Nir
 * @since 2022/01/18
 */
public class StringPair extends Pair<String, String> {

    public StringPair() {
        super();
    }

    public StringPair(String key, String value) {
        super(key, value);
    }
}
