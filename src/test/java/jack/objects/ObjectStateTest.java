package jack.objects;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Test suite for {@link ObjectState}.
 *
 * @author Guy Raz Nir
 * @since 2021/09/30
 */
public class ObjectStateTest {

    /**
     * Test that two states with primitive values are equal.
     */
    @Test
    @DisplayName("Test should detect equality between equal states.")
    public void testShouldResultInEquality() {
        ObjectState os1 = new ObjectState(null, "Hi");
        ObjectState os2 = new ObjectState(null, "Hi");

        Assertions.assertThat(os1).isEqualTo(os2);
    }

    /**
     * Test that two different states with primitive values are not equal.
     */
    @Test
    @DisplayName("Test should detect inequality between unequal states.")
    public void testShouldResultInInequality() {
        ObjectState os1 = new ObjectState(null, "Hi");
        ObjectState os2 = new ObjectState(null, "Goodbye");

        Assertions.assertThat(os1).isNotEqualTo(os2);
    }

}
