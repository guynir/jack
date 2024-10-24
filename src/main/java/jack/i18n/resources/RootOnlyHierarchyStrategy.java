package jack.i18n.resources;

import jack.utils.Asserts;

import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * <p>A hierarchy strategy that makes all given locales as <i>root locales</i>.
 * </p>
 * For example, given the following locales: <i>en</i>, <i>en_US</i>, <i>en_CA</i>, the following map is created:
 * <pre>
 *     en -> null
 *     en_US -> null
 *     en_CA -> null
 * </pre>
 *
 * @author Guy Raz Nir
 * @since 2024/10/24
 */
public class RootOnlyHierarchyStrategy implements HierarchyStrategy {

    /**
     * Generate a <i>root locales</i> only map.
     *
     * @param locales List of locales to generate mapping for.
     * @return Map of all locales, where each key is a locale and each value is always {@code null}.
     * @throws IllegalArgumentException If <i>locales</i> is {@code null}.
     */
    @Override
    public Map<Locale, Locale> getRelationships(Collection<Locale> locales) {
        Asserts.notNull(locales, "Locales collection cannot be null.");

        Map<Locale, Locale> relationships = new HashMap<>(locales.size());
        locales.forEach(locale -> relationships.put(locale, null));
        return relationships;
    }
}
