package jack.i18n.resources;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test suite for {@link ResourceBundlePackBuilder}.
 *
 * @author Guy Raz Nir
 * @since 2024/08/02
 */
public class ResourceBundlePackBuilderTest {

    /**
     * Test generation of a resource bundle pack with default-only a resource bundle.
     */
    @Test
    @DisplayName("Test should build single resource bundle")
    public void testShouldBuildSingleResourceBundle() {
        ResourceBundlePack pack = ResourceBundlePackBuilder
                .newBuilder()
                .setDefaultLanguage("en")
                .registerResource("FIRST_NAME", String.class)
                .addResource("en", "FIRST_NAME", "First name")
                .build();

        assertThat(pack.getLocales()).isEqualTo(List.of(new Locale("en")));
        assertThat(pack.getDefault().getAsString("FIRST_NAME")).isEqualTo("First name");
    }

    /**
     * Test that a bundle with parent can inherit and override a resource value.
     */
    @Test
    @DisplayName("Test should fetch override resource")
    public void testShouldFetchOverriddenResource() {
        final String RESOURCE1 = "RESOURCE1";
        final String RESOURCE2 = "RESOURCE2";
        final String PARENT_VALUE = "Parent";
        final String CHILD_OVERRIDE_VALUE = "Child";

        final Locale EN = Locale.ENGLISH;
        final Locale EN_US = new Locale("en", "US");

        //
        // Define a bundle pack with 2 resource bundlers. Each contains 2 resources.
        //
        ResourceBundlePack pack = ResourceBundlePackBuilder
                .newBuilder()
                .setDefaultLanguage("en")
                .registerResource(RESOURCE1, String.class)
                .registerResource(RESOURCE2, String.class)
                .addResource("en", RESOURCE1, PARENT_VALUE)
                .addResource("en", RESOURCE2, PARENT_VALUE)
                .addResource("en_US", RESOURCE2, CHILD_OVERRIDE_VALUE)
                .build();

        // Parent bundle have 2 keys define, both with the value 'Parent'.
        assertThat(pack.getDefault().getAsString(RESOURCE1)).isEqualTo(PARENT_VALUE);
        assertThat(pack.getDefault().getAsString(RESOURCE2)).isEqualTo(PARENT_VALUE);

        // Child bundle inherits 'RESOURCE1' from the parent bundle ('en') and a resource RESOURCE2
        // overridden with its own value.
        assertThat(pack.getBundle(EN_US).getAsString(RESOURCE1)).isEqualTo(PARENT_VALUE);
        assertThat(pack.getBundle(EN_US).getAsString(RESOURCE2)).isEqualTo(CHILD_OVERRIDE_VALUE);
    }

    /**
     * Test that a parent resource bundle cannot be created it is missing a resource.
     */
    @Test
    @DisplayName("Test should fail on validation of missing resource")
    public void testShouldFailValidationOnMissingResources() {
        //
        // Define a bundle pack with 2 resource bundlers. Each contains 2 resources.
        //
        List<String> errors = ResourceBundlePackBuilder
                .newBuilder()
                .setDefaultLanguage("en")
                .registerResource("Resource1", String.class)
                .registerResource("Resource2", String.class)
                .addResource("en", "Resource1", "")
                .validate();

        assertThat(errors).hasSize(1);
    }

    /**
     * Test that validation fails when a non-inheritable resource is not defined on a child bundle (non-inheritable
     * resource must be defined by all bundles).
     */
    @Test
    @DisplayName("Test should fail on missing non-inheritable resource")
    public void testShouldFailOnMissingNonInheritableResource() {
        //
        // Define a bundle pack with 2 resource bundles, having 2 non-inheritable properties.
        // The parent bundle has all properties defined. The child is missing a mandatory (non-inheritable) resource.
        //
        List<String> errors = ResourceBundlePackBuilder
                .newBuilder()
                .setDefaultLanguage("en")
                .registerResource("Resource1", String.class, false)
                .registerResource("Resource2", String.class, false)
                .addResource("en", "Resource1", "")
                .addResource("en", "Resource2", "")
                .addResource("en_US", "Resource1", "")
                .validate();

        assertThat(errors).hasSize(1);
    }

}
