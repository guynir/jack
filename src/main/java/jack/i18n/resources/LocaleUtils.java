package jack.i18n.resources;

import jack.utils.Asserts;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.stream.Collectors;

/**
 * A collection of utilities to parse, validate and order locales.
 *
 * @author Guy Raz Nir
 * @since 2024/07/30
 */
public class LocaleUtils {

    /**
     * Cache of already parsed locale tags.
     */
    private static final Map<String, Locale> cache = new ConcurrentHashMap<>();

    /**
     * Parse a locale and return a new {@link Locale} object.
     * This implementation uses an internal cache for better performance and lower memory usages.
     *
     * @param localeTag String to parse as a locale tag. If the tag is provided as an old-style Java locale tag (e.g.: en_US),
     *                  it is converted to IETF BCP 47 format (e.g.: en-US).`
     * @return New {@code Locale} object.
     * @throws IllegalArgumentException   If either the argument is {@code null} or {@link String#isEmpty() empty}.
     * @throws UnsupportedLocaleException If provided locale string is not a valid locale tag.
     */
    public static Locale parseLocale(String localeTag) {
        Asserts.notEmpty(localeTag, "Locale tag is null or empty.");
        // Firstly, check if a given locale tag already exists in our cache.
        Locale locale = cache.get(localeTag);
        if (locale != null) {
            return locale;
        }

        // If a caller provided an old-Java style locale tag (e.g.: en_US), convert it to
        // IETF BCP 47 format (e.g.: en-US).
        localeTag = localeTag.trim().replace("_", "-");
        locale = Locale.forLanguageTag(localeTag);
        validateLocale(locale, localeTag);

        // If locale has a variant - we remove it. We do not support locale variants at this time.
        if (!locale.getVariant().isEmpty()) {
            // Creat a new locale without a variant.
            locale = new Locale.Builder().setLocale(locale).setVariant("").build();

            // Try to use an existing object from our cache, if any.
            locale = cache.getOrDefault(locale.toString(), locale);
        }

        // Add newly created locale to cache.
        cache.put(localeTag, locale);

        return locale;
    }

    /**
     * Validate that a given <i>locale</i> is valid (has at least valid language assigned to it).
     *
     * @param locale A locale to test.
     * @throws UnsupportedLocaleException If provide <i>locale</i> is invalid.
     */
    public static void validateLocale(Locale locale) throws UnsupportedLocaleException {
        validateLocale(locale, null);
    }

    /**
     * Validate that a given <i>locale</i> is valid (has at least valid language assigned to it).
     *
     * @param locale    A locale to test.
     * @param localeTag Optionally, the raw locale tag originated the provided <i>locale</i>.
     *                  Included in case of an exception.
     * @throws UnsupportedLocaleException If provide <i>locale</i> is invalid.
     */
    public static void validateLocale(Locale locale, String localeTag) throws UnsupportedLocaleException {
        boolean valid;
        try {
            if (locale.getISO3Language().isEmpty()) {
                throw new UnsupportedLocaleException(formatLocaleValidationMessage(localeTag));
            }
        } catch (MissingResourceException ex) {
            throw new UnsupportedLocaleException(formatLocaleValidationMessage(localeTag));
        }
    }

    /**
     * <p>Construct a list of locale ordered by reference.
     * Parents appear first on the list while their children follow.
     * </p>
     * If a cyclic reference is found inside a map, such as:
     * <pre>
     *     en -> en_US
     *     en_US -> en
     * </pre>
     * then an exception is thrown.
     *
     * @param relationshipMap A child-parent map.
     * @return List of locales ordered by reference.
     * @throws IllegalArgumentException       If <i>relationshipMap</i> is {@code null}.
     * @throws CyclicLocaleReferenceException If a cyclic reference is found in the relationship map.
     */
    public static List<Locale> orderLocalesByReference(Map<Locale, Locale> relationshipMap)
            throws IllegalArgumentException, CyclicLocaleReferenceException {
        List<Locale> result = new ArrayList<>(relationshipMap.size());
        Deque<Locale> stack = new LinkedBlockingDeque<>();
        relationshipMap.keySet().forEach(locale -> {
            if (!result.contains(locale)) {
                appendLocale(locale, result, relationshipMap, stack);
            }
        });

        return result;
    }

    /**
     * Implements a recursive process that adds parents-first and children-after to a <i>target</i> list.
     *
     * @param locale         Locale to process.
     * @param target         Target list to append locales to.
     * @param childParentMap A child-parent reference map.
     * @param stack          A stack collection, used for detecting cyclic reference.
     */
    private static void appendLocale(Locale locale, List<Locale> target, Map<Locale, Locale> childParentMap, Deque<Locale> stack) {
        //
        // Check for cyclic reference.
        //
        if (stack.contains(locale)) {
            List<String> names = stack.stream().map(Locale::toString).collect(Collectors.toCollection(LinkedList::new));
            names.add(locale.toString());
            String cycleReferenceLocales = String.join(" -> ", names);
            throw new CyclicLocaleReferenceException("Cyclic reference -- " + cycleReferenceLocales);
        }
        stack.addLast(locale);

        //
        // Lookup current's locale parent.
        //
        Locale parentLocale = childParentMap.get(locale);
        if (parentLocale != null && !target.contains(parentLocale)) {
            // Make sure the parent of the current locale is also defined in the reference map.
            if (!childParentMap.containsKey(parentLocale)) {
                throw new IllegalStateException("Locale '%s' does not exist (referenced by '%s').".formatted(parentLocale, locale));
            }

            // Recursively, append parent(s) of this locale.
            appendLocale(parentLocale, target, childParentMap, stack);
        }

        // Append top-most locale to the output list.
        target.add(locale);

        // Remove current locale from stack (used to detect cyclic reference).
        stack.removeLast();
    }


    /**
     * Format a simple error message with or without a locale tag.
     *
     * @param localeTag Locale tag to use for the message. May be {@code null}.
     * @return Formatted error message.
     */
    private static String formatLocaleValidationMessage(String localeTag) {
        return "Unsupported or invalid locale" + (localeTag != null ? " -- " + localeTag : "") + ".";
    }
}
