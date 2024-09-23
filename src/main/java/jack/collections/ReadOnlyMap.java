package jack.collections;

/**
 * <p>This interface defines a readonly contract for querying data models.
 * </p>
 * An actual implementation may be a mutable model; however, this interface represents a readonly approach (query only,
 * with no ability to change data).
 *
 * @param <K> Generic type of key
 * @author Guy Raz Nir
 * @since 2024/01/01
 */
public interface ReadOnlyMap<K> {

    /**
     * Check if a given key exists in the model.
     *
     * @param key Key to test.
     * @return {@code true} if the map contains the key, {@code false} if not.
     * @throws IllegalArgumentException If <i>key</i> is {@code null}.
     */
    boolean containsKey(K key) throws IllegalArgumentException;

    /**
     * Fetch an object denoted by a <i>key</i>.
     *
     * @param key Key of the object.
     * @param <T> Generic type of object.
     * @return Object associated with <i>key</i>.
     * @throws MissingKeyException      If <i>key</i> does not exist.
     * @throws IllegalArgumentException If <i>key</i> is {@code null}.
     * @throws TypeMismatchException    If the object exists but could not be converted to type <i>T</i>.
     */
    <T> T get(K key) throws MissingKeyException, IllegalArgumentException, TypeMismatchException;

    /**
     * Fetch an object denoted by a given <i>key</i> with fallback default if key is missing.
     *
     * @param key          Key to search by.
     * @param defaultValue Default value to fall back to.
     * @param <T>          Generic type of object.
     * @return Either object associated with a key or <i>defaultValue</i> if key does not exist.
     * @throws IllegalArgumentException If <i>key</i> is {@code null}.
     */
    <T> T get(K key, T defaultValue) throws IllegalArgumentException;

    /**
     * Fetch an object as an integer value.
     *
     * @param key Key to look by.
     * @return Value associated with the key.
     * @throws MissingKeyException      If the provided <i>key</i> does not exist.
     * @throws TypeMismatchException    If object value could not be converted to numeric form.
     * @throws IllegalArgumentException If <i>key</i> is {@code null}.
     */
    int getAsInteger(K key) throws MissingKeyException, TypeMismatchException, IllegalArgumentException;

    /**
     * Fetch an object as an integer value, falling back to a given default if <i>key</i> is not found.
     *
     * @param key          Key to look by.
     * @param defaultValue Default value to fallback, in case <i>key</i> does not exist.
     * @return Value associated with the key or <i>defaultValue</i> if key does not exist.
     * @throws TypeMismatchException    If object value could not be converted to numeric form.
     * @throws IllegalArgumentException If <i>key</i> is {@code null}.
     */
    int getAsInteger(K key, int defaultValue) throws MissingKeyException, TypeMismatchException, IllegalArgumentException;

    /**
     * Fetch an object as a long value.
     *
     * @param key Key to look by.
     * @return Value associated with the key.
     * @throws MissingKeyException      If the provided <i>key</i> does not exist.
     * @throws TypeMismatchException    If object value could not be converted to numeric form.
     * @throws IllegalArgumentException If <i>key</i> is {@code null}.
     */
    long getAsLong(K key) throws MissingKeyException, TypeMismatchException, IllegalArgumentException;

    /**
     * Fetch an object as a long value, falling back to a given default if <i>key</i> is not found.
     *
     * @param key          Key to look by.
     * @param defaultValue Default value to fallback, in case <i>key</i> does not exist.
     * @return Value associated with the key or <i>defaultValue</i> if key does not exist.
     * @throws TypeMismatchException    If object value could not be converted to numeric form.
     * @throws IllegalArgumentException If <i>key</i> is {@code null}.
     */
    long getAsLong(K key, long defaultValue) throws MissingKeyException, TypeMismatchException, IllegalArgumentException;

