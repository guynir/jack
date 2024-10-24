package jack.i18n.resources;

import java.util.*;
import java.util.stream.Collectors;

public class LocaleBasedHierarchyStrategy implements HierarchyStrategy {

    /**
     * Class constructor.
     */
    public LocaleBasedHierarchyStrategy() {
    }

    /**
     * Convert a given <i>locale</i> into an array of values:
     * <ul>
     *     <li><i>(language, script, country, variant)</i></li>
     *     <li><i>(language, country, variant)</i></li>
     *     <li><i>(language, country)</i></li>
     *     <li><i>(language)</i></li>
     * </ul>
     * <p>
     * Based on the properties available by the locale.<p>
     * </p>
     *
     * @param locale Locale to split into parts.
     * @return An array object (array wrapper) holding all the locale parts.
     */
    private static ArrayObject toArrayObject(Locale locale) {
        String[] parts = new String[4];
        int offset = 0;

        if (locale.getISO3Language().isEmpty()) {
            throw new IllegalArgumentException("Locale is missing language property (mandatory).");
        }

        parts[offset++] = locale.getISO3Language();
        if (!locale.getScript().isEmpty()) {
            parts[offset++] = locale.getScript();
        }
        if (!locale.getCountry().isEmpty()) {
            parts[offset++] = locale.getCountry();
        }
        if (!locale.getVariant().isEmpty()) {
            parts[offset++] = locale.getVariant();
        }

        return ArrayObject.fromArray(Arrays.copyOf(parts, offset));
    }

    /**
     * Given a list of locales, build references (parent/child) between them.
     * For example, given the following locales:
     * <ul>
     *     <li>zh-Hans-CN (Simplified Chinese in China)</li>
     *     <li>zh-Hans (Simplified Chinese)</li>
     *     <li>zh-Hant-TW (Traditional Chinese in Taiwan)</li>
     *     <li>zh-Hant (Traditional Chinese)</li>
     *     <li>zh (Chinese)</li>
     *     <li>en-US (English in US)</li>
     *     <li>en-CA (English in Canada)</li>
     *     <li>en (English)</li>
     *     <li>he-IL (Hebrew in Israel)</li>
     * </ul>
     * <p>
     * The following mapping should be produced (<child> -> <parent> form):
     * <ul>
     *     <li>zh-Hans-CN -> zh-Hans</li>
     *     <li>zh-Hans -> zh</li>
     *     <li>zh-Hant-TW -> zh-Hant</li>
     *     <li>zh-Hant -> zh</li>
     *     <li>en-US -> en</li>
     *     <li>en-CA -> en</li>
     *     <li>zh -> null</li>
     *     <li>en -> null</li>
     *     <li>he-IL -> null</li>
     * </ul>
     * <p>
     * The locales with <i>zh</i> and <i>en</i> will get a {@code null} parent, as they are the top-most
     * locales in the hierarchy.
     *
     * @param locales List of locales to produce mapping for.
     * @return A map between a locale and its parent; a mapping between a locale and a {@code null} value are
     * assigned to a top-most locales in the hierarchy that have no parents.
     * @throws IllegalArgumentException If <i>locales</i> is {@code null}.
     */
    @Override
    public Map<Locale, Locale> getRelationships(Collection<Locale> locales) throws IllegalArgumentException {
        //
        // Map between a locale parts and the locale object.
        // This will help to quickly look up locales by their parts.
        //
        Map<ArrayObject, Locale> localesMap = locales.stream().collect(Collectors.toMap(LocaleBasedHierarchyStrategy::toArrayObject, e -> e));

        //
        // Build a map between a locale and its parent.
        //
        Map<Locale, Locale> results = new HashMap<>(locales.size());
        for (Locale locale : locales) {
            ArrayObject localeKey = toArrayObject(locale).reduce();
            while (localeKey.size() > 1 && !localesMap.containsKey(localeKey)) {
                localeKey = localeKey.reduce();
            }
            results.put(locale, localesMap.get(localeKey));
        }
        return results;
    }

    /**
     * A simple wrapper object for an array of objects. Intended to be used as a {@link java.util.Map} key (which
     * requires implementation of {@link Object#hashCode()} and {@link Object#equals(Object)}).
     */
    @SuppressWarnings("ClassCanBeRecord")
    private static class ArrayObject {

        /**
         * Array of objects.
         */
        private final Object[] array;

        /**
         * Class constructor.
         *
         * @param array Array of objects.
         */
        public ArrayObject(Object... array) {
            this.array = array;
        }

        /**
         * Construct a new instance from a given array of objects.
         *
         * @param array Array of objects.
         * @return New instance.
         */
        public static ArrayObject fromArray(Object[] array) {
            return new ArrayObject(array);
        }

        /**
         * @return A new {@code ArrayObject} instance with the same original values, reducing the last value.
         */
        public ArrayObject reduce() {
            if (array.length == 0) {
                throw new IllegalStateException("Cannot reduce an empty array");
            }

            return new ArrayObject(Arrays.copyOf(array, array.length - 1));
        }

        public int size() {
            return array.length;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof ArrayObject that)) return false;
            return Objects.deepEquals(array, that.array);
        }

        @Override
        public int hashCode() {
            return Arrays.hashCode(array);
        }
    }
}