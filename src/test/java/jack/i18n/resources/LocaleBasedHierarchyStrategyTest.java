package jack.i18n.resources;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatException;

/**
 * A test suite for {@link LocaleBasedHierarchyStrategy}.
 *
 * @author Guy Raz Nir
 * @since 2024/08/02
 */
public class LocaleBasedHierarchyStrategyTest {

    /**
     * Instance to run tests on.
     */
    private final LocaleBasedHierarchyStrategy strategy = new LocaleBasedHierarchyStrategy();

    /**
     * Test that strategy produce a correct child-parent mapping.
     */
    @Test
    @DisplayName("Test should map a child locale to parent locale")
    public void testShouldMapLocaleToParentLocale() {
        Locale parent = new Locale("en");
        Locale child = new Locale("en", "US");

        Map<Locale, Locale> map = strategy.getRelationships(Arrays.asList(parent, child));

        // Generated map should contain two mappings:
        //  en -> null (language English is root and has no parent).
        //  en_US -> en (English-US has a parent -- English).
        assertThat(map.size()).isEqualTo(2);
        assertThat(map).containsKeys(parent, child);
        assertThat(map.get(parent)).isNull();
        assertThat(map.get(child)).isEqualTo(parent);
    }

    /**
     * Test that a long locale definition (such as <i>sr-Latn-BA-tag1-tag2</i> [Serbian (Latin, Bosnia, Herzegovina)])
     * with two variants/tags -- can find a match on a locale which is much shorter (language only, e.g.: <i>sr</i>
     * [Serbian]).
     */
    @Test
    @DisplayName("Test that a long-property child can have a matching short parent")
    public void testShouldMapLongLocateToShortParent() {
        Locale parent = new Locale("sr");
        Locale child = Locale.forLanguageTag("sr-Latn-BA-tag1-tag2");
        Map<Locale, Locale> map = strategy.getRelationships(List.of(parent, child));

        assertThat(map.get(child)).isEqualTo(parent);
    }

    /**
     * Test that when one of the locales is missing a language (a mandatory property), the generation
     * of parent/child references fails.
     */
    @Test
    @DisplayName("Test should fail on missing language in one of the locales")
    public void testShouldFailOnMissingLanguage() {
        Locale locale = new Locale("", "US");
        assertThatException()
                .isThrownBy(() -> strategy.getRelationships(List.of(locale)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    /**
     * Test that {@link LocaleUtils#orderLocalesByReference(Map)} construct a list in which
     * parents appear first and children follows.
     */
    @Test
    @DisplayName("Test should construct locale list ordered by reference")
    public void testShouldConstructLocaleOrderedByReference() {
        Locale localeSR1 = Locale.forLanguageTag("sr-Latn-BA-tag11-tag22");
        Locale localeSR2 = Locale.forLanguageTag("sr-Latn-BA");
        Locale localeSR3 = Locale.forLanguageTag("sr-Latn");
        Locale localeSR = Locale.forLanguageTag("sr");
        Locale localeEN = Locale.ENGLISH;
        Map<Locale, Locale> map = strategy.getRelationships(Arrays.asList(localeSR1,
                localeSR,
                localeSR2,
                localeSR3,
                Locale.ENGLISH));
        List<Locale> orderedList = LocaleUtils.orderLocalesByReference(map);

        assertThat(orderedList).containsExactly(localeSR, localeSR3, localeSR2, localeSR1, Locale.ENGLISH);
    }

    /**
     * Test that {@link LocaleUtils#orderLocalesByReference(Map)} can detect cyclic reference.
     */
    @Test
    @DisplayName("Test should fail on cyclic reference")
    public void testShouldFailOnCyclicReference() {
        //
        // Create a list of locales that have cyclic reference.
        //
        Locale locale1 = new Locale("en", "US");
        Locale locale2 = new Locale("en", "CA");
        Locale locale3 = new Locale("en", "AU");
        Locale locale4 = new Locale("en", "GB");
        Locale locale5 = new Locale("en", "NZ");
        Map<Locale, Locale> map = Map.of(
                locale1, locale2,
                locale2, locale3,
                locale3, locale4,
                locale4, locale5,
                locale5, locale1
        );

        // Should fail with exception indicating a cyclic reference.
        assertThatException()
                .isThrownBy(() -> LocaleUtils.orderLocalesByReference(map))
                .isInstanceOf(CyclicLocaleReferenceException.class)
                .withMessageContaining("Cyclic reference");
    }

    /**
     * Test that an exception is raised if a child reference a parent that does not exist in the
     * child-parent map.
     */
    @Test
    @DisplayName("Test should fail on missing parent")
    public void testShouldFailOnMissingParent() {
        //
        // Create a child-parent map containing child referencing an undefined parent (a parent that
        // does not exist in the map).
        //
        Locale child = new Locale("en", "US");
        Locale parent = new Locale("en");
        Map<Locale, Locale> map = Map.of(child, parent);

        // Test should fail with exception.
        assertThatException()
                .isThrownBy(() -> LocaleUtils.orderLocalesByReference(map))
                .isInstanceOf(IllegalStateException.class);

    }
}