    /**
     * Fetch an object as a boolean value.
     *
     * @param key Key to look by.
     * @return Value associated with the key.
     * @throws MissingKeyException      If the provided <i>key</i> does not exist.
     * @throws TypeMismatchException    If object value could not be converted to numeric form.
     * @throws IllegalArgumentException If <i>key</i> is {@code null}.
     */
    boolean getAsBoolean(K key) throws MissingKeyException, TypeMismatchException, IllegalArgumentException;

    /**
     * Fetch an object as a boolean value, falling back to a given default if <i>key</i> is not found.
     *
     * @param key          Key to look by.
     * @param defaultValue Default value to fallback, in case <i>key</i> does not exist.
     * @return Value associated with the key or <i>defaultValue</i> if key does not exist.
     * @throws TypeMismatchException    If object value could not be converted to numeric form.
     * @throws IllegalArgumentException If <i>key</i> is {@code null}.
     */
    boolean getAsBoolean(K key, boolean defaultValue) throws MissingKeyException, TypeMismatchException, IllegalArgumentException;

    /**
     * Fetch an object as a float value.
     *
     * @param key Key to look by.
     * @return Value associated with the key.
     * @throws MissingKeyException      If the provided <i>key</i> does not exist.
     * @throws TypeMismatchException    If object value could not be converted to numeric form.
     * @throws IllegalArgumentException If <i>key</i> is {@code null}.
     */
    float getAsFloat(K key) throws MissingKeyException, TypeMismatchException, IllegalArgumentException;

    /**
     * Fetch an object as a float value, falling back to a given default if <i>key</i> is not found.
     *
     * @param key          Key to look by.
     * @param defaultValue Default value to fallback, in case <i>key</i> does not exist.
     * @return Value associated with the key or <i>defaultValue</i> if key does not exist.
     * @throws TypeMismatchException    If object value could not be converted to numeric form.
     * @throws IllegalArgumentException If <i>key</i> is {@code null}.
     */
    float getAsFloat(K key, float defaultValue) throws MissingKeyException, TypeMismatchException, IllegalArgumentException;

    /**
     * Fetch an object as a double value.
     *
     * @param key Key to look by.
     * @return Value associated with the key.
     * @throws MissingKeyException      If the provided <i>key</i> does not exist.
     * @throws TypeMismatchException    If object value could not be converted to numeric form.
     * @throws IllegalArgumentException If <i>key</i> is {@code null}.
     */
    double getAsDouble(K key) throws MissingKeyException, TypeMismatchException, IllegalArgumentException;

    /**
     * Fetch an object as a double value, falling back to a given default if <i>key</i> is not found.
     *
     * @param key          Key to look by.
     * @param defaultValue Default value to fallback, in case <i>key</i> does not exist.
     * @return Value associated with the key or <i>defaultValue</i> if key does not exist.
     * @throws TypeMismatchException    If object value could not be converted to numeric form.
     * @throws IllegalArgumentException If <i>key</i> is {@code null}.
     */
    double getAsDouble(K key, double defaultValue) throws MissingKeyException, TypeMismatchException, IllegalArgumentException;

    /**
     * Fetch an object as a string value.
     *
     * @param key Key to look by.
     * @return Value associated with the key.
     * @throws MissingKeyException      If the provided <i>key</i> does not exist.
     * @throws TypeMismatchException    If object value could not be converted to numeric form.
     * @throws IllegalArgumentException If <i>key</i> is {@code null}.
     */
    String getAsString(K key) throws MissingKeyException, TypeMismatchException, IllegalArgumentException;

    /**
     * Fetch an object as a string value, falling back to a given default if <i>key</i> is not found.
     *
     * @param key          Key to look by.
     * @param defaultValue Default value to fallback, in case <i>key</i> does not exist.
     * @return Value associated with the key or <i>defaultValue</i> if key does not exist.
     * @throws TypeMismatchException    If object value could not be converted to numeric form.
     * @throws IllegalArgumentException If <i>key</i> is {@code null}.
     */
    String getAsString(K key, String defaultValue) throws MissingKeyException, TypeMismatchException, IllegalArgumentException;


}
