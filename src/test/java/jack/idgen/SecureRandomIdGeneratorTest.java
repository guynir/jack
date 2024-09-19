package jack.idgen;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class SecureRandomIdGeneratorTest {

    private SecureRandomIdGenerator generator;

    /**
     * Test fixture -- create a new instance of {@link SecureRandomIdGenerator} before each test is executed.
     */
    @BeforeEach
    public void setUp() {
        generator = new SecureRandomIdGenerator();
    }

    /**
     * Test that for each call to {@link SecureRandomIdGenerator#generate()}, a unique value is generated.
     * This test runs 1,000 tries, which is more than enough.
     */
    @Test
    @DisplayName("Test should verify uniqueness of identifier generator")
    public void testShouldGenerateUniqueValues() {
        final int MAX_TRIES = 1000;

        Set<String> values = new HashSet<>(MAX_TRIES);
        for (int i = 0; i < MAX_TRIES; i++) {
            String newId = generator.generate();
            boolean exists = values.add(newId);

            assertThat(exists).as("Identifier appears more than once: %s", newId).isTrue();
        }
    }

    /**
     * Ensures that a generated value is of expected length.
     */
    @Test
    @DisplayName("Test should ensure generated value length")
    public void testShouldEnsureGenerateValueLength() {
        SecureRandomIdGenerator generator = new SecureRandomIdGenerator();
        generator.setLength(32);
        assertThat(generator.generate()).hasSize(32);

        generator.setLength(80);
        assertThat(generator.generate()).hasSize(80);

        generator.setLength(2);
        assertThat(generator.generate()).hasSize(2);
    }
}
