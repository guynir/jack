package jack.collections;

import jack.utils.Asserts;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Implementation of {@link ReadOnlyMap} that also supports adding new values to the model.<p>
 * The implementation does not allow either a null-key or a null-value, and it is thread-safe.
 *
 * <h2>Accessors and checks</h2>
 * Each accessor (e.g.: {@link #get(String)}, {@link #getAsInteger(String)}, ...) may generate the following
 * exceptions:
 * <ul>
 *     <li>{@link IllegalArgumentException} - if the provided key is {@code null}.</li>
 *     <li>{@link MissingKeyException} - if the provided key does not exist.</li>
 *     <li>{@link TypeMismatchException} - if value could not be converted to target type.</li>
 * </ul>
 * <p>
 * Examples for errors:
 * <pre>
 *     MutableStringKeyMap map = new MutableStringKeyMap();
 *
 *     // Will generate IllegalArgumentException.
 *     map.get(null);
 *
 *     // Will generate MissingKeyException.
 *     map.get("non-existing-key");
 *
 *     map.add("AGE", 1);
 *     // Will generate TypeMismatchException.
 *     map.getAsString("AGE");
 * </pre>
 * <br>
 *
 * <h2>Accessors fallback values</h2>
 * Each accessor provides an overloaded version with defaults (e.g.: for {@link #get(String)} accessor, the
 * implementation provides an overloaded version {@link #get(String, Object)}). For example:
 *
 * <pre>
 *     MutableStringKeyMap map = new MutableStringKeyMap();
 *
 *     // Will generate IllegalArgumentException.
 *     map.get("someKey");
 *
 *     // Will return "myFallbackValue", since "someKey" does not exist.
 *     map.get("someKey", "myFallbackValue");
 * </pre>
 *
 * <h2>Key case-insensitivity</h2>
 * The key is case-insensitive, which means that {@code key} and {@code KEY} are considered the same.
 *
 * <pre>
 *     MutableStringKeyMap map = new MutableStringKeyMap();
 *
 *     map.add("key", "someValue");
 *
 *     // o1 == o2
 *     Object o1 = map.get("key");
 *     Object o2 = map.get("KEY");
 * </pre>
 *
 * @author Guy Raz Nir
 * @since 2024/01/01
 */
public class MutableStringKeyMap implements ReadOnlyMap<String> {

    /**
     * The actual model holding all the data.
     */
    private final Map<String, Object> map = new ConcurrentHashMap<>();

    /**
     * Class constructor.
     */
    public MutableStringKeyMap() {
    }

    /**
     * Construct a new instance with preset data. Since this implementation ignores key case-sensitivity, it is possible
     * that some values may not reach this implementation. For example, given the two pairs: <i>('Name', 'Guy')</i> and
     * <i>('NAME', 'Nir')</i>, one will be kept while the other be discarded.
     *
     * @param source Source map to read from.
     * @throws IllegalArgumentException If <i>source</i> contains a {@code null} key.
     */
    public MutableStringKeyMap(Map<String, Object> source) throws IllegalArgumentException {
        if (source != null) {
            source.forEach(this::add);
        }
    }

    /**
     * @return A copy of the internal state. Changing the returned state may affect the internal state.
     */
    public Map<String, Object> getState() {
        return new HashMap<>(map);
    }

    /**
     * Add or update a key/value pair.
     *
     * @param key   Key identifying the object.
     * @param value Object to store.
     * @return {@code true} if the object was added, {@code false} if updated (previously existed).
     * @throws IllegalArgumentException If either arguments are {@code null}.
     */
    public boolean add(String key, Object value) throws IllegalArgumentException {
        Asserts.notNull(value, "Value cannot be null.");
        return this.map.put(normalizeKey(key), value) == null;
    }

    /**
     * Check if a given key exists in the map.
     *
     * @param key Key to test.
     * @return {@code true} if key exists, {@code false} if not.
     * @throws IllegalArgumentException If <i>key</i> is {@code null}.
     */
    @Override
    public boolean containsKey(String key) throws IllegalArgumentException {
        return map.containsKey(normalizeKey(key));
    }

    /**
     * Fetch an object from the map associated with a given key.
     *
     * @param key Key identifying the object.
     * @param <T> Expected type of object.
     * @return Object associated with <i>key</i>.
     * @throws MissingKeyException      If the ket does not exist.
     * @throws TypeMismatchException    If the object cannot be cast to type <i>T</i>.
     * @throws IllegalArgumentException If <i>key</i> is {@code null}.
     */
    @Override
    public <T> T get(String key) throws MissingKeyException, TypeMismatchException, IllegalArgumentException {
        Object value = map.get(normalizeKey(key));
        if (value == null) {
            throw new MissingKeyException("Unknown key: " + key);
        }

        try {
            // Cast the returned value here, so if it generates a ClassCastException, we can catch it
            // and translate it to our TypeMismatchException.
            //noinspection unchecked
            return (T) value;
        } catch (ClassCastException ex) {
            throw new TypeMismatchException("Could not convert object of type "
                    + value.getClass().getName()
                    + " to target type.",
                    ex);
        }
    }

    @Override
    public <T> T get(String key, T defaultValue) throws IllegalArgumentException {
        try {
            return get(key);
        } catch (MissingKeyException ex) {
            return defaultValue;
        }
    }

    @Override
    public int getAsInteger(String key) throws MissingKeyException, TypeMismatchException, IllegalArgumentException {
        return asNumber(key, Integer.TYPE).intValue();
    }

    @Override
    public int getAsInteger(String key, int defaultValue) throws MissingKeyException, TypeMismatchException, IllegalArgumentException {
        return asNumber(key, Integer.TYPE, defaultValue).intValue();
    }

    @Override
    public long getAsLong(String key) throws MissingKeyException, TypeMismatchException, IllegalArgumentException {
        return asNumber(key, Long.TYPE).longValue();
    }

    @Override
    public long getAsLong(String key, long defaultValue) throws MissingKeyException, TypeMismatchException, IllegalArgumentException {
        return asNumber(key, Long.TYPE, defaultValue).longValue();
    }

    @Override
    public boolean getAsBoolean(String key) throws MissingKeyException, TypeMismatchException, IllegalArgumentException {
        Object obj = get(key);
        try {
            return (boolean) obj;
        } catch (ClassCastException ex) {
            throw new TypeMismatchException(Boolean.TYPE, obj.getClass(), ex);
        }
    }

    @Override
    public boolean getAsBoolean(String key, boolean defaultValue) throws MissingKeyException, TypeMismatchException, IllegalArgumentException {
        try {
            return getAsBoolean(key);
        } catch (MissingKeyException ex) {
            return defaultValue;
        }
    }

    @Override
    public float getAsFloat(String key) throws MissingKeyException, TypeMismatchException, IllegalArgumentException {
        return asNumber(key, Float.TYPE).floatValue();
    }

    @Override
    public float getAsFloat(String key, float defaultValue) throws MissingKeyException, TypeMismatchException, IllegalArgumentException {
        return asNumber(key, Float.TYPE, defaultValue).floatValue();
    }

    @Override
    public double getAsDouble(String key) throws MissingKeyException, TypeMismatchException, IllegalArgumentException {
        return asNumber(key, Double.TYPE).doubleValue();
    }

    @Override
    public double getAsDouble(String key, double defaultValue) throws MissingKeyException, TypeMismatchException, IllegalArgumentException {
        return asNumber(key, Double.TYPE, defaultValue).doubleValue();
    }

    @Override
    public String getAsString(String key) throws MissingKeyException, TypeMismatchException, IllegalArgumentException {
        return get(key);
    }

    @Override
    public String getAsString(String key, String defaultValue) throws MissingKeyException, TypeMismatchException, IllegalArgumentException {
        try {
            return getAsString(key);
        } catch (MissingKeyException ex) {
            return defaultValue;
        }
    }

    /**
     * Return a normalized form of the key. In specific, convert string to lower-case.
     *
     * @param key Key to normalize.
     * @return Normalized form of the key (converted to lower-case).
     * @throws IllegalArgumentException If <i>key</i> is {@code null}.
     */
    protected String normalizeKey(String key) throws IllegalArgumentException {
        Asserts.notNull(key, "Key cannot be null.");
        return key.toUpperCase();
    }

    /**
     * Get a value as a {@link Number} object.
     *
     * @param key          Key identifying the object.
     * @param expectedType Actual exception type (e.g., {@code java.lang.Integer}, {@code java.lang.Float}, ...).
     * @param <N>          Generic type of numeric object (e.g., {@code lava.lang.Integer}, {@code lava.lang.Long}, ...).
     * @return Object as a number.
     * @throws IllegalArgumentException If <i>key</i> does not exist.
     * @throws TypeMismatchException    If the type could not be converted to {@code Number}.
     * @throws MissingKeyException      If <i>key</i> does not exist and no <i>fallback</i> is provided.
     */
    protected <N extends Number> Number asNumber(String key, Class<?> expectedType) throws IllegalArgumentException, TypeMismatchException {
        Object obj = get(key);

        // Try to convert the value to a 'java.lang.Number' object.
        try {
            return (Number) obj;
        } catch (ClassCastException ex) {
            throw new TypeMismatchException(expectedType, obj.getClass(), ex);
        }
    }

    /**
     * Get a value as a {@link Number} object.
     *
     * @param key          Key identifying the object.
     * @param expectedType Actual exception type (e.g., : {@code java.lang.Integer}, {@code java.lang.Float}, ...).
     * @param fallback     Optional fallback value to use. A non-{@code null} value indicates valid fallback.
     * @param <N>          Generic type of numeric object (e.g., {@code lava.lang.Integer}, {@code lava.lang.Long}, ...).
     * @return Object as a number.
     * @throws IllegalArgumentException If <i>key</i> does not exist.
     * @throws TypeMismatchException    If the type could not be converted to {@code Number}.
     * @throws MissingKeyException      If <i>key</i> does not exist and no <i>fallback</i> is provided.
     */
    protected <N extends Number> Number asNumber(String key, Class<?> expectedType, N fallback) throws IllegalArgumentException, TypeMismatchException {
        try {
            return asNumber(key, expectedType);
        } catch (MissingKeyException ex) {
            return fallback;
        }
    }
}
