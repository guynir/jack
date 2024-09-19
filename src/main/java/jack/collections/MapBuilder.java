package jack.collections;

import jack.utils.Asserts;

import java.util.HashMap;
import java.util.Map;

/**
 * A utility for easily and optimally build {@link HashMap}s.
 *
 * @param <K> Generic type of first value.
 * @param <V> Generic type of value.
 * @author Guy Raz Nir
 * @since 2024/02/04
 */
public class MapBuilder<K, V> {

    /**
     * Map created by this builder.
     */
    public final Map<K, V> map;

    /**
     * Class constructor.
     *
     * @param initialSize Initial size of the map, for optimal memory allocation and performance.
     */
    private MapBuilder(int initialSize) {
        this.map = new HashMap<>(Math.max(initialSize, 0));
    }

    /**
     * Creates a new builder.
     *
     * @param <K> Generic key type.
     * @param <V> Generic key value.
     * @return New builder with an empty map.
     */
    public static <K, V> MapBuilder<K, V> newBuilder() {
        return new MapBuilder<>(0);
    }

    /**
     * Creates a new builder with initial size and optionally specified values.
     *
     * @param initialSize Initial size of the map.
     * @param entries     Optional list of entries.
     * @param <K>         Generic type of key.
     * @param <V>         Generic type of value.
     * @return New builder encapsulating a map with <i>entries</i>.
     */
    @SafeVarargs
    public static <K, V> MapBuilder<K, V> newBuilder(int initialSize, Map.Entry<K, V>... entries) {
        // Create a new map builder with predefined size.
        MapBuilder<K, V> builder = new MapBuilder<>(Math.max(initialSize, entries.length));

        // Add optionally provided pairs.
        for (Map.Entry<K, V> entry : entries) {
            builder.map.put(entry.getKey(), entry.getValue());
        }

        return builder;
    }

    /**
     * Creates a new builder with initial size and optionally specified values.
     *
     * @param initialSize Initial size of the map.
     * @param entries     Optional list of entries.
     * @param <K>         Generic type of key.
     * @param <V>         Generic type of value.
     * @return New builder encapsulating a map with <i>entries</i>.
     */
    @SafeVarargs
    public static <K, V> MapBuilder<K, V> newBuilder(int initialSize, Pair<K, V>... entries) {
        // Create a new map builder with predefined size.
        MapBuilder<K, V> builder = new MapBuilder<>(Math.max(initialSize, entries.length));

        // Add optionally provided pairs.
        for (Pair<K, V> entry : entries) {
            builder.map.put(entry.first, entry.second);
        }

        return builder;
    }

    /**
     * Appends an additional key/value pair to the map.
     *
     * @param key   Key.
     * @param value Value.
     * @return This instance.
     */
    public MapBuilder<K, V> with(K key, V value) {
        map.put(key, value);
        return this;
    }

    /**
     * Appends and additional key/value pair to the map.
     *
     * @param pair Pair object containing key/value.
     * @return This instance.
     */
    public MapBuilder<K, V> with(Pair<K, V> pair) {
        Asserts.notNull(pair, "Pair object cannot be null.");
        map.put(pair.first, pair.second);
        return this;
    }
}
