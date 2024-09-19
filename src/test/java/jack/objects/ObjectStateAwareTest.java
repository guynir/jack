package jack.objects;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

/**
 * Test suite for {@link ObjectStateAware}.
 *
 * @author Guy Raz Nir
 * @since 2021/09/30
 */
public class ObjectStateAwareTest {

    /**
     * Test that two objects with the same state are considered equal.
     */
    @Test
    @DisplayName("Test should detect equality for same state")
    public void testShouldDetectEqualityForSameState() {
        Assertions.assertThat(new SampleObj("Hi")).isEqualTo(new SampleObj("Hi"));
        Assertions.assertThat(new SampleObj(Arrays.asList(1, 2, 3))).isEqualTo(new SampleObj(Arrays.asList(1, 2, 3)));
    }

    /**
     * Test that two objects with different state are considered not equal.
     */
    @Test
    @DisplayName("Test should detect inequality for different state")
    public void testShouldDetectInequalityForDifferentStates() {
        Assertions.assertThat(new SampleObj("Hi")).isNotEqualTo(new SampleObj("Goodbye"));
        Assertions.assertThat(new SampleObj(Arrays.asList(1, 2, 3))).isNotEqualTo(new SampleObj(Arrays.asList(1, 2, 4)));
    }

    /**
     * Test that two bag-aware objects with the same state generate the same hash code.
     */
    @Test
    @DisplayName("Test should generate the same hash code for the same state")
    public void testShouldGenerateSameHashCodeForTheSameStates() {
        SampleObj o1 = new SampleObj("Hi");
        SampleObj o2 = new SampleObj("Hi");

        Assertions.assertThat(o1.hashCode()).isEqualTo(o2.hashCode());
        Assertions.assertThat(o1.hashCode()).isGreaterThan(0);
    }

    /**
     * Test that two objects of different state generate different hash code.
     */
    @Test
    @DisplayName("Test should generate different hash code for different states")
    public void testShouldGenerateDifferentHashCodeForTheDifferentStates() {
        SampleObj o1 = new SampleObj(Arrays.asList(1, 2, 3));
        SampleObj o2 = new SampleObj("Hi");

        Assertions.assertThat(o1.hashCode()).isNotEqualTo(o2.hashCode());
        Assertions.assertThat(o1.hashCode()).isGreaterThan(0);
    }

    private static class SampleObj extends ObjectStateAware {

        public Object value;

        public SampleObj() {
        }

        public SampleObj(Object value) {
            this.value = value;
        }

        @Override
        protected ObjectState state() {
            return new ObjectState(value);
        }
    }
}
