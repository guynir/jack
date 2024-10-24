package jack.i18n.resources;

import java.util.*;
import java.util.stream.Collectors;

/**
 * A resource bundle pack contains all message aggregated by a locale.
 *
 * @author Guy Raz Nir
 * @since 2024/07/28
 */
public class ResourceBundlePack {

    /**
     * List of all locales within this pack.
     */
    private final List<Locale> locales;

    /**
     * The default locale for this pack.
     */
    private final Locale defaultLocale;

    /**
     * All resource bundles keyed by their locale.
     */
    private final Map<Locale, ResourceBundle> bundlesMap;

    /**
     * Holds a mapping between a locale and a human-readable name (e.g., <i>en_US</i> -> <i>English (United States)</i>).
     */
    private final Map<String, String> displayableLocales;

    /**
     * Holds a mapping between a ISO-639 language code (3-characters) and a human-readable name of the language.
     */
    private final Map<String, String> displayableLanguages;

    /**
     * Class constructor.
     *
     * @param bundlesMap    A map of all bundles.
     * @param defaultLocale Default locale for this pack. Must be one that exists in <i>bundlesMap</i>.
     */
    ResourceBundlePack(Map<Locale, ResourceBundle> bundlesMap, Locale defaultLocale) {
        this.locales = new ArrayList<>(bundlesMap.keySet());
        this.defaultLocale = defaultLocale;
        this.bundlesMap = bundlesMap;
        this.displayableLocales = createDisplayableLocales(locales);
        this.displayableLanguages = createDisplayableLanguages(locales);
    }

    /**
     * Create a collection of languages in a human-readable form.
     *
     * @param locales List of locales.
     * @return A map between an ISO 639-2 3-characters country code and a human-readable name of the language.
     * The generated collection is immutable.
     */
    private static Map<String, String> createDisplayableLanguages(List<Locale> locales) {
        Map<String, String> map = new HashMap<>();
        locales.forEach(locale -> {
            if (map.containsKey(locale.getISO3Language())) {
                map.put(locale.getISO3Language(), locale.getDisplayLanguage());
            }
        });

        return Collections.unmodifiableMap(map);
    }

    /**
     * Create a list of displayable locales. For further information, see {@link #getDisplayableLocales()}.
     *
     * @param locales List of locales.
     * @return A map between a locale tag and its human-readable name. The generated collection is immutable.
     */
    private static Map<String, String> createDisplayableLocales(List<Locale> locales) {
        return Collections.unmodifiableMap(locales
                .stream()
                .collect(Collectors.toMap(Locale::toLanguageTag, Locale::getDisplayName)));

    }

    /**
     * @return List of supported locales.
     */
    public List<Locale> getLocales() {
        return locales;
    }

    /**
     * @return The default (primary) locale.
     */
    public Locale getDefaultLocale() {
        return defaultLocale;
    }

    /**
     * @return The resource bundle of the default locale.
     */
    public ResourceBundle getDefault() {
        return getBundle(defaultLocale);
    }

    public ResourceBundle getBundle(Locale locale) throws UnknownLocaleException {
        return bundlesMap.get(locale);
    }

    public Map<String, String> getLanguages() {
        return locales.stream().collect(Collectors.toMap(Locale::getLanguage, Locale::getCountry));
    }

    /**
     * @return A map collection of "displayable locales" where the key is the locale tag (e.g., <i>en_US</i>) and the
     * value is a human-readable text that includes the language and optionally the country (e.g., <i>English
     * (United States)</i>).
     */
    public Map<String, String> getDisplayableLocales() {
        return displayableLocales;
    }

    /**
     * @return A collection (in the form of a map) of languages. The returned map contains language codes (ISO-639
     * 3-characters country codes) mapped to a human-readable language name.
     */
    public Map<String, String> getDisplayableLanguages() {
        return displayableLanguages;
    }
}
