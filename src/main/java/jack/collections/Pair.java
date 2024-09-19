package jack.collections;

/**
 * Represents a generic paired values, represented as key and value.
 *
 * @param <V1> Generic type of first value.
 * @param <V2> Generic type of second value.
 * @author Guy Raz Nir
 * @since 2022/01/18
 */
public class Pair<V1, V2> {

    /**
     * Key.
     */
    public V1 first;

    /**
     * Value.
     */
    public V2 second;

    /**
     * Class constructor.
     */
    public Pair() {
    }

    /**
     * Class constructor.
     *
     * @param first  Initial first value.
     * @param second Initial second value.
     */
    public Pair(V1 first, V2 second) {
        this.first = first;
        this.second = second;
    }

    /**
     * Helper method for easily creating new {@code Pair} instance.
     *
     * @param first  First value.
     * @param second Second value.
     * @param <V1>   Generic type of first value.
     * @param <V2>   Generic type of second value.
     * @return New {@code Pair} instance.
     */
    public static <V1, V2> Pair<V1, V2> of(V1 first, V2 second) {
        return new Pair<>(first, second);
    }
}
