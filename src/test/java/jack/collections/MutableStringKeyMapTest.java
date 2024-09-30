package jack.collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Test suite for {@link MutableStringKeyMap}.
 *
 * @author Guy Raz Nir
 * @since 2021/12/09
 */
public class MutableStringKeyMapTest {

    //
    // Sample values for testing.
    //
    private static final String KEY = "someRandomKey";
    private static final int INT_VALUE = 1;
    private static final long LONG_VALUE = 2L;
    private static final float FLOAT_VALUE = 3.1f;
    private static final double DOUBLE_VALUE = 4.2d;
    private static final boolean BOOLEAN_VALUE = true;
    private static final String STRING_VALUE = "string";
    /**
     * Instance to test.
     */
    private MutableStringKeyMap map;

    /**
     * Test fixture -- creates a new map before each test.
     */
    @BeforeEach
    public void setUp() {
        map = new MutableStringKeyMap();
    }

    /**
     * Test should return an object associated with a known key.
     */
    @Test
    @DisplayName("Test should return an expected object.")
    public void testShouldReturnAnObject() {
        // Add sample value.
        map.add(KEY, STRING_VALUE);

        // Make sure we can fetch the value.
        assertThat((Object) map.get(KEY)).isEqualTo(STRING_VALUE);
    }

    /**
     * Test should return values converted to specific basic types, such as integers, boolean, string, ...
     */
    @Test
    @DisplayName("Test should return type-safe values")
    public void testShouldReturnValueAs() {
        map.add(Integer.class.getSimpleName(), INT_VALUE);
        map.add(Long.class.getSimpleName(), LONG_VALUE);
        map.add(Float.class.getSimpleName(), FLOAT_VALUE);
        map.add(Double.class.getSimpleName(), DOUBLE_VALUE);
        map.add(Boolean.class.getSimpleName(), BOOLEAN_VALUE);
        map.add(String.class.getSimpleName(), STRING_VALUE);

        assertThat(map.getAsInteger(Integer.class.getSimpleName())).isEqualTo(INT_VALUE);
        assertThat(map.getAsLong(Long.class.getSimpleName())).isEqualTo(LONG_VALUE);
        assertThat(map.getAsFloat(Float.class.getSimpleName())).isEqualTo(FLOAT_VALUE);
        assertThat(map.getAsDouble(Double.class.getSimpleName())).isEqualTo(DOUBLE_VALUE);
        assertThat(map.getAsBoolean(Boolean.class.getSimpleName())).isEqualTo(BOOLEAN_VALUE);
        assertThat(map.getAsString(String.class.getSimpleName())).isEqualTo(STRING_VALUE);
    }

    /**
     * Test should return provided default values when key does not exist in the map.
     */
    @Test
    @DisplayName("Test should return default values for non-existing keys")
    public void testShouldReturnDefaultValues() {
        assertThat(map.getAsInteger(Integer.class.getSimpleName(), INT_VALUE)).isEqualTo(INT_VALUE);
        assertThat(map.getAsLong(Long.class.getSimpleName(), LONG_VALUE)).isEqualTo(LONG_VALUE);
        assertThat(map.getAsFloat(Float.class.getSimpleName(), FLOAT_VALUE)).isEqualTo(FLOAT_VALUE);
        assertThat(map.getAsDouble(Double.class.getSimpleName(), DOUBLE_VALUE)).isEqualTo(DOUBLE_VALUE);
        assertThat(map.getAsBoolean(Boolean.class.getSimpleName(), BOOLEAN_VALUE)).isEqualTo(BOOLEAN_VALUE);
        assertThat(map.getAsString(String.class.getSimpleName(), STRING_VALUE)).isEqualTo(STRING_VALUE);
    }

    /**
     * Test should fail when requesting an object with non-existing key.
     */
    @Test
    @DisplayName("Test should fail on non-existing key")
    public void testShouldFailOnNonExistingKey() {
        assertThatThrownBy(() -> map.get("non-existing-key")).isInstanceOf(MissingKeyException.class);
        assertThatThrownBy(() -> map.getAsInteger("non-existing-key")).isInstanceOf(MissingKeyException.class);
    }

    /**
     * Test that key values are case-insensitive.
     */
    @Test
    @DisplayName("Test should ignore key case")
    public void testShouldIgnoreKeyCase() {
        // Add a sample value to the map.
        map.add(KEY, STRING_VALUE);

        // Generate a new key with the same content but with different casing.
        String key2 = Character.isLowerCase(KEY.charAt(0)) ? KEY.toUpperCase() : KEY.toLowerCase();

        // Assert that regardless of the key casing, the same object is returned.
        assertThat((Object) map.get(key2)).isEqualTo(STRING_VALUE);
    }
}
