package jack.i18n.resources;

import java.util.Collection;
import java.util.Locale;
import java.util.Map;

/**
 * <p>A strategy that determines the relationship between locales. This strategy is used by {@link ResourceBundlePackBuilder} to
 * create a hierarchy within resource bundles.
 * </p>
 * The caller provides a collection of locales and returns a parent/child relationships.
 * A locale which has no parent is called <i>root locale</i>.
 * For example, given locales <i>en</i> and <i>en_US</i>, <i>en</i> may be <i>root locale</i> and <i>en_US</i> should be
 * the child or <i>en</i>.
 * Thus, creating the following mapping:
 * <pre>
 *     en -> null
 *     en_US -> en
 * </pre>
 *
 * @author Guy Raz Nir
 * @since 2024/10/24
 */
public interface HierarchyStrategy {

    /**
     * Generate a relationship map between locales.
     *
     * @param locales List of locales to generate mapping for.
     * @return A parent/child map of locales.
     * @throws IllegalArgumentException       If <i>locales</i> is {@code null}.
     * @throws CyclicLocaleReferenceException If a cyclic reference detected within locales.
     */
    Map<Locale, Locale> getRelationships(Collection<Locale> locales) throws IllegalArgumentException, CyclicLocaleReferenceException;
}
